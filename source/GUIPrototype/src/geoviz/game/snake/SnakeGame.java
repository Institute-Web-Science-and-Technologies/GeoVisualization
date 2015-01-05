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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SnakeGame extends Game {

	// list of all players participating in this game
	private Map<String, Player> players = new HashMap<String, Player>();
	private Map<String, Integer> highscore = new HashMap();
	private Gson gson = new GsonBuilder().create();
	// list of all chickens
	private List<Chicken> chickens = new LinkedList();
	private boolean receivedStatus = false;

	/**
	 * represents the game this phone takes part in
	 * 
	 * @param gameID
	 *            the id of the game this phone takes part in
	 * @param a
	 *            interface stuff
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

		Player player = players.get(t.senderName);
		// create a new player if no player with the sender's name exists
		if (player == null) {
			player = new Player(t.senderName);
			players.put(t.senderName, player);

		}
		switch (t.msgType) {
		// adds a chciken created by sender
		case TransferObject.TYPE_ADD_CHICKEN:
			chickens.add(new Chicken(t.pos, Const.CHICKEN_COLLISION_RADIUS, 1,
					t.msg));
			break;

		// removes a chicken collected by sender
		case TransferObject.TYPE_KILL_CHICKEN:
			int i =0;
			for (Chicken chicken : chickens) {
				if (chicken.id.equals(t.msg))
					chicken.kill();
					i =chickens.indexOf(chicken);
			}
			chickens.remove(i);
			addToHighscore(t.senderName, 1);
			players.get(t.senderName).changeMaxLength(Const.SNAKE_GROWTH_RATE);
			break;

		// updates sender's snake length to 3 after sender collided
		case TransferObject.TYPE_SNAKE_DIED:
			players.get(t.senderName).snakeDied(t.timeStamp);
			break;
		case TransferObject.TYPE_JOIN_GAME:
			String senderID = t.senderID;
			TransferSnakeGame tsg = new TransferSnakeGame(chickens, players);
			String msg = gson.toJson(tsg);
			JeroMQQueue jmqq = JeroMQQueue.getInstance();
			jmqq.sendMsg(TransferObject.TYPE_GAME_STATUS, msg, senderID);

			break;
		case TransferObject.TYPE_GAME_STATUS: 
			if (!receivedStatus){
				receivedStatus=true;
			TransferSnakeGame tsg2 = gson.fromJson(t.msg,
					TransferSnakeGame.class);
			for (String p : tsg2.players.keySet()) {
				players.put(p, new Player(p, tsg2.players.get(p)));
			}
			for (String c : tsg2.chickens.keySet()) {
				chickens.add(new Chicken(tsg2.chickens.get(c),
						Const.CHICKEN_COLLISION_RADIUS, 1, c));
			}
		}
			break;
		// updates sender's player object with new inofrmation abut sender's
		// physical position
		case TransferObject.TYPE_COORD:
			swipeScreen.runOnUiThread(new Runnable() {
				public void run() {
					// msf.handlePosition(t.senderName,t.pos);
					Player player = players.get(t.senderName);
					player.update(t);
					if (player.getName().equals(userName)) {
						player.checkCollision(t.timeStamp, players, chickens);

					}
					// chickens.add(new Chicken(Functions.randLoc(t.pos,
					// 10), 5, 1));
					// new TransferObject(TransferObject.TYPE_ADD_CHICKEN,
					// "", timeStamp, senderID, senderName, location,
					// gameID);

					if (chickens.size() < 7)
						if (0 == (int) (Math.random() * 10))
							addChicken(Functions.randLoc(t.pos,
									Const.CHICKEN_SPAWN_RADIUS));
				}
			});
			break;

		}

	}

	/**
	 * add points th player's high score
	 * 
	 * @param player
	 * @param point
	 */
	void addToHighscore(String player, int point) {
		if (highscore.containsKey(player))
			highscore.put(player, highscore.get(player) + 1);
		else
			highscore.put(player, 1);
	}

	/**
	 * informs all phones taking part in the current game that chicken was
	 * created and all information to create an equal chicken object
	 * 
	 * @param loc
	 *            location where the chicken will be created
	 */
	public void addChicken(LatLng loc) {
		final JeroMQQueue jmqq = JeroMQQueue.getInstance();
		jmqq.sendMsg(TransferObject.TYPE_ADD_CHICKEN, loc,
				"" + (long) (Math.random() * Long.MAX_VALUE), gameID);
	}

	@Override
	public void clearScreen() {
		for (Chicken c : chickens) {
			c.kill();
		}
		swipeScreen.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				for (String p : players.keySet()) {

					// TODO Auto-generated method stub
					players.get(p).getSnake().remove();
				}

			}

		});

	}

}
