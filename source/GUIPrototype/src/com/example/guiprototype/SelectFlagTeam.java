package com.example.guiprototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SelectFlagTeam extends Activity {
	public static final String EXTRA_TEAM ="ihavenoideawhatthisisfor";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_flag_team);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_flag_team, menu);
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
	public void sBlue(View view){
		Intent intent = getIntent();
		String userName = intent.getStringExtra(MainActivity.EXTRA_USER);
		String gameId = intent.getStringExtra(MainActivity.EXTRA_GAMEID);
		intent = new Intent(this, SwipeScreenFlag.class);
		intent.putExtra(MainActivity.EXTRA_USER, userName);
		intent.putExtra(MainActivity.EXTRA_GAMEID, gameId);
		intent.putExtra(EXTRA_TEAM, "teamBlue");
		startActivity(intent);
		
	}
	public void sRed(View view){
		Intent intent = getIntent();
		String userName = intent.getStringExtra(MainActivity.EXTRA_USER);
		String gameId = intent.getStringExtra(MainActivity.EXTRA_GAMEID);
		intent = new Intent(this, SwipeScreenFlag.class);
		intent.putExtra(MainActivity.EXTRA_USER, userName);
		intent.putExtra(MainActivity.EXTRA_GAMEID, gameId);
		intent.putExtra(EXTRA_TEAM, "teamRed");
		startActivity(intent);
		
	}
}
