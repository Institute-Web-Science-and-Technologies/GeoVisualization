package com.example.gpstester;

import java.util.Calendar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import android.support.v7.app.ActionBarActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
public class LocationManagerActivity extends ActionBarActivity implements LocationListener {

	Button btnShowLocation;
    LocationManager lm; 
    // GPSTracker class
    GPSTracker gps;
    boolean enabled;
	String provider;
    Location loc;
    TextView tw;
    ScrollView sv;
    Button changeWriting;
    File root;
    File dir;
    File file;
  
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_manager);
		changeWriting = (Button) findViewById(R.id.strButton);
		root = android.os.Environment.getExternalStorageDirectory();
		dir = new File(root.getAbsolutePath() + "/gpsTestDaten");
		dir.mkdirs();
		file = new File(dir, "locationManagerData.txt");
		if (file.exists())file.delete();
		
		
		tw = (TextView) findViewById(R.id.Log);
		sv= (ScrollView) findViewById(R.id.ScrollView1);
		
		
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
		
		//tw.append("time="+new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime())+" Latitude="+loc.getLatitude()+" Longitude="+loc.getLongitude());
		sv.fullScroll(View.FOCUS_DOWN);
		
		
		
		
		
		/*btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        
        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
           
            @Override
            public void onClick(View arg0) {       
                // create class object
              gps = new GPSTracker(LocationManagerActivity.this);
 
                // check if GPS enabled    
                if(gps.canGetLocation()){
                     
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                     
                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();   
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
                 
            }
        });
	*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location_manager, menu);
		
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
		
		tw.append("\ntime="+new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime())+" Latitude="+location.getLatitude());//+" Longitude="+location.getLongitude()
		tw.append("\nlat:"+lm.getLastKnownLocation("gps").getLatitude());
		//tw.append("\ntime="+new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
		sv.fullScroll(View.FOCUS_DOWN);
		if (changeWriting.getText().equals("Stop Writing")) {
			try {
				PrintWriter pw=new PrintWriter(new FileWriter (root.getAbsolutePath() + "//gpsTestDaten//locationManagerData.txt",true));
				pw.append ("\n"+new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
				pw.close();
	    	} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	public void changeWriting (View view){
    	if (!changeWriting.getText().equals("Stop Writing")) changeWriting.setText("Stop Writing");
    	else {changeWriting.setText("Start Writing");
    	
    	}
    }
	
	
	private void writeToSDFile(){
		
	}
}
