package geoviz.game.flag;

import java.util.Date;

import com.google.android.gms.maps.model.LatLng;

public class Player {
	public String name;
	public String id;
	public LatLng position;
	public boolean hasFlag;
	public long lastMarkedAt;
	public float speed;
	
	public boolean isVisible(){
		return (((new Date().getTime() - lastMarkedAt) < Const.markedInms) || speed > Const.maxspeed);
	}
	public void setPos(LatLng pos){
		this.position=pos;
	}
	public void setSpeed(float speed){
		this.speed=speed;
	}
}