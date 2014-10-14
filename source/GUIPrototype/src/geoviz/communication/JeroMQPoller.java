package geoviz.communication;

import java.text.SimpleDateFormat;

import org.zeromq.ZMQ;

import com.example.fragments.MapScreenFragment;
import com.example.guiprototype.R;
import com.example.guiprototype.TransferObject;
import com.example.guiprototype.R.id;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

//subscribes smartphone to the server and handles incoming communication
public class JeroMQPoller  {
	
	
	final FragmentActivity self;
	final String serverIP;
	
	
	public JeroMQPoller(FragmentActivity self) {
		super();
		this.self = self;
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
					System.out.println(msg);
					final TransferObject t=gson.fromJson(msg, TransferObject.class);
					if (t.msgtype==0) 
						self.runOnUiThread(new Runnable(){
						
						@Override
						public void run() {
							
							SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
							ScrollView sv= (ScrollView) self.findViewById(R.id.fragmentScrollView1);
							TextView scrollTv = (TextView) self.findViewById(R.id.fragmentChatLog);
							scrollTv.append(t.senderName+" "+dateFormat.format(t.timestamp)+" :"+t.msg+"\n");
							sv.fullScroll(View.FOCUS_DOWN);
							
						}
					
					});
					if (t.msgtype==1){
						self.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MapScreenFragment.getMSF().handlePosition(t.senderName,t.pos);
								
							}
							
						});
						
					}
					
				}
				
			}
        	
        }).start();
		
	}

	

}
