package com.example.guiprototype;

import android.support.v7.app.ActionBarActivity;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LocationManagerActivity extends ActionBarActivity implements LocationListener {

    Button btnShowLocation;
    LocationManager lm; 
    // GPSTracker class
    boolean enabled;
	String provider;
    Location loc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lm = (LocationManager)getSystemService(LOCATION_SERVICE);
		
		Criteria crit=new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);
		//Toast.makeText(this, "FATAL ERROR, SELFDESTRUCT ACTIVATED, 3...2...1...BOOOM Nah just joking",Toast.LENGTH_LONG).show();
		
		//provider=l.getBestProvider(crit, true);
		provider="gps";
		loc=lm.getLastKnownLocation(provider);
		enabled=lm.isProviderEnabled(provider);
		if (lm.isProviderEnabled("gps")==false) onProviderDisabled ("gps");
		else onProviderEnabled ("gps");
		Toast.makeText(this, "Latitude="+loc.getLatitude()+" Longitude="+loc.getLongitude(), Toast.LENGTH_LONG).show();
		
		
		
		
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
		Toast.makeText(this, "Latitude="+loc.getLatitude()+" Longitude="+loc.getLongitude(), Toast.LENGTH_LONG).show();
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
		lm.requestLocationUpdates(provider, 400, 1, this);
	}
	public void onPause()   {
		super.onPause();
		lm.removeUpdates(this);
	}
}
