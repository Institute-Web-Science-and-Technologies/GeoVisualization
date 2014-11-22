package geoviz.game.snake;

import com.example.fragments.MapScreenFragment;
import com.example.guiprototype.SwipeScreen;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class Chicken {
	
	public final LatLng pos;
	public final float radius;
	public final float val;
	public boolean dead;
	public final String id ;
	
	Marker marker;
	
	/**
	 * creates a new passive object;
	 * creates a new marker on the map to represent that object
	 * @param pos position of the object (fix)
	 * @param radius distance maximum for collision with other objects/players 
	 * @param val not used atm
	 * @param id randomly created id 
	 */
	public Chicken(final LatLng pos, float radius, float val, final String id) {
		super();
		this.pos = pos;
		this.radius = radius;
		this.val = val;
		SwipeScreen.getInstance().runOnUiThread(new Runnable(){

			@Override
			public void run() {
				marker = MapScreenFragment.getMSF().initMarker(240, pos, "Chicken");
				
			}});
		this.id=id;
	}
	
	
	/**
	 * this method is called on collison with a player;
	 * destroys marker and sets dead
	 */
	void kill(){
		//remove marker
		SwipeScreen.getInstance().runOnUiThread(new Runnable(){

			@Override
			public void run() {
				marker.remove();
				
			}
			
		});
		dead=true;
	}

}
