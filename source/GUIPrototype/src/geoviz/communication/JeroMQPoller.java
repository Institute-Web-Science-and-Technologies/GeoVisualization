package geoviz.communication;

import geoviz.game.Game;

import java.text.SimpleDateFormat;

import org.zeromq.ZMQ;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.guiprototype.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//subscribes smartphone to the server and handles incoming communication
public class JeroMQPoller  {
	
	
	final FragmentActivity activity;
	final String serverIP;
	
	
	public JeroMQPoller(FragmentActivity self) {
		super();
		this.activity = self;
		this.serverIP = Const.serverIP;
		
	}




	public void poll(){
	
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ZMQ.Context context = ZMQ.context(1);
				ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
				subscriber.connect(serverIP+":5558");
				subscriber.subscribe("".getBytes());
				final Gson gson = new GsonBuilder().create();
				
				while (true) {
					final String msg=new String(subscriber.recv(0));
					final TransferObject t=gson.fromJson(msg, TransferObject.class);
					if (t.msgtype==TransferObject.TYPE_MSG) 
						activity.runOnUiThread(new Runnable(){
						
						@Override
						public void run() {
							
							SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
							ScrollView sv= (ScrollView) activity.findViewById(R.id.fragmentScrollView1);
							TextView scrollTv = (TextView) activity.findViewById(R.id.fragmentChatLog);
							scrollTv.append(t.senderName+" "+dateFormat.format(t.timestamp)+" :"+t.msg+"\n");
							sv.fullScroll(View.FOCUS_DOWN);
							
						}
					
					});
					if (t.msgtype!=TransferObject.TYPE_MSG){
						Game.getGame().update(t);
						
					}
					
				}
				
			}
        	
        }).start();
		
	}

	

}
