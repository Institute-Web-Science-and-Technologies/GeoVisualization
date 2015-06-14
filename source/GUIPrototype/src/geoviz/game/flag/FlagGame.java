package geoviz.game.flag;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;

import geoviz.flag.fragments.MapScreenFragment;
import com.example.guiprototype.R;
import com.example.guiprototype.SwipeScreen;
import com.example.guiprototype.SwipeScreenFlag;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import geoviz.communication.JeroMQQueue;
import geoviz.communication.TransferObject;
import geoviz.game.Game;

public class FlagGame extends Game {

	private boolean receivedStatus = false;
	private Team teamRed;
	private Team teamBlue;
	private Marker flagMarker = null;
	private Gson gson = new GsonBuilder().create();

	public FlagGame(String gameID, SwipeScreen swipescreen, String userName) {
		this.gameID = gameID;
		this.swipeScreen = swipescreen;
		this.userName = userName;
		this.userID = swipeScreen.getUserID();
		teamRed = new Team(Color.RED, this);
		teamBlue = new Team(Color.BLUE, this);

	}

	public void setTeam(String team) {
		if (team.contentEquals("teamBlue")) {
			teamBlue.userInTeam = true;
		} else {
			teamRed.userInTeam = true;
		}
	}

	public Team getTeamRed() {
		return teamRed;
	}

	public Team getTeamBlue() {
		return teamBlue;
	}

	public SwipeScreen getActivity() {
		return swipeScreen;
	}

	@Override
	public void update(TransferObject o) {
		switch (o.msgType) {
		case TransferObject.TYPE_COORD:
			boolean isUser = o.senderName.contentEquals(userName);
			if (o.teamRed)
				teamRed.updatePlayers(o.senderName,o.speed,o.pos, isUser);
			else {
				teamBlue.updatePlayers(o.senderName, o.speed, o.pos, isUser);
			}
			break;
		case TransferObject.TYPE_JOIN_GAME:
			if (!o.senderName.contentEquals(this.userName)) {
				String senderID = o.senderID;
				List<String> tR = new LinkedList<String>();
				List<String> tB = new LinkedList<String>();
				List<LatLng> teamRedPos = new LinkedList<LatLng>();
				List<LatLng> teamBluePos = new LinkedList<LatLng>();
				List<Float> teamRedSpeed = new LinkedList<Float>();
				List<Float> teamBlueSpeed = new LinkedList<Float>();
				List<Long> teamRedLMA = new LinkedList<Long>();
				List<Long> teamBlueLMA = new LinkedList<Long>();
				for (Player p : teamRed.players) {
					tR.add(p.getName());
					teamRedPos.add(p.getPosition());
					teamRedSpeed.add(p.getSpeed());
					teamRedLMA.add(p.getLastMarkedAt());
				}
				for (Player p : teamBlue.players) {
					tB.add(p.getName());
					teamBluePos.add(p.getPosition());
					teamBlueSpeed.add(p.getSpeed());
					teamBlueLMA.add(p.getLastMarkedAt());
				}
				TransferFlagGame tfg = new TransferFlagGame(tR, tB, teamRedPos,
						teamBluePos, teamRedSpeed, teamBlueSpeed, teamRedLMA,
						teamBlueLMA, teamRed.getEnemyFlag(),
						teamBlue.getEnemyFlag(), teamRed.getBase(),
						teamBlue.getBase(), teamRed.getPoints(),
						teamBlue.getPoints());
				String msg = gson.toJson(tfg);
				JeroMQQueue jmqq = JeroMQQueue.getInstance();
				jmqq.sendMsg(TransferObject.TYPE_GAME_STATUS, msg, senderID);
			}
			break;
		case TransferObject.TYPE_GAME_STATUS:
			if (!o.senderName.contentEquals(this.userName) && !receivedStatus) {
				receivedStatus = true;
				TransferFlagGame tfg2 = gson.fromJson(o.msg,
						TransferFlagGame.class);
				for (int i = 0; i < tfg2.teamRed.size(); i++) {
					boolean playerExists = false;
					for (Player player : teamRed.players) {
						if (player.getName().contentEquals(tfg2.teamRed.get(i))) {
							playerExists = true;
							player.setLastMarkedAt(tfg2.teamRedLMA.get(i));
							player.updatePlayer(tfg2.teamRedSpeed.get(i),
									tfg2.teamRedPos.get(i), teamRed.userInTeam,
									false);

						}
					}
					if (!playerExists) {
						teamRed.addPlayer(new Player(teamRed, tfg2.teamRed
								.get(i), tfg2.teamRedPos.get(i),
								tfg2.teamRedSpeed.get(i), tfg2.teamRedLMA
										.get(i), teamRed.userInTeam));
					}
				}
				for (int i = 0; i < tfg2.teamBlue.size(); i++) {
					boolean playerExists = false;
					for (Player player : teamBlue.players) {
						if (player.getName()
								.contentEquals(tfg2.teamBlue.get(i))) {
							playerExists = true;
							player.setLastMarkedAt(tfg2.teamBlueLMA.get(i));
							player.updatePlayer(tfg2.teamBlueSpeed.get(i),
									tfg2.teamBluePos.get(i),
									teamBlue.userInTeam, false);
						}
					}
					if (!playerExists) {
						teamBlue.addPlayer(new Player(teamBlue, tfg2.teamBlue
								.get(i), tfg2.teamBluePos.get(i),
								tfg2.teamBlueSpeed.get(i), tfg2.teamBlueLMA
										.get(i), teamBlue.userInTeam));
					}
				}
				if (tfg2.teamBlueBase != null)
					teamBlue.setBase(tfg2.teamBlueBase);
				if (tfg2.teamRedBase != null)
					teamRed.setBase(tfg2.teamRedBase);
				// flags in tfg2 are marked by from which team class they came
				// from atm
				if (tfg2.teamBlueFlag != null)
					teamBlue.setNewEnemyFlag(tfg2.teamBlueFlag);
				if (tfg2.teamRedFlag != null)
					teamRed.setNewEnemyFlag(tfg2.teamRedFlag);
				teamBlue.setPoints(tfg2.teamBluePoints);
				teamRed.setPoints(tfg2.teamRedPoints);

			}
			break;
		case TransferObject.TYPE_PLAYER_MARKED:
			for (Player p : teamBlue.players) {
				if (p.getName().contentEquals(o.msg)) {
					p.setLastMarkedAt(o.timeStamp.getTime());
				}
			}
			for (Player p : teamRed.players) {
				if (p.getName().contentEquals(o.msg)) {
					p.setLastMarkedAt(o.timeStamp.getTime());
				}
			}
			break;
		case TransferObject.TYPE_JOIN_TEAM:
			Log.d("null", o.msg);
			if (o.msg.contentEquals("teamBlue"))
				teamBlue.addPlayer(new Player(teamBlue, o.senderName));
			else
				teamRed.addPlayer(new Player(teamRed, o.senderName));
			break;
		case TransferObject.TYPE_SET_FLAG:
			if (o.msg.contentEquals("teamBlue")) {
				teamBlue.setNewEnemyFlag(o.pos);
			} else {
				teamRed.setNewEnemyFlag(o.pos);
			}
			break;
		case TransferObject.TYPE_PICKUP_FLAG:
			List<Player> allPlayers = new LinkedList<Player>(teamBlue.players);
			allPlayers.addAll(teamRed.players);
			for (Player p : allPlayers) {
				if (p.getName().equals(o.senderName)) {
					p.setHasFlag(true);
					p.getTeam().enemyFlagPickedUp = true;
				}

			}
			if (o.msg.contentEquals("teamBLue") && teamRed.userInTeam) {
				Vibrator v = (Vibrator) this.getActivity().getSystemService(
						Context.VIBRATOR_SERVICE);
				v.vibrate(Const.vibrateTimeInMs);
				final FlagGame me = this;

				final LatLng position = o.pos;
				this.getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						MapScreenFragment msf = (MapScreenFragment) me
								.getActivity()
								.getSupportFragmentManager()
								.findFragmentByTag(
										"android:switcher:" + R.id.pager + ":1");
						flagMarker = msf.initMarker(position);
						flagMarker.setIcon(BitmapDescriptorFactory
								.fromResource(R.drawable.red_flag));
					}

				});

				new CountDownTimer(Const.flagVisibilityTimerInMs, 1000) {

					public void onTick(long millisUntilFinished) {

					}

					public void onFinish() {
						flagMarker.remove();
					}
				}.start();
			}
			if (o.msg.contentEquals("teamRed") && teamBlue.userInTeam) {
				Vibrator v = (Vibrator) this.getActivity().getSystemService(
						Context.VIBRATOR_SERVICE);
				v.vibrate(Const.vibrateTimeInMs);
				final FlagGame me = this;
				final LatLng position = o.pos;

				this.getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						MapScreenFragment msf = (MapScreenFragment) me
								.getActivity()
								.getSupportFragmentManager()
								.findFragmentByTag(
										"android:switcher:" + R.id.pager + ":1");
						flagMarker = msf.initMarker(position);
						flagMarker.setIcon(BitmapDescriptorFactory
								.fromResource(R.drawable.blue_flag));
						new CountDownTimer(Const.flagVisibilityTimerInMs, 1000) {

							public void onTick(long millisUntilFinished) {

							}

							public void onFinish() {
								flagMarker.remove();
							}
						}.start();
					}

				});

				
			}
		case TransferObject.TYPE_SET_BASE:
			if (o.msg.contentEquals("teamBlue")) {
				teamBlue.setBase(o.pos);
			} else {
				teamRed.setBase(o.pos);
			}
			break;
		case TransferObject.TYPE_FLAGCARRIER_SHOT:
			if (o.msg.contentEquals("teamBlue")) {
				for (Player p : teamBlue.players) {
					if (p.hasFlag()) {
						p.setHasFlag(false);
						break;
					}
				}
			} else {
				for (Player p : teamRed.players) {
					if (p.hasFlag()) {
						p.setHasFlag(false);
						break;
					}
				}
			}
			break;
		case TransferObject.TYPE_DELIVER_FLAG:
			if (o.msg.contentEquals("teamBlue")) {
				for (Player p : teamBlue.players) {
					if (p.hasFlag()) {
						p.setHasFlag(false);
						p.getTeam().gainPoint();
						SwipeScreenFlag swf = (SwipeScreenFlag) swipeScreen;
						swf.addBluePoint();
						break;
					}
				}
			} else {
				for (Player p : teamRed.players) {
					if (p.hasFlag()) {
						p.setHasFlag(false);
						p.getTeam().gainPoint();
						SwipeScreenFlag swf = (SwipeScreenFlag) swipeScreen;
						swf.addRedPoint();
						break;
					}
				}
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void clearScreen() {
		// TODO Auto-generated method stub

	}

}
