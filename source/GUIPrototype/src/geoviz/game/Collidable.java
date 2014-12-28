package geoviz.game;

import com.google.android.gms.maps.model.LatLng;

public interface Collidable {
	
	boolean collides(LatLng ll);

}
