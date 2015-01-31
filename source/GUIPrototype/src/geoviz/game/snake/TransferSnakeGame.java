package geoviz.game.snake;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.android.gms.maps.model.LatLng;

public class TransferSnakeGame {
	Map<String,LatLng> chickens;
	Map<String,List<LatLng>> players;
	LatLng centerOfGame;

	
	public TransferSnakeGame(List<Chicken> cs, Map<String,Player> ps,LatLng cOG){
		chickens = new HashMap<String,LatLng>();
		for (Chicken c : cs){
			chickens.put(c.id,c.pos);
		}
		players = new HashMap<String,List<LatLng>>();
		for (String p : ps.keySet()){
			players.put(p, ps.get(p).positions);
		}
		centerOfGame = cOG;
	}
	
	
}
