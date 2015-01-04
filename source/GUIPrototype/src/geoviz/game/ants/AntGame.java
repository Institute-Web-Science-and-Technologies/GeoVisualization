package geoviz.game.ants;

import geoviz.communication.TransferObject;
import geoviz.game.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.guiprototype.SwipeScreen;

public class AntGame extends Game {
	
	List<FoodSource> foodsources = new ArrayList();
	Map<String,Ant> ants = new HashMap();
	List<Team> teams = new ArrayList();
	
	public AntGame(String gameID){
		SwipeScreen s = ((SwipeScreen)SwipeScreen.getInstance());
		userName = s.getUserName();
		userID=s.getUserID();
		this.gameID =gameID;
		
		teams.add(new Team(0,240,"a"));
		teams.add(new Team(1,240,"b"));
	}
	

	@Override
	public void update(TransferObject msg) {
		Ant ant = ants.get(msg.senderName);
		
		if(ant==null){
			ant= new Ant(msg.senderName,teams.get(0));
			ants.put(msg.senderName, ant);
		}
		
		switch(msg.msgType){
		case TransferObject.TYPE_COORD:
			ant.updatePos(msg.pos);
			if(ant.name.equals(userName))
				ant.checkCollision(ants, foodsources, teams);
			
		}
	}

}
