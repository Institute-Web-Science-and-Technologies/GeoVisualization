package geovis14.awesomelocationaccuracytesterapp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class LocationManagerMap extends ActionBarActivity implements LocationListener{
	
	private GoogleMap map;
    LocationManager lm; 
    boolean enabled;
	String provider;
    Location loc;
    File root;
    File dir;
    File file;
    
    boolean writing = false;
	Marker start; 
	Marker stop;
	Polyline beeline;
	Polyline walked;
	List<LatLng> locs = new LinkedList();
	PolylineOptions polylineOptions;
	
	@SuppressLint("NewApi")@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_manager_map);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.LmMap))
		        .getMap();
		root = android.os.Environment.getExternalStorageDirectory();
		dir = new File(root.getAbsolutePath() + "/gpsTestDaten");
		dir.mkdirs();
		file = new File(dir, "locationManagerData.txt");
		if (file.exists())file.delete();
		
		lm = (LocationManager)getSystemService(LOCATION_SERVICE);
		Criteria crit=new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);
		//Toast.makeText(this, "FATAL ERROR, SELFDESTRUCTING IN 3...2...1...BOOOM",Toast.LENGTH_LONG).show();
		//Toast.makeText(this, "Nah just joking",Toast.LENGTH_LONG).show();
		//provider=l.getBestProvider(crit, true);
		provider="gps";
		loc=lm.getLastKnownLocation(provider);
		enabled=lm.isProviderEnabled(provider);
		if (lm.isProviderEnabled("gps")==false) onProviderDisabled ("gps");
		else onProviderEnabled ("gps");
		
		map.setMyLocationEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location_manager_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onLocationChanged(Location location) {
		if (writing == true) {
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(
						root.getAbsolutePath()
								+ "//gpsTestDaten//locationManagerData.txt",
						true));
				pw.append("\n"
						+ new SimpleDateFormat("HH:mm:ss; ").format(Calendar
								.getInstance().getTime()) + "accu: "
						+ location.getAccuracy() + "; " + " Lat="
						+ location.getLatitude() + " Long="
						+ location.getLongitude() + ";");
				pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		locs.add(new LatLng(location.getLatitude(),location.getLongitude()));
		}
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, provider+" is enabled",Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Error, Provider "+provider+" isn't enabled",Toast.LENGTH_LONG).show();		
	}
	public void onResume(){
		super.onResume();
		lm.requestLocationUpdates("gps", 50, 0.1f, this);
	}
	public void onPause()   {
		super.onPause();
		lm.removeUpdates(this);
	}
	public void lmStart(View view) {
		writing=true;
		LatLng stCoord = new LatLng(lm.getLastKnownLocation(provider).getLatitude(), lm.getLastKnownLocation(provider).getLongitude());
		start = map.addMarker(new MarkerOptions().position(stCoord)
		        .title("START"));
	}
	public void lmStop (View view){
		writing=false;
		LatLng stopCoord = new LatLng(lm.getLastKnownLocation(provider).getLatitude(), lm.getLastKnownLocation(provider).getLongitude());
		stop = map.addMarker(new MarkerOptions().position(stopCoord)
		        .title("STOP"));
	}
	public void lmDraw (View view){
		beeline=map.addPolyline(new PolylineOptions()
	     .add(start.getPosition(), stop.getPosition())
	     .width(5)
	     .color(Color.RED));
		
		polylineOptions = new PolylineOptions();
		polylineOptions.color(Color.BLACK);
		polylineOptions.width(5);
		polylineOptions.addAll(locs);
		map.addPolyline(polylineOptions);
	}
}
