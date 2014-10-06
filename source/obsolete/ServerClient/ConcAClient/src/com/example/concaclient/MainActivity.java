package com.example.concaclient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
	TextView textview;
boolean test= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		
        setContentView(R.layout.activity_main);
        Log.d("test","kommt das mehrmals?");
        textview=(TextView)findViewById(R.id.testStr);
		//new NetworkTest().execute(textview);

        new NetCon().start();
    	

    }
    
    void f(final String str){
    	this.runOnUiThread(new Runnable(){
    		public void run(){
    			TextView t=(TextView)findViewById(R.id.testStr);
    			t.setText(str);
    		}
    	});
    }
    
    class NetCon extends Thread{
    	public void run(){
    		String str="";
    		try {
    			URL url = new URL("http://141.26.94.96:3000/createplayer?name=test");
    			   HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    			  
    			     InputStream in = new BufferedInputStream(urlConnection.getInputStream());
    			     
    					BufferedReader b = new BufferedReader(new InputStreamReader(in));
    					str=b.readLine();
    					Log.d("test",str);
    					urlConnection.disconnect();
    					
    			   }
    			 catch (Exception e){
    				 e.printStackTrace();
    			 }
    		f(str);
    	}
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
