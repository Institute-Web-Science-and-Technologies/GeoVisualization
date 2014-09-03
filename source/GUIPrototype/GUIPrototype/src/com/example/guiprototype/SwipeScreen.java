package com.example.guiprototype;

import com.example.adapter.TabsPagerAdapter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SwipeScreen extends FragmentActivity implements ActionBar.TabListener {
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	
	private String[] tabs= {"Map", "Chat", "Backpack"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe_screen);
		
		// Initialisierung
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar=getActionBar();
		mAdapter= new TabsPagerAdapter(getSupportFragmentManager());
		
		viewPager.setAdapter(mAdapter);
		//actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//Tabs der Actionbar hinzufügen
		for (String tab_name :tabs){
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
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
		
		//ermöglicht wechseln über Tabs
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
}
