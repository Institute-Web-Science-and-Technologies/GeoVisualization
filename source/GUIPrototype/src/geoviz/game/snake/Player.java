package geoviz.game.snake;

import geoviz.communication.JeroMQQueue;
import geoviz.communication.TransferObject;
import geoviz.game.Functions;
import geoviz.game.Game;
import geoviz.game.snake.Const;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Vibrator;
import android.widget.Toast;

import com.example.fragments.MapScreenFragment;
import com.example.guiprototype.SwipeScreen;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

public class Player {

	private String name;

	private Polyline snake;
	
	private long invincibilityStart=0;

	private float max_length = Const.SNAKE_START_LENGTH;

	List<LatLng> positions = new LinkedList();


	/**
	 * creates an object to store information about a player/phone;
	 * creates an polyline on the map to represent the snake
	 * @param name the player's chosen name
	 */
	public Player(String name) {
		this.name = name;
		SwipeScreen.getInstance().runOnUiThread(new Runnable() {
			public void run() {
				snake = MapScreenFragment.getMSF().initLine();

				snake.setPoints(positions);
			}
		});

	}
	public Player(String name, List<LatLng> pos){
		this.name=name;
		positions.addAll(pos);
		SwipeScreen.getInstance().runOnUiThread(new Runnable() {
			public void run() {
				snake = MapScreenFragment.getMSF().initLine();

				snake.setPoints(positions);
			}
		});
	}


	/**
	 * normalizes the snakes length th max_length
	 */
	void normalize() {
		while (positions.size() > 2 && length() > max_length)
			positions.remove(0);
	}


	/**
	 * increases or decreases Max_length
	 * @param f amount of the change
	 */
	void changeMaxLength(float f) {
		max_length += f;
	}
	
	public void snakeDied(Date timeOfDeath){
		max_length=Const.SNAKE_START_LENGTH;
		invincibilityStart=timeOfDeath.getTime();
		
	}

	/**
	 * calculates and adds the distance between point i and i+1 for i 0, ..., |snake|-2
	 * @return the snake's length
	 */
	float length() {
		float length = 0;
		if (positions.size() > 1)
			for (int i = 0; i < positions.size() - 1; i++) {
				length += Functions.distance(positions.get(i), positions.get(i + 1));
			}
		return length;
	}


	/**
	 * returns the player's position
	 * @return the last point added to the snake
	 */
	LatLng head() {
		return positions.get(positions.size() - 1);
	}


	/**
	 * checks wether this player and chicken collide
	 * @param chicken an immovable object
	 * @return true iff collision happened
	 */
	boolean collides(Chicken chicken) {
		
		return Functions.distance(chicken.pos, head()) < chicken.radius;
	}
	

	/**
	 * checks whether this player and player p collide
	 * @param p a different or the same player
	 * @return true if collision happened
	 */
	boolean collides(Player p){
		final double RADIUS = Const.SNAKE_COLLISION_RADIUS;
		List<LatLng> collposs = p.positions;
		if (p.name == this.name) {
			if (positions.size() < 3)
				return false;;
			int i = positions.size() - 1;
			double d = 0;
			while (d < RADIUS*2 && i > 1) {
				i--;
				d += Functions.distance(positions.get(i), positions.get(i + 1));
			}
			collposs = positions.subList(0, i);
		}
		return Functions.collides_simple(this.positions, collposs, RADIUS);

		//return Functions.collides(this.poss, collposs);
	}

	/**
	 * updates this snake's state to newer information send by the player
	 * @param t msg send by the player this snake corresponds to
	 */
	void update(TransferObject t) {

		positions.add(t.pos);

		normalize();

		snake.setPoints(positions);

	}

	
	/**
	 * checks collisions between this snake and other objects and handles their consequences
	 * @param players list of all players known
	 * @param chickens list of all chickens known
	 */
	void checkCollision(Date timeStamp,Map<String, Player> players, List<Chicken> chickens) {
		for (String key : players.keySet()) {
			Player p = players.get(key);
			// if (p.name == this.name)
			// continue;
			if (!this.isInvincible() && !p.isInvincible()){
			if (collides(p)) {
				final JeroMQQueue jmqq = JeroMQQueue.getInstance();
				jmqq.sendMsg(TransferObject.TYPE_SNAKE_DIED, Game.getGame().gameID);
				
				Toast.makeText(SwipeScreen.getInstance(), "Connected", Toast.LENGTH_SHORT).show();
				
				//MapScreenFragment.getMSF().initMarker(120, head(), "Collision "+name);
				}
			}
		}

		for (Chicken chicken : chickens) {
			if (!chicken.dead && collides(chicken)) {
				// chicken.kill();
				final JeroMQQueue jmqq = JeroMQQueue.getInstance();
				if (max_length < Const.SNAKE_MAX_LENGTH){
					jmqq.sendMsg(TransferObject.TYPE_KILL_CHICKEN,  chicken.id, Game.getGame().gameID);
					jmqq.sendMsg(TransferObject.TYPE_MSG, "gained point", Game.getGame().gameID);
					
					Vibrator v = (Vibrator) SwipeScreen.getInstance().getSystemService(
							Context.VIBRATOR_SERVICE);
					v.vibrate(Const.vibrateTimeInMs);
					
				}
				else {
					jmqq.sendMsg(TransferObject.TYPE_SNAKE_WINS, Game.getGame().gameID);
				}

			}
		}

	}

	public String getName() {
		return this.name;
	}

	public Player setName(String name) {
		this.name = name;
		return this;
	}

	public Polyline getSnake() {
		return this.snake;
	}
	
	public boolean isInvincible(){
		if (new Date().getTime()-this.invincibilityStart<=10000)return true;
		else return false;
	}
}
