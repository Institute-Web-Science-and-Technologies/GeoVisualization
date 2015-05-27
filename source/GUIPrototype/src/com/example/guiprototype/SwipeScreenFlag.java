package com.example.guiprototype;

import geoviz.game.Game;
import geoviz.game.snake.SnakeGame;

import geoviz.flag.adapter.TabsPagerAdapterFlag;

import android.os.Bundle;
import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;



public class SwipeScreenFlag extends SwipeScreen {
	
	
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);	
		viewPager = (ViewPager) findViewById(R.id.pager);
				actionBar = getActionBar();
				mAdapter = new TabsPagerAdapterFlag(getSupportFragmentManager());

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

	public void toogleMark(View view) {
		FrameLayout shoot = (FrameLayout) findViewById(R.id.fragmentFrameLayoutShoot);
		if (shoot.getVisibility()== View.VISIBLE){
			shoot.setVisibility(View.GONE);
		} else{
			shoot.setVisibility(View.VISIBLE);
		}
	}
	
	public void addBluePoint(View view) {
		ProgressBar bluebar = (ProgressBar) findViewById(R.id.fragmentProgressBlue);		
		bluebar.setProgress(bluebar.getProgress()+1);
	}
	
	public void addRedPoint(View view) {
		ProgressBar redbar =(ProgressBar) findViewById(R.id.fragmentProgressRed);
		redbar.setProgress(redbar.getProgress()+1);
	}
	
	public void resetPoints(View view) {
		ProgressBar bluebar = (ProgressBar) findViewById(R.id.fragmentProgressBlue);
		bluebar.setProgress(0);
		
		ProgressBar redbar =(ProgressBar) findViewById(R.id.fragmentProgressRed);
		redbar.setProgress(0);
		
	}
}
