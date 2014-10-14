package geoviz.game;

import geoviz.communication.TransferObject;

public abstract class Game {
	
	static Game __instance;
	
	public static Game getGame(){
		return __instance;
	}
	
	public static void init(Game g){
		__instance =g;
	}
	
	abstract public void update(TransferObject o);

}
