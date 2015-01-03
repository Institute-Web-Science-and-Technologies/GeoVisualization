package geoviz.game.ants;

import geoviz.communication.TransferObject;
import geoviz.game.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AntGame extends Game {
	
	List<FoodSource> foodsources = new ArrayList();
	List<Home> homes = new ArrayList();
	Map<String,Ant> ants = new HashMap();
	

	@Override
	public void update(TransferObject o) {
		// TODO Auto-generated method stub

	}


	@Override
	public void clearScreen() {
		// TODO Auto-generated method stub
		
	}

}
