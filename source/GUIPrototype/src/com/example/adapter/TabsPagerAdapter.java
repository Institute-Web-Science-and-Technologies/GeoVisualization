package com.example.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.fragments.BackpackScreenFragment;
import com.example.fragments.ChatScreenFragment;
import com.example.fragments.GamesScreenFragment;
import com.example.fragments.MapScreenFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter{
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int index) {
		switch (index){
		case 0:
			
			return new ChatScreenFragment ();
		case 1:
			return new MapScreenFragment();
			
		case 2:
			return new GamesScreenFragment();
		
		}
		return null;
	}

	@Override
	public int getCount() {
		
		return 3;
	}

}
