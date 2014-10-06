package com.example.jeromqandroid;

import java.io.Serializable;


public class TransferObject implements Serializable {
	//public int msgtype;
	//public java.util.Date timestamp;
	public String sender;
	public String msg;
	
	public TransferObject( String sender, String msg){
		this.sender=sender;
		this.msg=msg;
	}
}
