package com.example.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.zeromq.ZMQ;

import com.example.guiprototype.MainActivity;
import com.example.guiprototype.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class ChatScreenFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_chat_screen, container, false);
		return rootView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Initialisierung für den Chat
		new Thread(new Runnable(){
			public void run() {
				ZMQ.Context context = ZMQ.context(1);
				ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
				subscriber.connect("tcp://141.26.71.46:5558");
				subscriber.subscribe("".getBytes());
				while (true) {
					final String msg=new String(subscriber.recv(0));
					getActivity().runOnUiThread(new Runnable(){
						public void run(){
							ScrollView sv= (ScrollView) getView().findViewById(R.id.scrollView1);
							TextView scrollTv = (TextView) getView().findViewById(R.id.chatLog);
							scrollTv.append(msg);
							sv.fullScroll(View.FOCUS_DOWN);
						}
					});
					
					}
				}
		}).start();
	}
	
	public void sendMessage(View view){
		send();
	}
	
	/**
	 * Senden der der Chatnachrichten über den Server
	 */
	public void send(){
		EditText editText= (EditText) getView().findViewById(R.id.chatMessage);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String fdate= sdf.format(c.getTime());
		Intent intent= getActivity().getIntent();
		final String msg ="<"+ fdate + ">" +intent.getStringExtra(MainActivity.EXTRA_USER) + ": " + editText.getText().toString() + "\n";
      	new Thread(new Runnable(){
      		
  			@Override
  			public void run() {
  				ZMQ.Context context = ZMQ.context(1);
  				final ZMQ.Socket requester = context.socket(ZMQ.REQ);
  				requester.connect("tcp://141.26.71.46:5557");
  				
  					requester.send(msg.getBytes(),0);
  					
  					requester.recv(0);
  				
  				requester.close();
  				context.term();
  				
  			}
          	
          }).start();
	}
	
	
}
