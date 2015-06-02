package com.example.guiprototype;

import geoviz.communication.JeroMQQueue;
import geoviz.communication.TransferObject;
import geoviz.game.Game;
import geoviz.game.snake.SnakeGame;

import com.example.adapter.TabsPagerAdapter;

import android.os.Bundle;
import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.view.ViewPager;

public class SwipeScreenSnake extends SwipeScreen {
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		viewPager = (ViewPager) findViewById(R.id.pager);
				actionBar = getActionBar();
				mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

				viewPager.setAdapter(mAdapter);
				actionBar.setHomeButtonEnabled(false);
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

				// Tabs der Actionbar hinzuf�gen
				for (String tab_name : tabs) {
					actionBar.addTab(actionBar.newTab().setText(tab_name)
							.setTabListener(this));
				}
				/**
				 * Sorgt daf�r das beim wischen den entsprechden Tab ausgew�hlt wird
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


	}
	@Override
	public void connect(String gameID) {
		if (Game.getGame()!=null){
			poller.deleteSubscription(Game.getGame().gameID);
			Game.getGame().clearScreen();
			if (gameID.startsWith("1")){
				Intent intent= new Intent(this,SelectFlagTeam.class);
				intent.putExtra(MainActivity.EXTRA_USER, userName);
				intent.putExtra(MainActivity.EXTRA_GAMEID, gameID);
				startActivity(intent);
			}
		}
		Game.init(new SnakeGame(gameID,this));
		JeroMQQueue jmqq = JeroMQQueue.getInstance();
		jmqq.sendMsg(TransferObject.TYPE_JOIN_GAME,gameID);
		//if (gameID.startsWith("0"))
		
		//else
		//	Game.init(new FlagGame(gameID,this));
		poller.addSubscription(gameID);
		
	}
}
