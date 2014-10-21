package geoviz.game.snake;

import geoviz.communication.TransferObject;

import java.util.LinkedList;
import java.util.List;

import com.example.fragments.MapScreenFragment;
import com.example.guiprototype.SwipeScreen;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

public class Player {

	private String name;
	
	private Polyline snake;
	
	List<LatLng> poss = new LinkedList();
	
	public Player(String name) {
		this.name = name;
		SwipeScreen.getInstance().runOnUiThread(new Runnable(){
			public void run(){
			snake = MapScreenFragment.getMSF().initLine();


			snake.setPoints(poss);
			}
		});
		
	}
	
	void update(TransferObject t){


		poss.add(t.pos);

		snake.setPoints(poss);
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
