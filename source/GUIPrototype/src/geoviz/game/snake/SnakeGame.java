package geoviz.game.snake;

import geoviz.communication.JeroMQPoller;
import geoviz.communication.JeroMQQueue;
import geoviz.communication.TransferObject;
import geoviz.game.Functions;
import geoviz.game.Game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.widget.Toast;

import com.example.fragments.GamesScreenFragment;
import com.example.guiprototype.R;
import com.example.guiprototype.SwipeScreen;

public class SnakeGame extends Game {

	private Map<String, Player> players = new HashMap<String, Player>();
	private List<Chicken> chickens = new LinkedList();

	public SnakeGame(String gameID, SwipeScreen a) {
		this.gameID = gameID;
		this.swipeScreen = a;
		if (!swipeScreen.gameIDs.contains(gameID))
			swipeScreen.gameIDs.add(gameID);
		userName=swipeScreen.getUserName();
		userID=swipeScreen.getUserID();

	}

	@Override
	public void update(final TransferObject t) {
		if (t.gameID.equals(this.gameID)) {
			Player player = players.get(t.senderName);
			if (player == null) {
				player = new Player(t.senderName, players);
				players.put(t.senderName, player);
				
			}
			if(t.msgType==TransferObject.TYPE_ADD_CHICKEN){
				chickens.add(new Chicken(t.pos, 5, 1));
			}
			if (t.msgType == TransferObject.TYPE_COORD) {
				swipeScreen.runOnUiThread(new Runnable() {
					public void run() {
						// msf.handlePosition(t.senderName,t.pos);
						Player player =players.get(t.senderName);
						player.update(t);
						for (Chicken chicken: chickens){
							if(!chicken.dead&&player.collides(chicken)){
								chicken.kill();
								Toast.makeText(SwipeScreen.getInstance(), "chicken killed",
										Toast.LENGTH_SHORT).show();
							}
						}
						//chickens.add(new Chicken(Functions.randLoc(t.pos, 10), 5, 1));
						//new TransferObject(TransferObject.TYPE_ADD_CHICKEN, "", timeStamp, senderID, senderName, location, gameID);
					final JeroMQQueue jmqq = JeroMQQueue.getInstance();
					jmqq.sendMsg(TransferObject.TYPE_ADD_CHICKEN, Functions.randLoc(t.pos, 10), "");
					}
				});

			}
		} else {
			GamesScreenFragment gsf = (GamesScreenFragment) swipeScreen
					.getSupportFragmentManager().findFragmentByTag(
							"android:switcher:" + R.id.pager + ":2");
			if (!gsf.games.contains(t.gameID))
				gsf.games.add(t.gameID);
		}

	}

	
	

}
