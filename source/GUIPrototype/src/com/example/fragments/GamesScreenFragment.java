package com.example.fragments;

import java.util.ArrayList;
import java.util.List;

import com.example.callbacks.GamesScreenFragmentCallbacks;
import com.example.guiprototype.R;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class GamesScreenFragment extends Fragment {

	



	public List<String> games;
	
	public ArrayAdapter<String> adapter;
	private GamesScreenFragmentCallbacks callbacks;
	
	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		callbacks = (GamesScreenFragmentCallbacks) activity;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		games = new ArrayList<String>();
	}



	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_games_screen,
				container, false);

	
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, games);
		
		ListView lv =(ListView) rootView.findViewById(R.id.gameListView);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				int i =(int) id;
				callbacks.connect(games.get(i));
				
			}
			
		});
		lv.setAdapter(adapter);
		return rootView;
	}

}
