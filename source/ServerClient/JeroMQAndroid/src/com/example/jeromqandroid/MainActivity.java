package com.example.jeromqandroid;

import java.io.Serializable;

import org.zeromq.ZMQ;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends ActionBarActivity {
	
	MainActivity self =this;
	
	final String name="user"+(int)(Math.random()*100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        
        
        new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ZMQ.Context context = ZMQ.context(1);
				ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
				subscriber.connect("tcp://141.26.71.31:5558");
				subscriber.subscribe("".getBytes());
				while (true) {
					final String msg=new String(subscriber.recv(0));
					System.out.println(msg);
					self.runOnUiThread(new Runnable(){

						@Override
						public void run() {
							TextView textview=(TextView)findViewById(R.id.textView1);
							textview.setText(textview.getText().toString()+ msg+'\n');
							
						}
					
					});
				}
				
			}
        	
        }).start();
        
        class Msg implements Serializable{
        	/**
			 * 
			 */
			private static final long serialVersionUID = 4394966648199940712L;
			public String sender, value;

			public Msg(String sender, String value) {
				super();
				this.sender = sender;
				this.value = value;
			}
        	
        	
        }
        
    	Button button=(Button)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	 Gson gson = new GsonBuilder().create();
            	final TextView autotextview=(TextView)findViewById(R.id.autoCompleteTextView1);
            	  final String m = autotextview.getText().toString();
            	  TransferObject msg = new TransferObject(name,m);
            	  final String json = gson.toJson(msg);
            	  Log.d(name, json);
            	new Thread(new Runnable(){

          			@Override
          			public void run() {
          				ZMQ.Context context = ZMQ.context(1);
          				final ZMQ.Socket requester = context.socket(ZMQ.REQ);
          				requester.connect("tcp://141.26.71.31:5557");
          				
          					requester.send(json.getBytes(),0);
          					
          					requester.recv(0);
          				
          				requester.close();
          				context.term();
          				
          			}
                  	
                  }).start();
           
            }});
        
      
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
}
