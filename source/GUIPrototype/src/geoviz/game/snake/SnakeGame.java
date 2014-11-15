package geoviz.game.snake;

import geoviz.communication.JeroMQQueue;
import geoviz.communication.TransferObject;
import geoviz.game.Functions;
import geoviz.game.Game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.example.fragments.GamesScreenFragment;
import com.example.guiprototype.R;
import com.example.guiprototype.SwipeScreen;
import com.google.android.gms.maps.model.LatLng;

public class SnakeGame extends Game {

	private Map<String, Player> players = new HashMap<String, Player>();
	private Map<String, Integer> highscore =new HashMap();
	private List<Chicken> chickens = new LinkedList();

	public SnakeGame(String gameID, SwipeScreen a) {
		this.gameID = gameID;
		this.swipeScreen = a;
		if (!swipeScreen.gameIDs.contains(gameID))
			swipeScreen.gameIDs.add(gameID);
		userName = swipeScreen.getUserName();
		userID = swipeScreen.getUserID();

	}

	@Override
	public void update(final TransferObject t) {
		if (t.gameID.equals(this.gameID)) {
			Player player = players.get(t.senderName);
			if (player == null) {
				player = new Player(t.senderName);
				players.put(t.senderName, player);

			}
			switch(t.msgType){
			case TransferObject.TYPE_ADD_CHICKEN:
				chickens.add(new Chicken(t.pos, Const.CHICKEN_COLLISION_RADIUS, 1, t.msg));
			break;
			
			case TransferObject.TYPE_KILL_CHICKEN:
				for (Chicken chicken : chickens) {
					if (chicken.id.equals(t.msg))
						chicken.kill();
				}
				addToHighscore(t.senderName, 1);
				players.get(t.senderName).changeMaxLength(Const.SNAKE_GROWTH_RATE);
			break;
			
			case TransferObject.TYPE_COORD:
				swipeScreen.runOnUiThread(new Runnable() {
					public void run() {
						// msf.handlePosition(t.senderName,t.pos);
						Player player = players.get(t.senderName);
						player.update(t);
						if (player.getName().equals(userName)){
							player.checkCollision(players, chickens);
							
						}
						// chickens.add(new Chicken(Functions.randLoc(t.pos,
						// 10), 5, 1));
						// new TransferObject(TransferObject.TYPE_ADD_CHICKEN,
						// "", timeStamp, senderID, senderName, location,
						// gameID);
						if(chickens.size()<20)
						if(0==(int)(Math.random()*10))
						addChicken(Functions.randLoc(t.pos, Const.CHICKEN_SPAWN_RADIUS));
					}
				});
				break;
			

			}
		} else {
			GamesScreenFragment gsf = (GamesScreenFragment) swipeScreen
					.getSupportFragmentManager().findFragmentByTag(
							"android:switcher:" + R.id.pager + ":2");
			if (!gsf.games.contains(t.gameID))
				gsf.games.add(t.gameID);
		}

	}
	
	void addToHighscore(String player, int point){
		if(highscore.containsKey(player))
			highscore.put(player, highscore.get(player)+1);
		else
			highscore.put(player, 1);
	}

	public void addChicken(LatLng loc) {
		final JeroMQQueue jmqq = JeroMQQueue.getInstance();
		jmqq.sendMsg(TransferObject.TYPE_ADD_CHICKEN, loc,
				"" + (long) (Math.random() * Long.MAX_VALUE));
	}

}
