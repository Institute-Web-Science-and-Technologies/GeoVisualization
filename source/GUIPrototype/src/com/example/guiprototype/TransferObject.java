package com.example.guiprototype;


import java.io.Serializable;
import java.util.Date;

import com.google.android.gms.maps.model.LatLng;


public class TransferObject implements Serializable {
	public int msgtype;
	public String msg;
	public java.util.Date timestamp;
	public long senderID;
	public String senderName;
	public LatLng pos;
	
	/**
	 * 
	 * @param msgtype int 
	 * @param msg String message
	 * @param timestamp java.util.Date
	 * @param senderID double
	 * @param senderName String
	 */
	public TransferObject(int msgtype, String msg, Date timestamp,
			long senderID, String senderName, LatLng location) {
		super();
		this.msgtype = msgtype;
		this.msg = msg;
		this.timestamp = timestamp;
		this.senderID = senderID;
		this.senderName = senderName;
		this.pos=location;
	}
	
	
		
}
