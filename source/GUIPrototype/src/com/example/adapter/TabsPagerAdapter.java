package com.example.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.fragments.BackpackScreenFragment;
import com.example.fragments.ChatScreenFragment;
import com.example.fragments.MapScreenFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter{
	Activity a;
	public TabsPagerAdapter(FragmentManager fm, Activity a) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.a=a;
	}

	@Override
	public Fragment getItem(int index) {
		switch (index){
		case 0:
			
			return new MapScreenFragment (a);
			
		case 1:
			return new ChatScreenFragment();
			
		case 2:
			return new BackpackScreenFragment();
		
		}
		return null;
	}

	@Override
	public int getCount() {
		
		return 3;
	}

}
