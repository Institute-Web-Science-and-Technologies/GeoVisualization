package geoviz.game;

import com.google.android.gms.maps.model.Polyline;

public class Player {

	private String name;
	
	private Polyline snake;
	
	public Player(String name) {
		this.name = name;
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
