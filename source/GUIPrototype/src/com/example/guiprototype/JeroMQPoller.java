package com.example.guiprototype;

import org.zeromq.ZMQ;

import android.app.Activity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class JeroMQPoller  {
	
	
	final Activity self;
	final String serverIP;


	public JeroMQPoller(Activity self, String serverIP) {
		super();
		this.self = self;
		this.serverIP = serverIP;
	}




	void poll(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ZMQ.Context context = ZMQ.context(1);
				ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
				subscriber.connect(serverIP+":5558");
				subscriber.subscribe("".getBytes());
				while (true) {
					final String msg=new String(subscriber.recv(0));
					System.out.println(msg);
					self.runOnUiThread(new Runnable(){

						@Override
						public void run() {
							/*TextView textview=(TextView)self.findViewById(R.id.textView1);
							textview.setText(textview.getText().toString()+ msg+'\n');*/
							
							ScrollView sv= (ScrollView) self.findViewById(R.id.fragmentScrollView1);
							TextView scrollTv = (TextView) self.findViewById(R.id.fragmentChatLog);
							scrollTv.append(msg);
							sv.fullScroll(View.FOCUS_DOWN);
							
						}
					
					});
				}
				
			}
        	
        }).start();
	}

	

}
