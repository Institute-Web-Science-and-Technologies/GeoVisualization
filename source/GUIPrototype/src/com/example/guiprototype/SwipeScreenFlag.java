package com.example.guiprototype;

import java.util.Date;

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
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
	FlagGame currentGame;

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
		currentGame = ((FlagGame) Game.getGame());
		currentGame.setTeam(team);
		/*
		 * JeroMQQueue jmqq = JeroMQQueue.getInstance();
		 * jmqq.sendMsg(TransferObject.TYPE_JOIN_TEAM, team,
		 * intent.getStringExtra(MainActivity.EXTRA_GAMEID));
		 */
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

		if (location.getAccuracy() < MIN_GPS_QUALITY) {
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
			MapScreenFragment.getMSF().mMap.animateCamera(cameraUpdate);
			gps_quality_reached = true;
		}

		if (gps_quality_reached)
			JeroMQQueue.getInstance()
					.sendMsg(
							TransferObject.TYPE_COORD,
							new LatLng(location.getLatitude(), location
									.getLongitude()), location.getSpeed(),
							Game.getGame().gameID,
							currentGame.getTeamRed().userInTeam);
	}

	public void markPlayer(View view) {

		if (markIsReady == true) {
			Log.d("marker", "marker used");
			float orientationAngle = (float) Math
					.toDegrees(orientationValues[0]);
			GeomagneticField geoField = new GeomagneticField(
					(float) mCurrentLocation.getLatitude(),
					(float) mCurrentLocation.getLongitude(),
					(float) mCurrentLocation.getAltitude(),
					System.currentTimeMillis());
			orientationAngle += geoField.getDeclination();
			Log.d("marker", "orientationangle = " + orientationAngle);
			FlagGame flaggame = (FlagGame) Game.getGame();
			if (flaggame.getTeamBlue().userInTeam) {
				for (Player player : flaggame.getTeamRed().players) {
					Location pl = new Location("pl");
					if (player.getPosition() != null) {
						pl.setLatitude(player.getPosition().latitude);
						pl.setLongitude(player.getPosition().longitude);
						Log.d("marker",
								"player lat: " + mCurrentLocation.getLatitude()
										+ " long: "
										+ mCurrentLocation.getLongitude());
						Log.d("marker", "redplayer at lat: " + pl.getLatitude()
								+ " long: " + pl.getLongitude());
						Log.d("marker",
								"redplayer distance: "
										+ Functions.distance(
												this.getOwnLocation(),
												player.getPosition()));
						Log.d("marker", "redplayer bearingAngle: "
								+ mCurrentLocation.bearingTo(pl));
						if (Functions.distance(this.getOwnLocation(),
								player.getPosition()) <= Const.markerRange
								&& withInAngle(orientationAngle,
										mCurrentLocation.bearingTo(pl),
										Const.markerAngleInDegree,
										Functions.distance(this.getOwnLocation(),
												player.getPosition()))) {
							JeroMQQueue jmqq = JeroMQQueue.getInstance();
							if (!player.hasFlag())
								jmqq.sendMsg(TransferObject.TYPE_PLAYER_MARKED,
										player.getName(), flaggame.gameID);
							else
								jmqq.sendToServer(
										TransferObject.TYPE_FLAGCARRIER_SHOT,
										flaggame.getTeamRed().getBase(),
										"teamRed", flaggame.gameID);
						}
					}
				}
			} else {
				for (Player player : flaggame.getTeamBlue().players) {
					Location pl = new Location("pl");
					if (player.getPosition() != null) {
						pl.setLatitude(player.getPosition().latitude);
						pl.setLongitude(player.getPosition().longitude);
						Log.d("marker",
								"player lat: " + mCurrentLocation.getLatitude()
										+ " long: "
										+ mCurrentLocation.getLongitude());
						Log.d("marker",
								"blueplayer at lat: " + pl.getLatitude()
										+ " long: " + pl.getLongitude());
						Log.d("marker",
								"blueplayer distance: "
										+ Functions.distance(
												this.getOwnLocation(),
												player.getPosition()));
						Log.d("marker", "blueplayer bearingAngle: "
								+ mCurrentLocation.bearingTo(pl));

						if (Functions.distance(this.getOwnLocation(),
								player.getPosition()) <= Const.markerRange
								&& withInAngle(orientationAngle,
										mCurrentLocation.bearingTo(pl),
										Const.markerAngleInDegree,
										Functions.distance(this.getOwnLocation(),
												player.getPosition()))) {
							JeroMQQueue jmqq = JeroMQQueue.getInstance();
							if (!player.hasFlag())
								jmqq.sendMsg(TransferObject.TYPE_PLAYER_MARKED,
										player.getName(), flaggame.gameID);
							else
								jmqq.sendToServer(
										TransferObject.TYPE_FLAGCARRIER_SHOT,
										flaggame.getTeamBlue().getBase(),
										"teamBlue", flaggame.gameID);
						}
					}
				}

			}
			// markIsReady = false;
			// startMarkCooldown();
		} else {
			// Feedback that mark isn't Ready
		}

	}

	public void scan(View view) {
		if (scanIsReady == true) {
			Log.d("scanner", "scanner used");
			FlagGame flaggame = (FlagGame) Game.getGame();

			if (flaggame.getTeamBlue().userInTeam) {
				for (Player player : flaggame.getTeamRed().players) {
					if (player.getPosition() != null) {
						Location pl = new Location("pl");
						pl.setLatitude(player.getPosition().latitude);
						Log.d("scanner", "teamRedPlayer " + player.getName()
								+ " lat: " + pl.getLatitude());
						pl.setLongitude(player.getPosition().longitude);
						Log.d("scanner", "teamRedPlayer " + player.getName()
								+ " long:" + pl.getLongitude());
						Log.d("scanner",
								"teamRedPlayer distance; "
										+ Functions.distance(
												this.getOwnLocation(),
												player.getPosition()));
						if (Functions.distance(this.getOwnLocation(),
								player.getPosition()) <= Const.scannerRange) {
							player.setLastScannedAt(new Date().getTime());
						}
					}
				}
			} else {
				for (Player player : flaggame.getTeamBlue().players) {
					if (player.getPosition() != null) {
						Location pl = new Location("pl");
						pl.setLatitude(player.getPosition().latitude);
						Log.d("scanner", "teamBluePlayer " + player.getName()
								+ " lat: " + pl.getLatitude());
						pl.setLongitude(player.getPosition().longitude);
						Log.d("scanner", "teamBluePlayer " + player.getName()
								+ " long:" + pl.getLongitude());
						Log.d("scanner",
								"teamBluePlayer distance; "
										+ Functions.distance(
												this.getOwnLocation(),
												player.getPosition()));
						if (Functions.distance(this.getOwnLocation(),
								player.getPosition()) <= Const.scannerRange) {
							player.setLastScannedAt(new Date().getTime());
						}
					}
				}
			}
			scanIsReady = false;
			startScanCooldown();
		} else {
			// Feedback that scan isn't ready
		}
	}

	public void addBluePoint() {
		ProgressBar bluebar = (ProgressBar) findViewById(R.id.fragmentProgressBlue);
		bluebar.setProgress(bluebar.getProgress() + Const.pointGain);
	}

	public void addRedPoint() {
		ProgressBar redbar = (ProgressBar) findViewById(R.id.fragmentProgressRed);
		redbar.setProgress(redbar.getProgress() + Const.pointGain);
	}

	public void resetPoints() {
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

	@Override
	public void connect(String gameID) {
		if (Game.getGame() != null) {
			if (poller != null)
				poller.deleteSubscription(Game.getGame().gameID);
			Game.getGame().clearScreen();
			if (gameID.startsWith("0")) {
				Intent intent = new Intent(this, SwipeScreenSnake.class);
				intent.putExtra(MainActivity.EXTRA_USER, userName);
				intent.putExtra(MainActivity.EXTRA_GAMEID, gameID);
				startActivity(intent);
			}

		}
		Game.init(new FlagGame(gameID, this, this.userName));
		JeroMQQueue jmqq = JeroMQQueue.getInstance();
		if (poller != null)
			poller.addSubscription(gameID);
		// jmqq.sendMsg(TransferObject.TYPE_JOIN_TEAM,this.team, gameID);
		jmqq.sendMsg(TransferObject.TYPE_JOIN_GAME, gameID);
		// if (gameID.startsWith("0"))

		// else
		// Game.init(new FlagGame(gameID,this));

	}

	private boolean withInAngle(float orientation, float bearing, int maxdegree, float distance) {
		int degree = maxdegree;
		if (distance < 25)
			degree = 75;
		if (distance < 10)
			degree = 120;
		if (orientation + degree / 2 > bearing
				&& orientation - degree / 2 < bearing)
			return true;
		if (orientation + 360 + degree / 2 > bearing
				&& orientation + 360 - degree / 2 < bearing)
			return true;
		if (orientation + degree / 2 > bearing + 360
				&& orientation - degree / 2 < bearing + 360)
			return true;
		return false;
	}

	public void startMarkCooldown() {
		final TextView mCooldown = (TextView) findViewById(R.id.fragmentMarkCooldown);

		new CountDownTimer(Const.markedCdInMs, 1000) {

			public void onTick(long millisUntilFinished) {
				mCooldown.setText("" + millisUntilFinished / 1000);
			}

			public void onFinish() {
				mCooldown.setText("Ready");
				markIsReady = true;
			}
		}.start();

	}

	public void startScanCooldown() {
		final TextView sCooldown = (TextView) findViewById(R.id.fragmentScanCooldown);

		new CountDownTimer(Const.scanCdinMs, 1000) {

			public void onTick(long millisUntilFinished) {
				sCooldown.setText("" + millisUntilFinished / 1000);
			}

			public void onFinish() {
				sCooldown.setText("Ready");
				scanIsReady = true;
			}
		}.start();

	}

	public void setBase(View view) {
		JeroMQQueue jmqq = JeroMQQueue.getInstance();
		jmqq.sendToServer(TransferObject.TYPE_SET_BASE, this.getOwnLocation(),
				team, Game.getGame().gameID);

	}
}
