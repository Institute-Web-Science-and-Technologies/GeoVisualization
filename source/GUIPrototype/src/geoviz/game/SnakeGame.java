package geoviz.game;

import geoviz.communication.TransferObject;

import android.app.Activity;

import com.example.fragments.GamesScreenFragment;
import com.example.fragments.MapScreenFragment;
import com.example.guiprototype.R;
import com.example.guiprototype.SwipeScreen;

public class SnakeGame extends Game {
	public SnakeGame(String gameID, SwipeScreen a){
		this.gameID=gameID;
		this.swipeScreen=a;
		if(!swipeScreen.gameIDs.contains(gameID))
			swipeScreen.gameIDs.add(gameID);
	}
	@Override
	public void update(final TransferObject t) {
		if (t.msgType==TransferObject.TYPE_COORD){
			if(t.gameID.equals(this.gameID)){
			//final MapScreenFragment msf  =MapScreenFragment.getMSF();
			final MapScreenFragment msf = (MapScreenFragment) swipeScreen.getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":1");
			SwipeScreen.runOnUi(new Runnable(){
				public void run(){
					msf.handlePosition(t.senderName,t.pos);
				}
			});
					
			
			}
			else {
				GamesScreenFragment gsf = (GamesScreenFragment) swipeScreen.getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":2");
				if(!gsf.games.contains(t.gameID))
					gsf.games.add(t.gameID);
			}
		}

	}

}
