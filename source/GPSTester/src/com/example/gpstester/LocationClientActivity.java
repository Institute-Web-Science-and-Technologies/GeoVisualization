package com.example.gpstester;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class LocationClientActivity extends ActionBarActivity implements
		GooglePlayServicesClient.ConnectionCallbacks, LocationListener,
		GooglePlayServicesClient.OnConnectionFailedListener {
	public LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	private Location mCLocation;
	Button changeWriting;
	File root;
	File dir;
	File file;
	TextView tw;
	ScrollView sv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_client);
		this.mLocationClient = new LocationClient(this, this, this);
		this.mLocationRequest = LocationRequest.create();
		this.mLocationRequest
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		this.mLocationRequest.setInterval(500);
		this.mLocationRequest.setFastestInterval(250);
		changeWriting = (Button) findViewById(R.id.strButton2);
		root = android.os.Environment.getExternalStorageDirectory();

		File dir = new File(android.os.Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "//gpsTestDaten");
		dir.mkdirs();
		file = new File(dir, "locationClientData.txt");
		if (file.exists())
			file.delete();

		tw = (TextView) findViewById(R.id.Log2);
		sv = (ScrollView) findViewById(R.id.ScrollView2);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location_client, menu);
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

	@Override
	public void onConnected(Bundle dataBundle) {
		mLocationClient.requestLocationUpdates(this.mLocationRequest, this);
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		this.mCLocation = mLocationClient.getLastLocation();
	}

	@Override
	public void onLocationChanged(Location location) {

		tw.append("\n"
				+ new SimpleDateFormat("HH:mm:ss").format(Calendar
						.getInstance().getTime()));
		sv.fullScroll(View.FOCUS_DOWN);

		if (changeWriting.getText().equals("Stop Writing")) {
			try {
				PrintWriter pw=new PrintWriter(new FileWriter (root.getAbsolutePath() + "//gpsTestDaten//locationClientData.txt",true));
				pw.append("\n"
						+ new SimpleDateFormat("HH:mm:ss").format(Calendar
								.getInstance().getTime()));
				pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void changeWriting(View view) {
		if (!changeWriting.getText().equals("Stop Writing"))
			changeWriting.setText("Stop Writing");
		else {
			changeWriting.setText("Start Writing");

		}
	}
}