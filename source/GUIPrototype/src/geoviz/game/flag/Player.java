package geoviz.game.flag;

import java.util.Date;

import com.example.fragments.MapScreenFragment;
import com.example.guiprototype.R;
import com.example.guiprototype.SwipeScreen;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

public class Player {
	private String name;
	private String id;
	private LatLng position;
	private boolean hasFlag;
	private long lastMarkedAt;
	private float speed;
	private Circle posMarker;
	private final Team team;

	public Player(Team team, String name){
		this.team=team;
		final SwipeScreen activity = team.getGame().getActivity();
		this.name=name;
		//id="0";
		speed= 0;
		hasFlag = false;
		lastMarkedAt = 0;
		team.getGame().getActivity().runOnUiThread(new Runnable(){



			@Override
			public void run() {

				MapScreenFragment msf = (MapScreenFragment) activity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":1");
				posMarker = msf.initCircle();


			}
		});
	}
	
	public Player(Team team, String name, LatLng pos, float speed, long lastMarkedAt){
		this.team= team;
		final SwipeScreen activity = team.getGame().getActivity();
		this.name = name;
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

	public boolean isHasFlagasFlag() {
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

	public Circle getPosMarker() {
		return posMarker;
	}

	public void setPosMarker(Circle posMarker) {
		this.posMarker = posMarker;
	}

	public Team getTeam() {
		return team;
	}

	public boolean isVisible(){
		return (((new Date().getTime() - lastMarkedAt) < Const.markedInms) || speed > Const.maxspeed);
	}

	public void setSpeed(float speed){
		this.speed=speed;
	}

	public void updatePlayer(float speed, LatLng pos, boolean userInTeam) {
		position = pos;
		this.speed= speed;
		if (userInTeam){
			posMarker.setCenter(pos);
			posMarker.setFillColor(100);
			posMarker.setVisible(true);
		}
		else if (isVisible()){
			posMarker.setCenter(pos);
			posMarker.setFillColor(200);
			posMarker.setVisible(true);
		}
		else {
			posMarker.setVisible(false);
		}

	}
}