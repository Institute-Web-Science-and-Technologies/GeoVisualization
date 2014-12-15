package com.example.guiprototype;

import geoviz.communication.JeroMQPoller;
import geoviz.communication.JeroMQQueue;
import geoviz.communication.TransferObject;
import geoviz.game.Game;
import geoviz.game.snake.SnakeGame;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.telephony.TelephonyManager;

import com.example.adapter.TabsPagerAdapter;
import com.example.callbacks.GamesScreenFragmentCallbacks;
import com.example.fragments.GamesScreenFragment;
import com.example.fragments.MapScreenFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SwipeScreen extends FragmentActivity implements
		ActionBar.TabListener, GooglePlayServicesClient.ConnectionCallbacks,

		LocationListener, GooglePlayServicesClient.OnConnectionFailedListener ,GamesScreenFragmentCallbacks {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	public LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	private JeroMQPoller poller;
	public List<String> gameIDs = new ArrayList<String>();

	// redundant in swipescreen an dgame
	String userID;
	// redundant in swipescreen an dgame
	String userName;

	public static String getDeviceId(Context context) {
		final String deviceId = ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		if (deviceId != null) {
			return deviceId;
		} else {
			return android.os.Build.SERIAL;
		}
	}

	public String getUserID() {
		return userID;
	}


	public String getUserName() {
		return userName;
	}


	private String[] tabs = { "Chat", "Map", "Games" };
	private Location mCurrentLocation;

	private static FragmentActivity __instance;

	public static FragmentActivity getInstance() {
		return __instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe_screen);
		this.mLocationClient = new LocationClient(this, this, this);
		this.mLocationRequest = LocationRequest.create();
		this.mLocationRequest
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		this.mLocationRequest.setInterval(500);
		this.mLocationRequest.setFastestInterval(250);

		userID = getDeviceId(this);
		// get user name
		Intent intent = getIntent();
		userName = intent.getStringExtra(MainActivity.EXTRA_USER);

		// Initialisierung
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		// actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		__instance = this;

		Game.init(new SnakeGame("0", this));


		poller = new JeroMQPoller(this);
		poller.addSubscription("0");
		poller.addSubscription(userID);
		poller.poll();


		// Tabs der Actionbar hinzuf�gen
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * Sorgt daf�r das beim wischen den entsprechden Tab ausgew�hlt wird
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		// new JeroMQPoller(this, serverIP).poll();

		/*
		 * ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		 * android.R.layout.simple_list_item_1, gameIDs); ListView lv =
		 * (ListView) findViewById(R.id.gameListView); lv.setAdapter(adapter);
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.swipe_screen, menu);
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
	public void onTabSelected(Tab tab, FragmentTransaction ft) {

		// erm�glicht wechseln �ber Tabs
		viewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

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
	 * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle dataBundle) {
		mLocationClient.requestLocationUpdates(this.mLocationRequest, this);
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		this.mCurrentLocation = mLocationClient.getLastLocation();
	}
	
	float MIN_GPS_QUALITY=10;
	boolean gps_quality_reached=false;

	@Override
	public void onLocationChanged(Location location) {
		this.mCurrentLocation = location;
		LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 100);

		MapScreenFragment.getMSF().mMap.animateCamera(cameraUpdate);
		Toast.makeText(this, location.getAccuracy()+"", Toast.LENGTH_SHORT).show();
		/*
		 * TransferObject msg = new TransferObject(1, "", Calendar
		 * .getInstance().getTime(), userID, userName, new LatLng
		 * (location.getLatitude(),
		 * location.getLongitude()),Game.getGame().gameID); final String json =
		 * gson.toJson(msg); JeroMQQueue.getInstance().add(json);
		 */
		
		if(location.getAccuracy()<MIN_GPS_QUALITY)
			gps_quality_reached=true;
		
		if(gps_quality_reached)
		JeroMQQueue.getInstance()
				.sendMsg(
						TransferObject.TYPE_COORD,
						new LatLng(location.getLatitude(), location
								.getLongitude()), "");

	}

	/*
	 * Called by Location Services if the connection to the location client
	 * drops because of an error.
	 */
	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

	public LatLng getOwnLocation() {
		return new LatLng(this.mCurrentLocation.getLatitude(),
				this.mCurrentLocation.getLongitude());
	}

	public void createGame(View view) {
		GamesScreenFragment gsf = (GamesScreenFragment) getSupportFragmentManager()
				.findFragmentByTag("android:switcher:" + R.id.pager + ":2");
		String gameId = Game.TYPE_SNAKE + this.userID;

		if (!gsf.games.contains(gameId)){
		gsf.games.add(gameId);
		}
		connect(gameId);
		gsf.adapter.notifyDataSetChanged();
		JeroMQQueue jmqq = JeroMQQueue.getInstance();
		jmqq.sendMsg(TransferObject.TYPE_CREATE, getOwnLocation(), "");
	}
	
	public void updateGameList(View view){
	JeroMQQueue jmqq = JeroMQQueue.getInstance();
	jmqq.sendMsg(TransferObject.TYPE_GET_GAMELIST, getOwnLocation(), "");
	}

	public void sendMessage(View view) {
		final EditText autotextview = (EditText) findViewById(R.id.fragmentChatMessage);
		final String m = autotextview.getText().toString();

		LatLng location = this.getOwnLocation();
		final JeroMQQueue jmqq = JeroMQQueue.getInstance();
		jmqq.sendMsg(TransferObject.TYPE_MSG, location, m);

		/*
		 * TransferObject msg = new TransferObject(0, m, Calendar
		 * .getInstance().getTime(), userID, userName,
		 * location,Game.getGame().gameID); final String json =
		 * gson.toJson(msg); Log.d(userName, json); final JeroMQQueue jmqq =
		 * JeroMQQueue.getInstance(); jmqq.add(json);
		 */
		/***************************
		 * still needed ?
		 */
		// MapScreenFragment fragment= MapScreenFragment.getMSF();// =
		// (MapScreenFragment)
		// this.getSupportFragmentManager().findFragmentById(R.id.mapScreenFragment);
		// jmqq.add(gson.toJson(fragment.testMSF(userID, m, location)));

		// doesnt work yet
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void connect(String gameID) {
		poller.deleteSubscription(Game.getGame().gameID);
		poller.addSubscription(gameID);
		Game.init(new SnakeGame(gameID,this));
	}

}
