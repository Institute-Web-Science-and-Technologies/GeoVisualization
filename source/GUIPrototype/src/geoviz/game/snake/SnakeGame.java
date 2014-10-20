package geoviz.game.snake;

import geoviz.communication.TransferObject;
import geoviz.game.Game;

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
			SwipeScreen.getInstance().runOnUiThread(new Runnable(){
				public void run(){
					//msf.handlePosition(t.senderName,t.pos);
					players.get(t.senderName).update(t);
				}
			});
								
		}
		

	}

}
