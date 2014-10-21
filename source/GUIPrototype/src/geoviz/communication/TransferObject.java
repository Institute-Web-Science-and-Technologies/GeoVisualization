package geoviz.communication;


import java.io.Serializable;
import java.util.Date;

import com.google.android.gms.maps.model.LatLng;


public class TransferObject implements Serializable {
	public final static int TYPE_MSG =0,
			TYPE_COORD =1;
	
	public int msgType;
	public String msg;
	public java.util.Date timeStamp;
	public String senderID;
	public String senderName;
	public LatLng pos;
	public String gameID;
	
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
	}
	
	
		
}
