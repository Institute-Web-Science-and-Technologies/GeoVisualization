package server;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import getPixelFromPicture.GoogleStaticMapLoader;
import org.zeromq.ZMQ;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public class ReqRepPublisher {
	public final static int TYPE_MSG =0,
			TYPE_COORD =1,
			TYPE_ADD_CHICKEN=2,
			TYPE_KILL_CHICKEN=3,
			TYPE_CREATE=4,
			TYPE_GET_GAMELIST=5,
			TYPE_SET_BASE = 12,
			TYPE_REQUEST_FLAG = 13,
			TYPE_SET_FLAG = 14,
			TYPE_FLAGCARRIER_SHOT = 16,
			TYPE_DELIVER_FLAG = 17;
	private ZMQ.Socket replier;
	private ZMQ.Socket publisher;
	private List<String> games;
	private Map<String, String> gameCreators;
	private Map<String, Date> gameList;
	private Gson gson;
	private GoogleStaticMapLoader gsml;
	public  void startServer() {
		System.out.println("Starting server ..");
		ZMQ.Context context = ZMQ.context(1);
		replier = context.socket(ZMQ.REP);
		publisher = context.socket(ZMQ.PUB);
		replier.bind("tcp://*:5557");
		publisher.bind("tcp://*:5558");
		ZMQ.Poller items = new ZMQ.Poller(2);
		items.register(replier, ZMQ.Poller.POLLIN);
		items.register(publisher, ZMQ.Poller.POLLIN);
		System.out.println("Server started.");
		games = new LinkedList<String>();
		gameCreators = new HashMap<String,String>();
		gameList = new HashMap<String,Date>();
		gson = new GsonBuilder().create();
		gsml = new GoogleStaticMapLoader();
	
		
		while (!Thread.currentThread().isInterrupted()) {
			byte[] message;
			items.poll();
			if (items.pollin(0)) {
				message = replier.recv(0);
				String msg = new String(message);
				System.out.println(msg);
				int msgType = Integer.parseInt(msg.split(",")[0]);
				String gameID = msg.split(",")[1];
				String senderID= msg.split(",")[2];
				msg = msg.substring(msg.indexOf("{"),msg.length());
				
				switch (msgType){
				case TYPE_COORD:
					handleCoord(gameID,msg,msgType);
					break;
				case TYPE_ADD_CHICKEN:
					handleAddChicken(gameID,msg,msgType);
					break;
				case TYPE_KILL_CHICKEN:
					handleKillChicken(gameID,msg,msgType);
					break;
				case TYPE_CREATE:
					handleCreate(gameID,msg,msgType);
					break;
				case TYPE_GET_GAMELIST:
					handleGetGamelist(senderID,msg,msgType);
					break;
				case TYPE_MSG:
					handleMsg(gameID,msg,msgType);
					break;
				case TYPE_SET_BASE:
					handleSetBase(gameID,msg,msgType);
					break;
				case TYPE_REQUEST_FLAG:
					handleRequestFlag(gameID,msg,msgType);
					break;
				case TYPE_FLAGCARRIER_SHOT:
					handleFlagcarrierShot(gameID,msg,msgType);
					break;
				case TYPE_DELIVER_FLAG:
					handleDeliverFlag(gameID,msg,msgType);
					break;
				default:
					send(gameID,msgType,msg);
					break;
				
				}
				
			}
			if (items.pollin(1)) {
				message = publisher.recv(0);
			}
		}
		replier.close();
		publisher.close();
		context.term();
		
	}
	private void handleDeliverFlag(String gameID, String msg, int msgType) {
		send(gameID,msgType,msg);
		TransferToServerObject ttso = gson.fromJson(msg, TransferToServerObject.class);
		double [] latlong = null;
		try {
			 latlong = gsml.createFlag(ttso.latitude, ttso.longitude, 500);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ttso.latitude=latlong[0];
		ttso.longitude=latlong[1];
		String flagMsg = gson.toJson(ttso);
		publisher.sendMore(gameID);
		publisher.sendMore(TYPE_SET_FLAG+"");
		publisher.send(flagMsg);
		
	}
	private void handleFlagcarrierShot(String gameID, String msg, int msgType) {
		send(gameID,msgType,msg);
		TransferToServerObject ttso = gson.fromJson(msg, TransferToServerObject.class);
		double [] latlong = null;
		try {
			 latlong = gsml.createFlag(ttso.latitude, ttso.longitude, 500);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ttso.latitude=latlong[0];
		ttso.longitude=latlong[1];
		String flagMsg = gson.toJson(ttso);
		publisher.sendMore(gameID);
		publisher.sendMore(TYPE_SET_FLAG+"");
		publisher.send(flagMsg);
	}
	private void handleRequestFlag(String gameID, String msg, int msgType) {
		TransferToServerObject ttso = gson.fromJson(msg, TransferToServerObject.class);
		double [] latlong = null;
		try{
			latlong = gsml.createFlag(ttso.latitude, ttso.longitude, 500);
		}catch (IOException e){
			e.printStackTrace();
		}
		ttso.latitude=latlong[0];
		ttso.longitude=latlong[1];
		String reply = gson.toJson(ttso);
		send(gameID,TYPE_SET_FLAG,reply);
		
	}
	private void handleSetBase(String gameID, String msg, int msgType) {
		send(gameID,msgType,msg);
		TransferToServerObject ttso = gson.fromJson(msg, TransferToServerObject.class);
		double [] latlong = null;
		try {
			 latlong = gsml.createFlag(ttso.latitude, ttso.longitude, 500);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ttso.latitude=latlong[0];
		ttso.longitude=latlong[1];
		String flagMsg = gson.toJson(ttso);
		publisher.sendMore(gameID);
		publisher.sendMore(TYPE_SET_FLAG+"");
		publisher.send(flagMsg);
		
		
	}
	private void handleMsg(String gameID, String msg, int msgType) {
		send(gameID,msgType,msg);
	}
	private void handleGetGamelist(String senderID, String msg, int msgType) {
		long time = new Date().getTime();
		for (String game : gameList.keySet()){
			if (time - gameList.get(game).getTime() > 3600000){
				gameList.remove(game);
				gameCreators.remove(game);
				}
		}
		List<String> gameIDs = new LinkedList<String>(gameList.keySet());
 		List<String> games = new LinkedList<String>();
 		for (int i =0; i<gameIDs.size(); i++){
 			games.add(gameIDs.get(i)+";"+gameCreators.get(gameIDs.get(i)));
 		}
		String replie = gson.toJson(games);
		send(senderID,msgType,replie);
	}
	private void handleCreate(String gameID, String msg, int msgType) {
		//games.add(gameID);
		String[] names = gameID.split(";");
		gameList.put(names[0], new Date());
		gameCreators.put(names[0], names[1]);
		replier.send("");
		
	}
	private void handleKillChicken(String gameID, String msg, int msgType) {
		send(gameID,msgType,msg);
	}
	private void handleAddChicken(String gameID, String msg, int msgType) {
		send(gameID,msgType,msg);
	}
	private void handleCoord(String gameID, String msg, int msgType) {
		send(gameID,msgType,msg);
		gameList.put(gameID, new Date());
	}
	public void send(String Filter, int msgType, String msg){
		publisher.sendMore(Filter);
		publisher.sendMore(msgType+"");
		publisher.send(msg);
		replier.send("");
	}
}
