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
	
	public Player(String name) {
		this.name = name;
		SwipeScreen.getInstance().runOnUiThread(new Runnable(){
			public void run(){
			snake = MapScreenFragment.getMSF().initLine();
			List<LatLng> ps =new LinkedList<LatLng>();
			ps.add(new LatLng(50.3511528, 7.5951959));
			ps.add(new LatLng(50.363417, 7.558432));
			snake.setPoints(ps);
			}
		});
		
	}
	
	void update(TransferObject t){

		List<LatLng> ps =new LinkedList<LatLng>();
		ps.add(t.pos);
		ps.add(new LatLng(50.3511528, 7.5951959));
		snake.setPoints(ps);
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
