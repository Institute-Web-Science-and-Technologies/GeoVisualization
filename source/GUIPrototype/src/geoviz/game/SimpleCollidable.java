package geoviz.game;

import com.google.android.gms.maps.model.LatLng;

public class SimpleCollidable implements Collidable {

	protected LatLng pos;
	protected float radius;

	public SimpleCollidable(LatLng pos, float radius) {
		super();
		this.pos = pos;
		this.radius = radius;
	}

	@Override
	public boolean collides(LatLng ll) {
		return Functions.distance(pos, ll)<=radius;
	}

}
