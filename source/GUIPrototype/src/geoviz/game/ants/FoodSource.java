package geoviz.game.ants;

import com.google.android.gms.maps.model.LatLng;

import geoviz.game.SimpleCollidable;


public class FoodSource extends SimpleCollidable {
	
	int capacity=100;

	public FoodSource(LatLng pos) {
		super(pos, 5);
	}
	

}
