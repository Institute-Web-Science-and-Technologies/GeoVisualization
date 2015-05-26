package geoviz.game.flag;

import java.util.LinkedList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class Team {
	public List<Player> players;
	private int points;
	public LatLng base;
	public LatLng flag;
	boolean userInTeam;
	public Team(){
		points=0;
		players= new LinkedList<Player>();
	}
	public void addPlayer(Player player){
		players.add(player);
	}
	public void removePlayer(Player player){
		players.remove(player);
	}
	public int updatePlayers (String player, float speed, LatLng pos ){
		for (Player p : players)
			if (p.name == player){
				p.setSpeed(speed);
				p.setPos(pos);
				if (userInTeam || p.isVisible()){
					//draw player on map
				}
				return 1;
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
