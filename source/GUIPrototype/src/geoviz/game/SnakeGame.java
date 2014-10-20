package geoviz.game;

import geoviz.communication.TransferObject;

import java.util.HashMap;
import java.util.Map;

import com.example.fragments.MapScreenFragment;
import com.example.guiprototype.SwipeScreen;

public class SnakeGame extends Game {

	private Map<String, Player> players = new HashMap<String, Player>();
	
	@Override
	public void update(final TransferObject t) {
		Player player = players.get(t.senderName);
		if (player == null) {
			player = new Player(t.senderName);
			players.put(t.senderName, player);
		}
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
