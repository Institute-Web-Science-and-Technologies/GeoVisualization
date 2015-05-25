package geoviz.flag.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guiprototype.R;

public class ChatScreenFragment extends Fragment {

	public String msgs;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_chat_screen, container, false);
		return rootView;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		TextView scrollTv = (TextView) getActivity().findViewById(R.id.fragmentChatLog);
		 msgs=scrollTv.getText().toString();
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		if (msgs!=null){
			TextView scrollTv = (TextView) getActivity().findViewById(R.id.fragmentChatLog);
			scrollTv.append(msgs);
			ScrollView sv= (ScrollView) getActivity().findViewById(R.id.fragmentScrollView1);
			sv.fullScroll(View.FOCUS_DOWN);
		}
			
	}


	

	
}
