package geoviz.game.flag;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.graphics.Color;

import com.example.fragments.MapScreenFragment;
import com.example.guiprototype.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class Team {
	public List<Player> players;
	private int points;
	private LatLng base = null;
	private LatLng enemyFlag = null;
	private Circle baseCircle;
	private final FlagGame game;
	private Marker flagMarker = null;
	public boolean enemyFlagPickedUp = false;
	public boolean userInTeam;
	int color;

	public Team(int color , FlagGame game){
		points=0;
		this.game=game;
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
	
	public int updatePlayers (String player, float speed, LatLng pos,boolean isUser ){
		for (Player p : players) {
			if (p.getName().contentEquals(player)) {
				p.updatePlayer(speed,pos,userInTeam,isUser);
				return 1;
			}
		}
		return 0;
	}
	
	public LatLng getEnemyFlag(){
		return enemyFlag;
	}
	
	public void setEnemyFlag(LatLng flag){
		this.enemyFlag = flag;
		if (flagMarker == null){
			game.getActivity().runOnUiThread(new Runnable(){

				@Override
				public void run() {
					MapScreenFragment msf = (MapScreenFragment) game.getActivity().getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":1");
					flagMarker = msf.initMarker();
				}
				
			});
			if(color==Color.BLUE){
				flagMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.red_flag));
			} else {
				flagMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.blue_flag));
			}
			
			//flagMarker.setIcon(icon);
		}
		flagMarker.setPosition(flag);
		if (!userInTeam){
			flagMarker.setVisible(false);
		}
		
	}
	
	public LatLng getBase(){
		return base;
	}
	
	
	
	public void setBase(LatLng pos){
		if(base == null){
			this.base=pos;
			game.getActivity().runOnUiThread(new Runnable(){

				@Override
				public void run() {
					MapScreenFragment msf = (MapScreenFragment) game.getActivity().getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":1");
					baseCircle = msf.initCircle();
					baseCircle.setCenter(base);
					if(color == Color.BLUE){
						baseCircle.setFillColor(Color.argb(125, 0, 0, 255));
						}
					else {
						baseCircle.setFillColor(Color.argb(125, 255, 0, 0));
					}
					
				}
				
			});
		}
	}
	public void gainPoint(){
		points++;
		if (points >= Const.maxpoints){
			// send victory msg
		}
	}
	
}
