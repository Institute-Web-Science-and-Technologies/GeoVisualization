package com.example.communicationprototype;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.widget.TextView;

public class Communication {
	
	Activity ui;
	
	boolean registerClient(String name){
		NetworkConnector nc=new NetworkConnector();;
		nc.url+="createplayer?name="+name;
		nc.start();
		return true;
	}
	
	void loop(){
		Timer t=new Timer();
		t.schedule(new TimerTask(){

			@Override
			public void run() {
				updateState();
				
			}
			
		}, 1000,1000);
	}
	
	void sendMsg(String text){
		NetworkConnector nc=new NetworkConnector();;
		nc.url+="msg?clientid=1&text="+text;
		nc.start();
	}
	
	void updateState(){
		final NetworkConnector nc=new NetworkConnector();;
		nc.url+="update";
		nc.callback = new Runnable(){

			@Override
			public void run() {
				ui.runOnUiThread(new Runnable(){

					@Override
					public void run() {
						TextView txt = (TextView) ui.findViewById(R.id.textView1);
						txt.setText(nc.result);
					}
					
				});
				
			}
			
		};
		nc.start();
	}

}
