package geoviz.game.snake;

import android.graphics.Color;

import com.example.fragments.MapScreenFragment;
import com.example.guiprototype.SwipeScreen;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import android.graphics.Color;

public class Chicken {
	
	public final LatLng pos;
	public final float radius;
	public final float val;
	
	Marker marker;
	
	public Chicken(final LatLng pos, float radius, float val) {
		super();
		this.pos = pos;
		this.radius = radius;
		this.val = val;
		SwipeScreen.getInstance().runOnUiThread(new Runnable(){

			@Override
			public void run() {
				marker = MapScreenFragment.getMSF().initMarker(240, pos, "Chicken");
				
			}});
		
	}
	
	void update(){
		
	}
	
	

}
