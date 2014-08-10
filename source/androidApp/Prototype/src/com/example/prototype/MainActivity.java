package com.example.prototype;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;


public class MainActivity extends Activity {
	
	private GoogleMap mMap;
	static final LatLng KOBLENZ = new LatLng(50.3511528, 7.5951959);
	private LatLng position = KOBLENZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.addMarker(new MarkerOptions()
        	.position(KOBLENZ)
        	.title("i am draggable")
        	.draggable(true));
        	mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
				
				@Override
				public boolean onMarkerClick(Marker arg0) {
					arg0.setSnippet("Lat: " + position.latitude  + "\n" + "Lng: " + position.longitude);
					return false;
				}
			});
        	
        	mMap.setOnMarkerDragListener(new OnMarkerDragListener() {
				
				@Override
				public void onMarkerDragStart(Marker arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onMarkerDragEnd(Marker arg0) {
					position = arg0.getPosition();
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onMarkerDrag(Marker arg0) {
					// TODO Auto-generated method stub
					
				}
			});
        		
        		
        		
        
        
    }

}
