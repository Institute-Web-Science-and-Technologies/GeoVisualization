package geoviz.communication;


import java.io.Serializable;
import java.util.Date;

import com.google.android.gms.maps.model.LatLng;


public class TransferObject implements Serializable {
	public final static int TYPE_MSG =0,
			TYPE_COORD =1,
			TYPE_ADD_CHICKEN=2,
			TYPE_KILL_CHICKEN=3,
			TYPE_CREATE=4,
			TYPE_GET_GAMELIST=5,
			TYPE_SNAKE_DIED=6,
			TYPE_JOIN_GAME=7,
			TYPE_GAME_STATUS=8,
			TYPE_SNAKE_WINS = 9,
			TYPE_PLAYER_MARKED = 10,
			TYPE_JOIN_TEAM = 11,
			TYPE_SET_BASE = 12,
			TYPE_SET_FLAG = 14,
			TYPE_PICKUP_FLAG = 15,
			TYPE_FLAGCARRIER_SHOT = 16,
			TYPE_DELIVER_FLAG = 17;


	
	public int msgType;
	public String gameID;
	public String msg;
	public float speed=0f;
	public LatLng pos;
	public java.util.Date timeStamp;
	public String senderID;
	public String senderName;
	public boolean teamRed;

	
	
	
	
	/**
	 * 
	 * @param msgtype int 0 is chat message, 1 is gps update
	 * @param msg String message
	 * @param timestamp java.util.Date
	 * @param senderID double
	 * @param senderName String
	 */
	public TransferObject(int msgType, String msg, Date timeStamp,
			String senderID, String senderName, LatLng location, String gameID) {
		super();
		this.msgType = msgType;
		this.msg = msg;
		this.timeStamp = timeStamp;
		this.senderID = senderID;
		this.senderName = senderName;
		this.pos=location;
		this.gameID=gameID;
		this.teamRed = false;
	}
	
	public TransferObject(int msgType,String msg, Date timeStamp,String senderID,String senderName,LatLng location,float speed, String gameID){
		super();
		this.msgType=msgType;
		this.msg = msg;
		this.timeStamp = timeStamp;
		this.senderID = senderID;
		this.senderName = senderName;
		this.pos=location;
		this.gameID=gameID;
		this.speed=speed;
		this.teamRed = false;
	}
	
	public TransferObject(int msgType,String msg, Date timeStamp,String senderID,String senderName,LatLng location,float speed, String gameID, boolean teamRed){
		super();
		this.msgType=msgType;
		this.msg = msg;
		this.timeStamp = timeStamp;
		this.senderID = senderID;
		this.senderName = senderName;
		this.pos=location;
		this.gameID=gameID;
		this.speed=speed;
		this.teamRed = teamRed;
	}
		
}
