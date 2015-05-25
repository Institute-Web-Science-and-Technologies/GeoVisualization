package geoviz.flag.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.adapter.TabsPagerAdapter;
import com.example.fragments.ChatScreenFragment;
import com.example.fragments.GamesScreenFragment;
import geoviz.flag.fragments.MapScreenFragment;

public class TabsPagerAdapterFlag extends TabsPagerAdapter{
	public TabsPagerAdapterFlag(FragmentManager fm) {
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
