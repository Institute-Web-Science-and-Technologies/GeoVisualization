package com.example.guiprototype;

import org.zeromq.ZMQ;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class Subscriber implements Runnable{
	private final Handler handler;
	
	
	public Subscriber (Handler h){
		this.handler = h;
	}
	@Override
	public void run() {
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
		subscriber.connect("tcp://141.26.71.201:5558");
		subscriber.subscribe("".getBytes());
		while (true) {
			final String msg=new String(subscriber.recv(0));
			Message msgObject = handler.obtainMessage();
			Bundle b = new Bundle();
			b.putString("message", msg);
			msgObject.setData(b);
			handler.handleMessage(msgObject);
			
			}
		}
		
		
	
}
