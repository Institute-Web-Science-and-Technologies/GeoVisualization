package geoviz.game.flag;

import java.util.LinkedList;
import java.util.List;

import com.example.guiprototype.SwipeScreen;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import geoviz.communication.JeroMQQueue;
import geoviz.communication.TransferObject;
import geoviz.game.Game;

public class FlagGame extends Game {


	private boolean receivedStatus=false;
	private Team teamRed;
	private Team teamBlue;
	private Gson gson = new GsonBuilder().create();

	public Team getTeamRed() {
		return teamRed;
	}
	public Team getTeamBlue() {
		return teamBlue;
	}

	public SwipeScreen getActivity(){
		return swipeScreen;
	}
	@Override
	public void update(TransferObject o) {
		switch (o.msgType){
		case TransferObject.TYPE_COORD:
			if(teamRed.updatePlayers(o.senderName, o.speed, o.pos)==0)
				teamBlue.updatePlayers(o.senderName, o.speed, o.pos);
			break;
		case TransferObject.TYPE_JOIN_GAME:
			String senderID = o.senderID;
			List<String> tR = new LinkedList<String>();
			List<String> tB = new LinkedList<String>();
			List<LatLng> teamRedPos = new LinkedList<LatLng>();
			List<LatLng> teamBluePos = new LinkedList<LatLng>();
			List<Float> teamRedSpeed = new LinkedList<Float>();
			List<Float> teamBlueSpeed = new LinkedList<Float>();
			List<Long> teamRedLMA = new LinkedList<Long>();
			List<Long> teamBlueLMA = new LinkedList<Long>();
			for (Player p : teamRed.players){
				tR.add(p.getName());
				teamRedPos.add(p.getPosition());
				teamRedSpeed.add(p.getSpeed());
				teamRedLMA.add(p.getLastMarkedAt());
			}
			for (Player p : teamBlue.players){
				tB.add(p.getName());
				teamBluePos.add(p.getPosition());
				teamBlueSpeed.add(p.getSpeed());
				teamBlueLMA.add(p.getLastMarkedAt());
			}
			TransferFlagGame tfg = new TransferFlagGame(tR, tB,
					teamRedPos, teamBluePos,
					teamRedSpeed, teamBlueSpeed,
					teamRedLMA, teamBlueLMA,
					teamRed.flag, teamBlue.flag, teamRed.base,
					teamBlue.base);
			String msg = gson.toJson(tfg);
			JeroMQQueue jmqq = JeroMQQueue.getInstance();
			jmqq.sendMsg(TransferObject.TYPE_GAME_STATUS, msg, senderID);
			break;
		case TransferObject.TYPE_GAME_STATUS:
			if (!receivedStatus){
				receivedStatus = true;
				TransferFlagGame tfg2 = gson.fromJson(o.msg, TransferFlagGame.class);
				for (int i = 0; i<tfg2.teamRed.size();i++){
					boolean playerExists = false;
					for (Player player : teamRed.players){
						if (player.getName().contentEquals(tfg2.teamRed.get(i))){
							playerExists = true;
							player.setSpeed(tfg2.teamRedSpeed.get(i));
							player.setLastMarkedAt(tfg2.teamRedLMA.get(i));
							player.setPosition(tfg2.teamRedPos.get(i));
						}
					}
					if (!playerExists){
						teamRed.addPlayer(new Player())
					}
				}
			}
			break;
		default : break;
		}

	}

	@Override
	public void clearScreen() {
		// TODO Auto-generated method stub

	}

}
