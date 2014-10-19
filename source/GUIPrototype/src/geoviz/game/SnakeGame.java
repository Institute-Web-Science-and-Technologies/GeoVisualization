package geoviz.game;

import geoviz.communication.TransferObject;

import android.app.Activity;

import com.example.fragments.MapScreenFragment;
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
			final MapScreenFragment msf  =MapScreenFragment.getMSF();
			SwipeScreen.runOnUi(new Runnable(){
				public void run(){
					msf.handlePosition(t.senderName,t.pos);
				}
			});
					
			
			}
			else {
				if(!swipeScreen.gameIDs.contains(t.gameID))
					swipeScreen.gameIDs.add(t.gameID);
			}
		}

	}

}
