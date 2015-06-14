package geoviz.game.flag;

import geoviz.communication.JeroMQQueue;
import geoviz.communication.TransferObject;
import geoviz.game.Functions;

import java.util.Date;

import android.graphics.Color;
import android.location.Location;

import geoviz.flag.fragments.MapScreenFragment;
import com.example.guiprototype.R;
import com.example.guiprototype.SwipeScreen;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class Player {
	private String name;
	private String id;
	private LatLng position;
	private Location location;
	private boolean hasFlag;
	private long lastMarkedAt;
	private float speed;
	//private Circle posMarker;
	private final Team team;
	private long lastScannedAt;
	private Marker posMarker;

	public void setLastScannedAt(long lastScannedAt) {
		this.lastScannedAt = lastScannedAt;
	}

	public Player(Team team, String name){
		this.team=team;
		final SwipeScreen activity = team.getGame().getActivity();
		this.name=name;
		//id="0";
		speed= 0;
		hasFlag = false;
		lastMarkedAt = 0;
		lastScannedAt=0;
		team.getGame().getActivity().runOnUiThread(new Runnable(){

			@Override
			public void run() {

				MapScreenFragment msf = (MapScreenFragment) activity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":1");
				posMarker = msf.initMarker(240, new LatLng(50.4768685,7.7396053), "me");


			}
		});
		//posMarker.setFillColor(team.color);
	}
	
	public Player(Team team, String name, LatLng pos, float speed, long lastMarkedAt, boolean playerInTeam){
		this.team= team;
		final SwipeScreen activity = team.getGame().getActivity();
		this.name = name;
		this.speed = 0;
		this.lastMarkedAt = lastMarkedAt;
		team.getGame().getActivity().runOnUiThread(new Runnable(){
			@Override
			public void run() {
				MapScreenFragment msf = (MapScreenFragment) activity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":1");
				posMarker = msf.initMarker(240, new LatLng(50.4768685,7.7396053), "me");
			}
		});
		//posMarker.setFillColor(team.color);
		updatePlayer(speed,pos,playerInTeam,false);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LatLng getPosition() {
		return position;
	}

	public void setPosition(LatLng position) {
		this.position = position;
	}

	public boolean hasFlag() {
		return hasFlag;
	}

	public void setHasFlag(boolean hasFlag) {
		this.hasFlag = hasFlag;
	}

	public long getLastMarkedAt() {
		return lastMarkedAt;
	}

	public void setLastMarkedAt(long lastMarkedAt) {
		this.lastMarkedAt = lastMarkedAt;
	}

	public float getSpeed() {
		return speed;
	}

	/*public Circle getPosMarker() {
		return posMarker;
	}

	public void setPosMarker(Circle posMarker) {
		this.posMarker = posMarker;
	}*/

	public Team getTeam() {
		return team;
	}

	public boolean isVisible(){
		long time = new Date().getTime();
		return (((time - lastMarkedAt) < Const.markedInms) || speed > Const.maxspeed || (time - lastScannedAt) < Const.scannerDuration);
	}

	public void setSpeed(float speed){
		this.speed=speed;
	}

	public void updatePlayer(float speed, LatLng pos, boolean userInTeam,boolean isUser) {
		position = pos;
		this.speed= speed;
		if(isUser && !team.enemyFlagPickedUp){
			if(Functions.distance(pos, team.getEnemyFlag()) < Const.flagPickupRadius){
				JeroMQQueue jmqq = JeroMQQueue.getInstance();
				if(team.color == Color.BLUE)
					jmqq.sendMsg(TransferObject.TYPE_PICKUP_FLAG, pos, "teamBlue", team.getGame().gameID);
				else
					jmqq.sendMsg(TransferObject.TYPE_PICKUP_FLAG, pos, "teamRed", team.getGame().gameID);
			}
		}
		if (hasFlag){
			team.changeEnemyFlagPosition(pos);
			if (Functions.distance(pos, team.getBase()) < Const.gainPointRadius){
				JeroMQQueue jmqq = JeroMQQueue.getInstance();
				if(team.color == Color.BLUE){
					jmqq.sendToServer(TransferObject.TYPE_DELIVER_FLAG, team.getGame().getTeamRed().getBase(), "teamBlue", team.getGame().gameID);
				}
				else {
					jmqq.sendToServer(TransferObject.TYPE_DELIVER_FLAG, team.getGame().getTeamBlue().getBase(), "teamRed", team.getGame().gameID);
				}	
			}
			}
		if (userInTeam){
			//posMarker.setCenter(pos);
			final LatLng position1 = pos;
			team.getGame().getActivity().runOnUiThread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					posMarker.setPosition(position1);
					posMarker.setVisible(true);
					
				}
				
			});
			
		}
		else if (isVisible()){
			//posMarker.setCenter(pos);
			final LatLng position1 = pos;
			team.getGame().getActivity().runOnUiThread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					posMarker.setPosition(position1);
					posMarker.setVisible(true);
					
				}
				
			});
		}
		else {
			team.getGame().getActivity().runOnUiThread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					posMarker.setVisible(false);
					
				}
				
			});
		}

	}
}