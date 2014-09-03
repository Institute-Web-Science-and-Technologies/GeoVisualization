package com.example.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.zeromq.ZMQ;

import com.example.guiprototype.MainActivity;
import com.example.guiprototype.MapScreen;
import com.example.guiprototype.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class MapScreenFragment extends Fragment {

	public static FragmentManager fragmentManager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_map_screen, container, false);

		return rootView;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentManager = getActivity().getSupportFragmentManager();
	}
	
	protected void initFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.map, fragment);
        fragmentTransaction.commit();
    }
	
	
	
}
