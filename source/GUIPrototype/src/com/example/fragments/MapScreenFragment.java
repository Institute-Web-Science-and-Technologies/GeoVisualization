package com.example.fragments;

import geoviz.communication.TransferObject;

import geoviz.game.snake.Player;
import geoviz.game.Game;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guiprototype.R;

import com.google.android.gms.internal.lc;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapScreenFragment extends Fragment {
	private static MapScreenFragment __instance;
	
	
	Map <String, Marker> players=new HashMap<String, Marker> ();
	final static String serverIP = "tcp://heglohitdos.west.uni-koblenz.de";
	
	public static FragmentManager fragmentManager;
	MapView mapView;
	public GoogleMap mMap;
	static final LatLng KOBLENZ = new LatLng(50.3511528, 7.5951959);
	static final LatLng UNI = new LatLng(50.363417, 7.558432);
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map_screen, container, false);
		 
	    
	/*	if (mapView == null){
		MapsInitializer.initialize(getActivity()); 
	    mapView = (MapView)rootView.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		if (mMap == null)
		mMap= mapView.getMap();
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
        mMap.animateCamera(cameraUpdate);
		}*/
		return rootView;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentManager = getActivity().getSupportFragmentManager();
		__instance=this;
	}
	
	public static MapScreenFragment getMSF(){
		return __instance;
	}
	
	public Polyline initLine() {
		return this.mMap.addPolyline(new PolylineOptions()
	     .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
	     .width(5)
	     .color(Color.rgb((int)(Math.random()*250), (int)(Math.random()*250), (int)(Math.random()*250))));
	}
	
	public void drawLine() {
		
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
	    this.setUpMapIfNeeded();
		//setUpMap();
		
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
	public TransferObject testMSF (String userID, String userName, LatLng location){
		
    	TransferObject msg = new TransferObject(0, userID, Calendar
				.getInstance().getTime(), userID, userName, location,Game.getGame().gameID);
    	return msg;
	}
	
	private void setUpMap() {
	   // this.mMap.setMyLocationEnabled(true);
	    //final Marker redFlag = initBase("red", KOBLENZ);
	   // final Marker blueFlag = initBase("blue", UNI);
	    //Marker blueMarker = initMarker(240, new LatLng(50.364661,7.563409));
	    //Marker redMarker = initMarker(0, new LatLng(50.358870, 7.577356));
		
		//Polyline line = this.initLine();
	    
	    this.mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
	    	
			// ##### Berechnet immer die Distanz vom Marker zur roten Fahne
			@Override
			/*public boolean onMarkerClick(Marker arg0) {
				double d = calcDistance(redFlag.getPosition(), arg0.getPosition());
				arg0.setSnippet("distance to red flag: " + (int)d + " meters");
				arg0.showInfoWindow();
				return true;
			}*/
			public boolean onMarkerClick (Marker arg0){
				arg0.setSnippet (arg0.getTitle());
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
				/*double d = calcDistance(redFlag.getPosition(), arg0.getPosition());
				arg0.setSnippet("distance to red flag: " + (int)d + " meters");
				arg0.showInfoWindow();*/				
			}
			
			
			@Override
			public void onMarkerDrag(Marker arg0) {
				// TODO Auto-generated method stub
				
			}
	    }); 
	 
	
	}
	public void handlePosition (String username, LatLng pos){
		if (players.containsKey(username)) players.get(username).setPosition(pos);
		else {
			players.put(username, initMarker((float)Math.random()*240,pos, username));
		}
	}
	
	
	public Marker initMarker(float colorDouble, LatLng position, String name) {
		Marker marker = this.mMap.addMarker(new MarkerOptions()
        	.position(position)
        	.title(name)
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
	    double earthRadius = 6378137; //Radius am �quator in Meter
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
