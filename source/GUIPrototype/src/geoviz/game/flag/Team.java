package geoviz.game.flag;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.example.fragments.MapScreenFragment;
import com.example.guiprototype.R;
import com.google.android.gms.maps.model.LatLng;

public class Team {
	public List<Player> players;
	private int points;
	public LatLng base;
	public LatLng flag;
	private FlagGame game;
	boolean userInTeam;

	public Team(){
		points=0;
		players= new LinkedList<Player>();
	}
	public FlagGame getGame() {
		return game;
	}
	public void addPlayer(Player player){
		players.add(player);
	}
	public void removePlayer(Player player){
		players.remove(player);
	}
	public int updatePlayers (String player, float speed, LatLng pos ){
		for (Player p : players) {
			if (p.getName().contentEquals(player)) {
				p.updatePlayer(speed,pos,userInTeam);
				return 1;
			}
		}
		return 0;
	}
	public void gainPoint(){
		points++;
		if (points >= Const.maxpoints){
			// send victory msg
		}
	}
	
}
