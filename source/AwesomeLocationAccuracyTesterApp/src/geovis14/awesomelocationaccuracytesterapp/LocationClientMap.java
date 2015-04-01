package geovis14.awesomelocationaccuracytesterapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

@SuppressLint("NewApi") public class LocationClientMap extends ActionBarActivity implements
		GooglePlayServicesClient.ConnectionCallbacks, LocationListener,
		GooglePlayServicesClient.OnConnectionFailedListener {

	public LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	private Location mCLocation;
	private GoogleMap map;
	File root;
	File dir;
	File file;
	File imageFile;
	boolean writing = false;
	Marker start; 
	Marker stop;
	Polyline beeline;
	Polyline walked;
	List<LatLng> locs = new LinkedList();
	PolylineOptions polylineOptions;

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_client_map);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.LCMap))
		        .getMap();
		this.mLocationClient = new LocationClient(this, this, this);
		this.mLocationRequest = LocationRequest.create();
		this.mLocationRequest
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		this.mLocationRequest.setInterval(500);
		this.mLocationRequest.setFastestInterval(250);
		root = android.os.Environment.getExternalStorageDirectory();

		File dir = new File(android.os.Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "//gpsTestDaten");
		dir.mkdirs();
		file = new File(dir, "locationClientData.txt");
		if (file.exists())
			file.delete();
		/*
		imageFile = new File(dir, "locationClient.jpeg");
		if (imageFile.exists())
			imageFile.delete();
		*/
		map.setMyLocationEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location_client_map, menu);
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
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		if (writing == true) {
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(
						root.getAbsolutePath()
								+ "//gpsTestDaten//locationClientData.txt",
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
	public void onConnected(Bundle dataBundle) {
		mLocationClient.requestLocationUpdates(this.mLocationRequest, this);
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		this.mCLocation = mLocationClient.getLastLocation();

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStart() {
		super.onStart();
		mLocationClient.connect();
		// Connect the client.

	}

	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		mLocationClient.disconnect();
		super.onStop();
	}
	
	public void lcStart(View view) {
		writing=true;
		LatLng stCoord = new LatLng(mLocationClient.getLastLocation().getLatitude(),mLocationClient.getLastLocation().getLongitude());
		start = map.addMarker(new MarkerOptions().position(stCoord)
		        .title("START"));
	}
	public void lcStop (View view){
		writing=false;
		LatLng stopCoord = new LatLng(mLocationClient.getLastLocation().getLatitude(),mLocationClient.getLastLocation().getLongitude());
		stop = map.addMarker(new MarkerOptions().position(stopCoord)
		        .title("STOP"));
	}
	public void lcDraw (View view){
		beeline=map.addPolyline(new PolylineOptions()
	     .add(start.getPosition(), stop.getPosition())
	     .width(5)
	     .color(Color.RED));
		
		polylineOptions = new PolylineOptions();
		polylineOptions.color(Color.BLACK);
		polylineOptions.width(5);
		polylineOptions.addAll(locs);
		map.addPolyline(polylineOptions);
		
		Bitmap bitmap;
		View v1 = getWindow().getDecorView().getRootView();
		v1.setDrawingCacheEnabled(true);
		bitmap = Bitmap.createBitmap(v1.getDrawingCache());
		v1.setDrawingCacheEnabled(false);
		
		OutputStream fout = null;
		
		/*imageFile = new File(dir, "locationClient.jpeg");
		if (imageFile.exists())
			imageFile.delete();
		*/
		
		/*
		try {
		    fout = new FileOutputStream(imageFile);
		    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
		    fout.flush();
		    fout.close();

		} catch (FileNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		*/
	}
}
