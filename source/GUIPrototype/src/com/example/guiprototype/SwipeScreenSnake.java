package com.example.guiprototype;

import geoviz.game.Game;
import geoviz.game.snake.SnakeGame;

import com.example.adapter.TabsPagerAdapter;

import android.os.Bundle;
import android.app.ActionBar;
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
				Game.init(new SnakeGame("0", this));

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
}
