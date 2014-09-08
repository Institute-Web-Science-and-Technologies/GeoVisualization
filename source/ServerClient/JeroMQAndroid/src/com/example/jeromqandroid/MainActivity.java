package com.example.jeromqandroid;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
	final static String serverIP = "tcp://141.26.71.91";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
       new JeroMQPoller(this, serverIP).poll();
        
        
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
        
        final BlockingQueue<String> bq = new LinkedBlockingQueue();
        
        class Consumer implements Runnable  {
        	final ZMQ.Socket requester;
        	final ZMQ.Context context;
        	
        	Consumer(){
        		 context = ZMQ.context(1);;
  				requester = context.socket(ZMQ.REQ);
  				requester.connect(serverIP+":5557");
        	}

			@Override
			public void run() {
				while(true){
					try{
					String msg = bq.take();
					
					if(msg!=null){
          					requester.send(msg.getBytes(),0);
          					
          					requester.recv(0);
          				
          				
					}
					}catch(Exception e){e.printStackTrace();}
				}
			}
			
			public void close(){
				requester.close();
  				context.term();
			}
        	
        }
        
       
        new Thread(new Runnable(){

			@Override
			public void run() {
				Consumer c = new Consumer();
				c.run();
				
			}
        	
        }).start();
        
        
        
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
          				/*ZMQ.Context context = ZMQ.context(1);
          				final ZMQ.Socket requester = context.socket(ZMQ.REQ);
          				requester.connect(serverIP+":5557");
          				
          					requester.send(json.getBytes(),0);
          					
          					requester.recv(0);
          				
          				requester.close();
          				context.term();*/
          				bq.add(json);
          				
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
