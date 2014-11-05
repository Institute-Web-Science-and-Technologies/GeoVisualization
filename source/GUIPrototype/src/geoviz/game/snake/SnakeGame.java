package geoviz.game.snake;

import geoviz.communication.TransferObject;
import geoviz.game.Game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.fragments.GamesScreenFragment;
import com.example.guiprototype.R;
import com.example.guiprototype.SwipeScreen;
import com.google.android.gms.maps.model.LatLng;

public class SnakeGame extends Game {

	private Map<String, Player> players = new HashMap<String, Player>();
	private List<Chicken> chickens = new LinkedList();

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
				player = new Player(t.senderName, players);
				players.put(t.senderName, player);
				
			}
			if (t.msgType == TransferObject.TYPE_COORD) {
				swipeScreen.runOnUiThread(new Runnable() {
					public void run() {
						// msf.handlePosition(t.senderName,t.pos);
						players.get(t.senderName).update(t);
						chickens.add(new Chicken(randLoc(t.pos, 10), 1, 1));
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

	
	//funktioniert nicht!!!!??
	public static LatLng randLoc(LatLng ll, int radius) {
		double x0 = ll.latitude, y0 = ll.longitude;
		Random random = new Random();

		// Convert radius from meters to degrees
		double radiusInDegrees = radius / 111000f;

		double u = random.nextDouble();
		double v = random.nextDouble();
		double w = radiusInDegrees * Math.sqrt(u);
		double t = 2 * Math.PI * v;
		double x = w * Math.cos(t);
		double y = w * Math.sin(t);

		// Adjust the x-coordinate for the shrinking of the east-west distances
		double new_x = x / Math.cos(y0);

		double foundLongitude = new_x + x0;
		double foundLatitude = y + y0;
		LatLng res = new LatLng(foundLatitude, foundLongitude);
		return res;
	}

}
