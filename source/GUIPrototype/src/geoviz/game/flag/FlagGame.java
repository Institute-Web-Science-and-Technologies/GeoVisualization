package geoviz.game.flag;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Color;

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
	
	public FlagGame(String gameID, SwipeScreen swipescreen,String team){
		this.gameID=gameID;
		this.swipeScreen=swipescreen;
		teamRed = new Team(Color.RED);
		teamBlue = new Team(Color.BLUE);
		if(team.contentEquals("teamBlue")){
			teamBlue.userInTeam= true;
		}
		else {
			teamRed.userInTeam = true;
		}
		
	}
	
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
							player.setLastMarkedAt(tfg2.teamRedLMA.get(i));
							player.updatePlayer(tfg2.teamRedSpeed.get(i), tfg2.teamRedPos.get(i), teamRed.userInTeam);

						}
					}
					if (!playerExists){
						teamRed.addPlayer(new Player(teamRed,tfg2.teamRed.get(i),tfg2.teamRedPos.get(i),tfg2.teamRedSpeed.get(i),tfg2.teamRedLMA.get(i),teamRed.userInTeam));
					}
				}
			}
			break;
		case TransferObject.TYPE_PLAYER_MARKED:
			for (Player p:teamBlue.players){
				if (p.getName().contentEquals(o.msg)){
					p.setLastMarkedAt(o.timeStamp.getTime());
				}
			}
			for (Player p:teamRed.players){
				if (p.getName().contentEquals(o.msg)){
					p.setLastMarkedAt(o.timeStamp.getTime());
				}
			}
			break;
		case TransferObject.TYPE_JOIN_TEAM:
			if (o.msg.contentEquals("teamBlue"))
				teamBlue.addPlayer(new Player(teamBlue,o.senderName));
			else 
				teamRed.addPlayer(new Player(teamRed,o.senderName));
			break;
		default : break;
		}

	}

	@Override
	public void clearScreen() {
		// TODO Auto-generated method stub

	}

}
