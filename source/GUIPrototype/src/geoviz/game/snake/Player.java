package geoviz.game.snake;

import geoviz.communication.TransferObject;
import geoviz.game.Functions;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.widget.Toast;

import com.example.fragments.MapScreenFragment;
import com.example.guiprototype.SwipeScreen;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

public class Player {

	private String name;

	private Polyline snake;
	
	private float max_length=20;

	List<LatLng> poss = new LinkedList();

	Map<String, Player> players;

	public Player(String name, Map<String, Player> players) {
		this.players = players;
		this.name = name;
		SwipeScreen.getInstance().runOnUiThread(new Runnable() {
			public void run() {
				snake = MapScreenFragment.getMSF().initLine();

				snake.setPoints(poss);
			}
		});

	}

	void normalize() {
		while (length() > max_length)
			poss.remove(0);
	}
	
	void changeMaxLength(float f){
		max_length+=f;
	}

	float length() {
		float length = 0;
		if (poss.size() > 1)
			for (int i = 0; i < poss.size() - 1; i++) {
				length += Functions.distance(poss.get(i), poss.get(i + 1));
			}
		return length;
	}
	
	LatLng head(){
		return poss.get(poss.size()-1);
	}
	
	boolean collides (Chicken chicken){
		return Functions.distance(chicken.pos,head())<chicken.radius;
	}
	
	

	

	void update(TransferObject t) {

		poss.add(t.pos);
		
		Toast.makeText(SwipeScreen.getInstance(), length() + "",
				Toast.LENGTH_SHORT).show();

		normalize();

		snake.setPoints(poss);

		

		/*
		 * for(String key: players.keySet()){ Player p=players.get(key);
		 * if(p.name==this.name) continue; if(collides(this.poss,p.poss)){
		 * //Toast.makeText(context, text, duration);
		 * JeroMQQueue.getInstance().add("Helllllllow!"); } }
		 */
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
