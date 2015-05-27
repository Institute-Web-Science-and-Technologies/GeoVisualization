package geoviz.game.flag;

import com.example.guiprototype.SwipeScreen;

import geoviz.communication.TransferObject;
import geoviz.game.Game;

public class FlagGame extends Game {



	private Team teamA;



	private Team teamB;

	public Team getTeamA() {
		return teamA;
	}
	public Team getTeamB() {
		return teamB;
	}

	public SwipeScreen getActivity(){
		return swipeScreen;
	}
	@Override
	public void update(TransferObject o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearScreen() {
		// TODO Auto-generated method stub

	}

}
