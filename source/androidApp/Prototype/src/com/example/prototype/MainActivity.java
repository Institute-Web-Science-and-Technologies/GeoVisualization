package com.example.prototype;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;


public class MainActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener{
	
	private GoogleMap mMap;
	static final LatLng KOBLENZ = new LatLng(50.3511528, 7.5951959);
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private LatLng position = KOBLENZ;
	private LocationClient mLocationClient;
	private Location mCurrentLocation;
	private ConnectionResult connectionResult;
	private LatLng KOBLENZ4 = new LatLng(50.3604590, 7.59832806);
	private LatLng KOBLENZ3 = new LatLng(50.35614999, 7.589021766);
	private LatLng KOBLENZ2 = new LatLng(50.35582023, 7.60318866);
	private boolean markerClicked = false;
    Polygon polygon;
    PolygonOptions polygonOptions;
	
	public MarkerOptions markop2 = new MarkerOptions()
		.position(KOBLENZ2)
		.title("marker2")
		.draggable(true);
	
	public MarkerOptions markop3 = new MarkerOptions()
		.position(KOBLENZ3)
		.title("marker3")
		.draggable(true);
	
	public MarkerOptions markop4 = new MarkerOptions()
		.position(KOBLENZ4)
		.title("marker 4")
		.draggable(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocationClient = new LocationClient(this, this, this);
       
        mMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);
       
        if (servicesConnected() && mLocationClient.isConnected()) {
        	mCurrentLocation = mLocationClient.getLastLocation();
        	LatLng currentLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            
            mMap.addMarker(new MarkerOptions()
            	.position(currentLocation)
            	.title("you are here")
            );	
        }
        
        
        
        mMap.addMarker(new MarkerOptions()
        .position(KOBLENZ)
    	.title("marker 1")
    	.draggable(true))
    	.setIcon(BitmapDescriptorFactory.fromAsset("red.png"));
        
        
		//Marker 2
    	mMap.addMarker(markop2)
    	.setIcon(BitmapDescriptorFactory.defaultMarker(210));
    	
  	
    	
    	//Marker 3
    	mMap.addMarker(markop3)
    	.setIcon(BitmapDescriptorFactory.defaultMarker(180));
    	
  
    	
    	//Marker 4
    	mMap.addMarker(markop4);
    	
    	mMap.addPolygon(new PolygonOptions()
    	.add(markop2.getPosition(), markop3.getPosition(), markop4.getPosition())
    	.strokeColor(Color.RED));
    	//markerClicked = false;
    	
    	markerClicked = false;
    	mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
		public boolean onMarkerClick(Marker arg0) {
//				arg0.setSnippet("Lat: " + position.latitude  + "\n" + "Lng: " + position.longitude);
				if(markerClicked){
				      
				      if(polygon != null){
				       polygon.remove();
				       polygon = null;
				      }
				      
				      polygonOptions.add(arg0.getPosition());
				      polygonOptions.strokeColor(Color.RED);
				      polygonOptions.fillColor(Color.BLUE);
				      polygon = mMap.addPolygon(polygonOptions);
				     }else{
				      if(polygon != null){
				       polygon.remove();
				       polygon = null;
				      }
				      
				      polygonOptions = new PolygonOptions().add(arg0.getPosition());
				      markerClicked = true;
				     }
				     
				     return true;
				   
		}
		});
    	
    	mMap.setOnMarkerDragListener(new OnMarkerDragListener() {
			
			@Override
			public void onMarkerDragStart(Marker arg0) {
				markerClicked = false;
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMarkerDragEnd(Marker arg0) {
				markerClicked = false;
				position = arg0.getPosition();
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMarkerDrag(Marker arg0) {
				markerClicked = false;
				// TODO Auto-generated method stub
				
			}
		}); 
    	
    	mMap.setOnMapLongClickListener(new OnMapLongClickListener(){

			@Override
			public void onMapLongClick(LatLng arg0) {
				mMap.addMarker(new MarkerOptions().position(arg0).title("neuer Marker").draggable(true));
				
				markerClicked = false;
			}
			
    	});
        
    }
    
    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.connect();
        // Connect the client.
        
    }

    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }
    
    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }
    
    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }
    
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
        }
    }
    
 
    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
		public void show(FragmentManager supportFragmentManager, String tag) {
			// TODO Auto-generated method stub
			
		}
    }
    
    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
           
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
                    
                    break;
                }
           
        }
     }
    
    private boolean servicesConnected() {
    	
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
            
			// Get the error code
            int errorCode = connectionResult.getErrorCode();
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    errorCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getSupportFragmentManager(),
                        "Location Updates");
            }
            return false;
        }
        
    }

}
