package geoviz.game.ants;

import geoviz.game.SimpleCollidable;

import com.google.android.gms.maps.model.LatLng;

public class Home extends SimpleCollidable {

	public Home(LatLng pos, int team) {
		super(pos, 5);
		this.team=team;
		// TODO Auto-generated constructor stub
	}

	int team;

	
}
