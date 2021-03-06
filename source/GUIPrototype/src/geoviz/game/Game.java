package geoviz.game;

import com.example.fragments.GamesScreenFragment;
import com.example.guiprototype.R;
import com.example.guiprototype.SwipeScreen;

import android.app.Activity;
import geoviz.communication.TransferObject;

public abstract class Game {
	
	protected SwipeScreen swipeScreen;
	static Game __instance;
	public String gameID,userID,userName;
	public static final String TYPE_SNAKE = "0";
	public static final String TYPE_FLAG = "1";
	
	public static Game getGame(){
		return __instance;
	}
	
	public static void init(Game g){
		__instance =g;
	}
	
	abstract public void update(TransferObject o);
	abstract public void clearScreen();

}
