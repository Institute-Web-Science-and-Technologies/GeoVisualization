package com.example.fragments;

import com.example.guiprototype.R;
import com.google.android.gms.internal.lc;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapScreenFragment extends Fragment {

	public static FragmentManager fragmentManager;
	private GoogleMap mMap;
	static final LatLng KOBLENZ = new LatLng(50.3511528, 7.5951959);
	static final LatLng UNI = new LatLng(50.363417, 7.558432);
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map_screen, container, false);
		return rootView;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentManager = getActivity().getSupportFragmentManager();
		
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
	    this.setUpMapIfNeeded();
	}
	
	
	
	protected void initFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.map, fragment);
        fragmentTransaction.commit();
    }
	
	public void setUpMapIfNeeded() {
		if (this.mMap == null) this.mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
    	setUpMap();
	}
	
	private void setUpMap() {
	    this.mMap.setMyLocationEnabled(true);
	    final Marker redFlag = initBase("red", KOBLENZ);
	    final Marker blueFlag = initBase("blue", UNI);
	    Marker blueMarker = initMarker(240, new LatLng(50.364661,7.563409));
	    Marker redMarker = initMarker(0, new LatLng(50.358870, 7.577356));
	    
	    this.mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
	    	
			// ##### Berechnet immer die Distanz vom Marker zur roten Fahne
			@Override
			public boolean onMarkerClick(Marker arg0) {
				double d = calcDistance(redFlag.getPosition(), arg0.getPosition());
				arg0.setSnippet("distance to red flag: " + (int)d + " meters");
				arg0.showInfoWindow();
				return true;
			}
    	
    	});
	    
	    this.mMap.setOnMarkerDragListener(new OnMarkerDragListener() {
			
			@Override
			public void onMarkerDragStart(Marker arg0) {
				// TODO Auto-generated method stub
				
			}
		
		
			// #### Wenn der Marker gezogen wird, wird neue Distanz berechnet
			@Override
			public void onMarkerDragEnd(Marker arg0) {
				double d = calcDistance(redFlag.getPosition(), arg0.getPosition());
				arg0.setSnippet("distance to red flag: " + (int)d + " meters");
				arg0.showInfoWindow();				
			}
			
			@Override
			public void onMarkerDrag(Marker arg0) {
				// TODO Auto-generated method stub
				
			}
	    }); 
	    
	}
	
	private Marker initMarker(float colorDouble, LatLng position) {
		Marker marker = this.mMap.addMarker(new MarkerOptions()
        	.position(position)
        	.title("marker")
        	.draggable(true));
		marker.setIcon(BitmapDescriptorFactory.defaultMarker(colorDouble));
		return marker;
        
	}
	
	private Marker initBase(String colorString, LatLng position) {
		int colorInt = Color.parseColor(colorString);
		this.mMap.addCircle(new CircleOptions()
			.center(position)
			.radius(50)
			.strokeColor(colorInt));
		Marker flag = this.mMap.addMarker(new MarkerOptions()
        	.position(position)
        	.title(colorString + " flag"));
		
        flag.setIcon(BitmapDescriptorFactory.fromAsset(colorString + ".png"));
        
        return flag;
	}
	
	//uses the Haversine formula to calculate the distance in meters
	public double calcDistance(LatLng pos1, LatLng pos2) {
		double meter = 0;
		double lat1 = pos1.latitude;
		double lat2 = pos2.latitude;
		double lng1 = pos1.longitude;
		double lng2 = pos2.longitude;
	    double earthRadius = 6378137; //Radius am Äquator in Meter
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);
	    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	            * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    meter = earthRadius * c;
		return meter;
	}
}
