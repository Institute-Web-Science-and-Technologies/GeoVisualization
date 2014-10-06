package com.example.concaclient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class NetworkTest extends AsyncTask<TextView, Void, String> {

	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String doInBackground(TextView... params) {
/*Socket server = null;
		
		try {
			server=new Socket ("141.26.94.96", 3000);
			Scanner in = new Scanner (server.getInputStream());
			PrintWriter out = new PrintWriter (server.getOutputStream(), true);
			
			out.println("");
			TextView t= params[0];
			t.setText(in.nextLine());
		}
		catch (Exception e) {e.printStackTrace();}
		finally {if (server != null) try {server.close();} catch(Exception e){}}
		return null;*/
		  // URL url = new URL("141.26.94.96:3000");
		 try {
		URL url = new URL("http://141.26.94.96:3000");
		   HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		  
		     InputStream in = new BufferedInputStream(urlConnection.getInputStream());
		     TextView t= params[0];
				BufferedReader b = new BufferedReader(new InputStreamReader(in));
				Log.d("test",b.readLine());
				urlConnection.disconnect();
		   }
		 catch (Exception e){
			 e.printStackTrace();
		 }
		     finally {
		     
		   }
		 return "Test";
		 }
	 protected void onPostExecute(Void result) {
       //  textview.setText("test");
 }      
	
	}

	


