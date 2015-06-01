package com.example.guiprototype;

import com.example.fragments.MapScreenFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import geoviz.game.Functions;
import geoviz.game.Game;
import geoviz.game.flag.Const;
import geoviz.game.flag.FlagGame;
import geoviz.game.flag.Player;
import geoviz.game.snake.SnakeGame;

import geoviz.communication.JeroMQQueue;
import geoviz.communication.TransferObject;
import geoviz.flag.adapter.TabsPagerAdapterFlag;

import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SwipeScreenFlag extends SwipeScreen implements SensorEventListener {

	Sensor accelerometer;
	Sensor magnetometer;
	private SensorManager sm;
	float[] grav = new float[9];
	float[] mag = new float[9];
	protected boolean scanIsReady = true;
	protected boolean markIsReady = true;
	float orientationValues[] = new float[3];

	public String team;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapterFlag(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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

		Intent intent = getIntent();
		team = intent.getStringExtra(SelectFlagTeam.EXTRA_TEAM);
		JeroMQQueue jmqq = JeroMQQueue.getInstance();
		jmqq.sendMsg(TransferObject.TYPE_JOIN_TEAM, team,
				intent.getStringExtra(MainActivity.EXTRA_GAMEID));

		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}

	public void toogleMark(View view) {
		FrameLayout shoot = (FrameLayout) findViewById(R.id.fragmentFrameLayoutShoot);
		if (shoot.getVisibility() == View.VISIBLE) {
			shoot.setVisibility(View.GONE);
		} else {
			shoot.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		sm.registerListener(this, accelerometer, sm.SENSOR_DELAY_UI);
		sm.registerListener(this, magnetometer, sm.SENSOR_DELAY_UI);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sm.unregisterListener(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		this.mCurrentLocation = location;
		LatLng latLng = new LatLng(location.getLatitude(),
				location.getLongitude());
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);

		if (location.getAccuracy() < MIN_GPS_QUALITY)
			gps_quality_reached = true;

		if (gps_quality_reached)
			JeroMQQueue.getInstance()
					.sendMsg(
							TransferObject.TYPE_COORD,
							new LatLng(location.getLatitude(), location
									.getLongitude()), location.getSpeed(),
							Game.getGame().gameID);
	}

	public void markPlayer(View view) {

		if (markIsReady == true) {
			float orientationAngle = (float) Math
					.toDegrees(orientationValues[0]);
			if (orientationAngle < 0)
				orientationAngle = 360f - orientationAngle;
			FlagGame flaggame = (FlagGame) Game.getGame();
			if (flaggame.getTeamBlue().userInTeam) {
				for (Player player : flaggame.getTeamRed().players) {
					Location pl = new Location("pl");
					pl.setLatitude(player.getPosition().latitude);
					pl.setLongitude(player.getPosition().longitude);

					if (Functions.distance(this.getOwnLocation(),
							player.getPosition()) <= Const.markerRange
							&& (mCurrentLocation.bearingTo(pl) >= orientationAngle
									- (Const.markerAngleInDegree / 2) && mCurrentLocation
									.bearingTo(pl) <= orientationAngle
									+ (Const.markerAngleInDegree / 2))) {
						JeroMQQueue jmqq = JeroMQQueue.getInstance();
						jmqq.sendMsg(TransferObject.TYPE_PLAYER_MARKED,
								player.getName(), flaggame.gameID);
					}
				}
			} else {
				for (Player player : flaggame.getTeamRed().players) {
					Location pl = new Location("pl");
					pl.setLatitude(player.getPosition().latitude);
					pl.setLongitude(player.getPosition().longitude);

					if (Functions.distance(this.getOwnLocation(),
							player.getPosition()) <= Const.markerRange
							&& (mCurrentLocation.bearingTo(pl) >= orientationAngle
									- (Const.markerAngleInDegree / 2) && mCurrentLocation
									.bearingTo(pl) <= orientationAngle
									+ (Const.markerAngleInDegree / 2))) {
						JeroMQQueue jmqq = JeroMQQueue.getInstance();
						jmqq.sendMsg(TransferObject.TYPE_PLAYER_MARKED,
								player.getName(), flaggame.gameID);
					}
				}

			}
			startMarkCooldown();
		}else{
			//Feedback that mark isn't Ready
		}

	}

	public void scan(View view) {
		if(scanIsReady==true){
		FlagGame flaggame = (FlagGame) Game.getGame();

		if (flaggame.getTeamBlue().userInTeam) {
			for (Player player : flaggame.getTeamRed().players) {
				Location pl = new Location("pl");
				pl.setLatitude(player.getPosition().latitude);
				pl.setLongitude(player.getPosition().longitude);

				if (Functions.distance(this.getOwnLocation(),
						player.getPosition()) <= Const.markerRange) {
					player.getPosMarker().setVisible(true);
				}
			}
		} else {
			for (Player player : flaggame.getTeamRed().players) {
				Location pl = new Location("pl");
				pl.setLatitude(player.getPosition().latitude);
				pl.setLongitude(player.getPosition().longitude);

				if (Functions.distance(this.getOwnLocation(),
						player.getPosition()) <= Const.markerRange) {
					player.getPosMarker().setVisible(true);
				}
			}
			
		}
		startMarkCooldown();
		}else{
			//Feedback that scan isn't ready
		}
	}

	public void addBluePoint(View view) {
		ProgressBar bluebar = (ProgressBar) findViewById(R.id.fragmentProgressBlue);
		bluebar.setProgress(bluebar.getProgress() + 1);
	}

	public void addRedPoint(View view) {
		ProgressBar redbar = (ProgressBar) findViewById(R.id.fragmentProgressRed);
		redbar.setProgress(redbar.getProgress() + 1);
	}

	public void resetPoints(View view) {
		ProgressBar bluebar = (ProgressBar) findViewById(R.id.fragmentProgressBlue);
		bluebar.setProgress(0);

		ProgressBar redbar = (ProgressBar) findViewById(R.id.fragmentProgressRed);
		redbar.setProgress(0);

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// Getting the Orientation
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			grav = event.values;
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			mag = event.values;
		if (grav != null && mag != null) {

			float[] R = new float[9];
			float[] I = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, grav, mag);
			if (success) {
				SensorManager.getOrientation(R, orientationValues);
			}
		}

	}
	public void startMarkCooldown(){
		final TextView mCooldown = (TextView) findViewById(R.id.fragmentMarkCooldown);
		
		new CountDownTimer(30000, 1000) {

		     public void onTick(long millisUntilFinished) {
		    	 mCooldown.setText("seconds remaining: " + millisUntilFinished / 1000);
		     }

		     public void onFinish() {
		    	 mCooldown.setText("Ready");
		    	 markIsReady=true;
		     }
		  }.start();

	}
}
