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

	
	//list of all players participating in this game 
	private Map<String, Player> players = new HashMap<String, Player>();
	private Map<String, Integer> highscore =new HashMap();
	//list of all chickens
	private List<Chicken> chickens = new LinkedList();

	
	/**
	 * represents the game this phone takes part in
	 * @param gameID the id of the game this phone takes part in
	 * @param a interface stuff
	 */
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
			//create a new player if no player with the sender's name exists
			if (player == null) {
				player = new Player(t.senderName);
				players.put(t.senderName, player);

			}
			switch(t.msgType){
			//adds a chciken created by sender 
			case TransferObject.TYPE_ADD_CHICKEN:
				chickens.add(new Chicken(t.pos, Const.CHICKEN_COLLISION_RADIUS, 1, t.msg));
			break;
			
			//removes a chicken collected by sender
			case TransferObject.TYPE_KILL_CHICKEN:
				for (Chicken chicken : chickens) {
					if (chicken.id.equals(t.msg))
						chicken.kill();
				}
				addToHighscore(t.senderName, 1);
				players.get(t.senderName).changeMaxLength(Const.SNAKE_GROWTH_RATE);
			break;
			
			//updates sender's player object with new inofrmation abut sender's physical position
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
						if(chickens.size()<7)
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
	
	/**
	 * add points th player's high score
	 * @param player
	 * @param point
	 */
	void addToHighscore(String player, int point){
		if(highscore.containsKey(player))
			highscore.put(player, highscore.get(player)+1);
		else
			highscore.put(player, 1);
	}

	/**
	 * informs all phones taking part in the current game that chicken was created
	 * and all information to create an equal chicken object
	 * @param loc location where the chicken will be created
	 */
	public void addChicken(LatLng loc) {
		final JeroMQQueue jmqq = JeroMQQueue.getInstance();
		jmqq.sendMsg(TransferObject.TYPE_ADD_CHICKEN, loc,
				"" + (long) (Math.random() * Long.MAX_VALUE));
	}

}
