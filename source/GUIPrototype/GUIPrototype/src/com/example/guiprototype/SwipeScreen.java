package com.example.guiprototype;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.zeromq.ZMQ;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.adapter.TabsPagerAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SwipeScreen extends FragmentActivity implements
		ActionBar.TabListener {
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	final BlockingQueue<String> bq  = new LinkedBlockingQueue();
	final Gson gson = new GsonBuilder().create();

	final long userID = (long) (Math.random() * Long.MAX_VALUE);
	String userName;
	final static String serverIP = "tcp://heglohitdos.west.uni-koblenz.de";

	private String[] tabs = { "Map", "Chat", "Backpack" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe_screen);
		
		
		// get user name
		Intent intent= getIntent();
		userName=intent.getStringExtra(MainActivity.EXTRA_USER) ;

		// Initialisierung
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		// actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		new JeroMQPoller(this, serverIP).poll();

		// Tabs der Actionbar hinzufügen
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * Sorgt dafür das beim wischen den entsprechden Tab ausgewählt wird
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		new JeroMQPoller(this, serverIP).poll();

		

		class Consumer implements Runnable {
			final ZMQ.Socket requester;
			final ZMQ.Context context;
			final BlockingQueue<String> bq;

			public Consumer(final BlockingQueue<String> bq) {
				this.bq = bq;
				context = ZMQ.context(1);
				requester = context.socket(ZMQ.REQ);
				requester.connect(serverIP + ":5557");
			}

			@Override
			public void run() {
				while (true) {
					try {
						String msg = bq.take();

						if (msg != null) {
							requester.send(msg.getBytes(), 0);

							requester.recv(0);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			public void close() {
				requester.close();
				context.term();
			}

		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				Consumer c = new Consumer(bq);
				c.run();

			}

		}).start();
		
	

	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.swipe_screen, menu);
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

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {

		// ermöglicht wechseln über Tabs
		viewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	public void sendMessage(View view) {
		
		final EditText autotextview = (EditText) findViewById(R.id.fragmentChatMessage);
		final String m = autotextview.getText().toString();

		TransferObject msg = new TransferObject(0, m, Calendar
				.getInstance().getTime(), userID, userName);
		final String json = gson.toJson(msg);
		Log.d(userName, json);
		bq.add(json);
	}

	/**
	 * Senden der der Chatnachrichten über den Server
	 */




}
