package geoviz.game;

import geoviz.communication.TransferObject;

import com.example.fragments.MapScreenFragment;
import com.example.guiprototype.SwipeScreen;

public class SnakeGame extends Game {

	@Override
	public void update(final TransferObject t) {
		if (t.msgtype==TransferObject.TYPE_COORD){
			final MapScreenFragment msf  =MapScreenFragment.getMSF();
			SwipeScreen.runOnUi(new Runnable(){
				public void run(){
					msf.handlePosition(t.senderName,t.pos);
				}
			});
								
		}

	}

}
