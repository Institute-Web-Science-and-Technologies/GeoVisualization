package com.example.fragments;

import java.util.ArrayList;
import java.util.List;

import com.example.guiprototype.R;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class GamesScreenFragment extends Fragment {

	public List<String> games;
	public ArrayAdapter<String> adapter;


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
		lv.setAdapter(adapter);
		return rootView;
	}

}
