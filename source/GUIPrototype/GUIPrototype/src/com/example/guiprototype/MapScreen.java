package com.example.guiprototype;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.zeromq.ZMQ;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MapScreen extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_screen);
	 	
		new Thread(new Runnable(){
			public void run() {
				ZMQ.Context context = ZMQ.context(1);
				ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
				subscriber.connect("tcp://141.26.71.201:5558");
				subscriber.subscribe("".getBytes());
				while (true) {
					final String msg=new String(subscriber.recv(0));
					MapScreen.this.runOnUiThread(new Runnable(){
						public void run(){
							ScrollView sv= (ScrollView) findViewById(R.id.scrollView1);
							TextView scrollTv = (TextView) findViewById(R.id.chatLog);
							scrollTv.append(msg);
							sv.fullScroll(View.FOCUS_DOWN);
						}
					});
					
					}
				}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void sendMessage(View view){
		send();
	}
	
	/**
	 * Ein und ausblenden des Chatfensters
	 */
	public void showChat(View view){
		LinearLayout layoutChatM= (LinearLayout) findViewById(R.id.linearLayoutChatM);
		LinearLayout layoutChatS= (LinearLayout) findViewById(R.id.linearLayoutChatScreen);
		
		if(layoutChatM.getVisibility()==View.VISIBLE) {
			layoutChatM.setVisibility(View.INVISIBLE); 
		} else layoutChatM.setVisibility(View.VISIBLE);
		
		if(layoutChatS.getVisibility()==View.VISIBLE) {
			layoutChatS.setVisibility(View.INVISIBLE); 
		} else layoutChatS.setVisibility(View.VISIBLE);
	}
	/**
	 * Ein und ausblenden der Items
	 */
	public void showItems (View view){
		
		HorizontalScrollView horizonScrVItm = (HorizontalScrollView) findViewById(R.id.horizontalScrollViewItems);
		
		if(horizonScrVItm.getVisibility()==View.VISIBLE) {
			horizonScrVItm.setVisibility(View.INVISIBLE); 
		} else horizonScrVItm.setVisibility(View.VISIBLE);
		
	}
	/**
	 * Hier ein lokaler test des Textfeldes
	 */
	
	public void send(){
		EditText editText= (EditText) findViewById(R.id.chatMessage);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String fdate= sdf.format(c.getTime());
		Intent intent= getIntent();
		final String msg ="<"+ fdate + ">" +intent.getStringExtra(MainActivity.EXTRA_USER) + ": " + editText.getText().toString() + "\n";
      	new Thread(new Runnable(){
      		
  			@Override
  			public void run() {
  				ZMQ.Context context = ZMQ.context(1);
  				final ZMQ.Socket requester = context.socket(ZMQ.REQ);
  				requester.connect("tcp://141.26.71.201:5557");
  				
  					requester.send(msg.getBytes(),0);
  					
  					requester.recv(0);
  				
  				requester.close();
  				context.term();
  				
  			}
          	
          }).start();
	}
	
	public void activateItem() {
		
	}
}
