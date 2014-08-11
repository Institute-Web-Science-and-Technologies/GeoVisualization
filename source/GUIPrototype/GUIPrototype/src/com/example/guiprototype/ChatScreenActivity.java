package com.example.guiprototype;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class ChatScreenActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_screen, menu);
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
		localSend();
	} 
	
	/**
	 * method to test the chatbox 
	 */
	public void localSend(){
		EditText editText= (EditText) findViewById(R.id.chatMessage);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String fdate= sdf.format(c.getTime());
		Intent intent= getIntent();
		String message="<"+ fdate + ">" +intent.getStringExtra(MainActivity.EXTRA_USER) + ": " + editText.getText().toString() + "\n";
		ScrollView sv= (ScrollView) findViewById(R.id.scrollView1);
		TextView scrollTv = (TextView) findViewById(R.id.chatLog);
		scrollTv.append(message);
		sv.fullScroll(View.FOCUS_DOWN);
		editText.setText("");
	}
	
	
	public void netwSend(){
	 //TO DO THE REALSTUFF	
	}
}
