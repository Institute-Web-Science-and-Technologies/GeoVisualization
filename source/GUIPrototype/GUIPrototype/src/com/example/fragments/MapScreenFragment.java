package com.example.fragments;

import com.example.guiprototype.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
	    if (this.mMap != null) setUpMap();
	    if (mMap == null) {
	    	this.mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	        if (this.mMap != null) setUpMap();
	    }
	}
	
	protected void initFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.map, fragment);
        fragmentTransaction.commit();
    }
	
	public void setUpMapIfNeeded() {
	    if (this.mMap == null) {
	        this.mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	        if (this.mMap != null) setUpMap();
	    }
	}
	
	private void setUpMap() {
	    this.mMap.setMyLocationEnabled(true);
	    mMap.addMarker(new MarkerOptions()
        	.position(KOBLENZ)
        	.title("marker 1")
        	.draggable(true));
	}
}
