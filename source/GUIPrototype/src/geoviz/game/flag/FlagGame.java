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
		switch (o.msgType){
		case TransferObject.TYPE_COORD:
			if(teamA.updatePlayers(o.senderName, o.speed, o.pos)==0)
				teamB.updatePlayers(o.senderName, o.speed, o.pos);
			break;
		default : break;
		}

	}

	@Override
	public void clearScreen() {
		// TODO Auto-generated method stub

	}

}
