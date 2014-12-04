package geoviz.game.snake;

import geoviz.communication.JeroMQQueue;
import geoviz.communication.TransferObject;
import geoviz.game.Functions;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.example.fragments.MapScreenFragment;
import com.example.guiprototype.SwipeScreen;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

public class Player {

	private String name;

	private Polyline snake;

	private float max_length = Const.SNAKE_START_LENGTH;

	List<LatLng> poss = new LinkedList();


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

				snake.setPoints(poss);
			}
		});

	}


	/**
	 * normalizes the sankes length th max_length
	 */
	void normalize() {
		while (poss.size() > 2 && length() > max_length)
			poss.remove(0);
	}


	/**
	 * increases or decreases Max_length
	 * @param f amount of the change
	 */
	void changeMaxLength(float f) {
		max_length += f;
	}


	/**
	 * calculates and adds the distance between point i and i+1 for i 0, ..., |snake|-2
	 * @return the snake's length
	 */
	float length() {
		float length = 0;
		if (poss.size() > 1)
			for (int i = 0; i < poss.size() - 1; i++) {
				length += Functions.distance(poss.get(i), poss.get(i + 1));
			}
		return length;
	}


	/**
	 * returns the player's position
	 * @return the last point added to the snake
	 */
	LatLng head() {
		return poss.get(poss.size() - 1);
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
	 * checks wether this player and player p collide
	 * @param p a different or the same player
	 * @return true iff collision happened
	 */
	boolean collides(Player p){
		final double RADIUS = Const.SNAKE_COLLISION_RADIUS;
		List<LatLng> collposs = p.poss;
		if (p.name == this.name) {
			if (poss.size() < 3)
				return false;;
			int i = poss.size() - 1;
			double d = 0;
			while (d < RADIUS*2 && i > 1) {
				i--;
				d += Functions.distance(poss.get(i), poss.get(i + 1));
			}
			collposs = poss.subList(0, i);
		}
		return Functions.collides_simple(this.poss, collposs, RADIUS);

		//return Functions.collides(this.poss, collposs);
	}

	/**
	 * updates this snake's state to newer information send by the player
	 * @param t msg send by the player this snake corresponds to
	 */
	void update(TransferObject t) {

		poss.add(t.pos);

		normalize();

		snake.setPoints(poss);

	}

	
	/**
	 * checks collisions between this snake and other objects and handles their consequences
	 * @param players list of all players known
	 * @param chickens list of all chickens known
	 */
	void checkCollision(Map<String, Player> players, List<Chicken> chickens) {
		for (String key : players.keySet()) {
			Player p = players.get(key);
			// if (p.name == this.name)
			// continue;
			if (collides(p)) {
				// final JeroMQQueue jmqq = JeroMQQueue.getInstance();
				// jmqq.sendMsg(TransferObject.TYPE_MSG, null,
				// "Bam!");
				MapScreenFragment.getMSF().initMarker(120, head(), "Collision "+name);
			}
		}

		for (Chicken chicken : chickens) {
			if (!chicken.dead && collides(chicken)) {
				// chicken.kill();
				final JeroMQQueue jmqq = JeroMQQueue.getInstance();
				jmqq.sendMsg(TransferObject.TYPE_KILL_CHICKEN, null, chicken.id);
				jmqq.sendMsg(TransferObject.TYPE_MSG, null, "gained point");

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

}
