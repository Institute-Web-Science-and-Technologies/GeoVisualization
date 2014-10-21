package geoviz.game.snake;

import geoviz.communication.TransferObject;
import geoviz.game.Game;

import java.util.HashMap;
import java.util.Map;

import com.example.fragments.GamesScreenFragment;
import com.example.fragments.MapScreenFragment;
import com.example.guiprototype.R;
import com.example.guiprototype.SwipeScreen;

public class SnakeGame extends Game {

	private Map<String, Player> players = new HashMap<String, Player>();

	public SnakeGame(String gameID, SwipeScreen a) {
		this.gameID = gameID;
		this.swipeScreen = a;
		if (!swipeScreen.gameIDs.contains(gameID))
			swipeScreen.gameIDs.add(gameID);
	}

	@Override
	public void update(final TransferObject t) {
		if (t.gameID.equals(this.gameID)) {
			Player player = players.get(t.senderName);
			if (player == null) {
				player = new Player(t.senderName);
				players.put(t.senderName, player);
			}
			if (t.msgType == TransferObject.TYPE_COORD) {
				swipeScreen.runOnUiThread(new Runnable() {
					public void run() {
						// msf.handlePosition(t.senderName,t.pos);
						players.get(t.senderName).update(t);
					}
				});

			}
		}
		else {
			GamesScreenFragment gsf = (GamesScreenFragment) swipeScreen.getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":2");
			if(!gsf.games.contains(t.gameID))
				gsf.games.add(t.gameID);
		}

	}

}
