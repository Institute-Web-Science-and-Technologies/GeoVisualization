package com.example.communicationprototype;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class NetworkConnector extends Thread {
	String result;
	String url="http://192.168.2.102:3000/";
	boolean done = false;
	Runnable callback;

	public void run() {
		try {
			URL u = new URL(url);
			URLConnection urlConnection = u.openConnection();
			result = "a";
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			result = "b";
			BufferedReader br = new BufferedReader(
					new InputStreamReader(in));
			result = "c";

			result = br.readLine();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		done = true;
		if(callback!=null)
			callback.run();
	}
}
