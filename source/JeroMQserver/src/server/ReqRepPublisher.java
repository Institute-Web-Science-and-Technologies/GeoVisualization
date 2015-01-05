package server;

import java.util.LinkedList;
import java.util.List;

import org.zeromq.ZMQ;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public class ReqRepPublisher {
	public final static int TYPE_MSG =0,
			TYPE_COORD =1,
			TYPE_ADD_CHICKEN=2,
			TYPE_KILL_CHICKEN=3,
			TYPE_CREATE=4,
			TYPE_GET_GAMELIST=5;
	private ZMQ.Socket replier;
	private ZMQ.Socket publisher;
	private List<String> games;
	private Gson gson;
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
		gson = new GsonBuilder().create();
	
		
		while (!Thread.currentThread().isInterrupted()) {
			byte[] message;
			items.poll();
			if (items.pollin(0)) {
				message = replier.recv(0);
				String msg = new String(message);
				System.out.println(msg);
				int msgType = Integer.parseInt(msg.split(",")[0]);
				String receiverID = msg.split(",")[1];
				String senderID= msg.split(",")[2];
				msg = msg.substring(msg.indexOf("{"),msg.length());
				
				switch (msgType){
				case TYPE_COORD:
					handleCoord(receiverID,msg,msgType);
					break;
				case TYPE_ADD_CHICKEN:
					handleAddChicken(receiverID,msg,msgType);
					break;
				case TYPE_KILL_CHICKEN:
					handleKillChicken(receiverID,msg,msgType);
					break;
				case TYPE_CREATE:
					handleCreate(receiverID,msg,msgType);
					break;
				case TYPE_GET_GAMELIST:
					handleGetGamelist(receiverID,msg,msgType);
					break;
				case TYPE_MSG:
					handleMsg(receiverID,msg,msgType);
					break;
				default:
					send(receiverID,msgType,msg);
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
	private void handleMsg(String gameID, String msg, int msgType) {
		send(gameID,msgType,msg);
	}
	private void handleGetGamelist(String senderID, String msg, int msgType) {
		
		String replie = gson.toJson(games);
		send(senderID,msgType,replie);
	}
	private void handleCreate(String gameID, String msg, int msgType) {
		games.add(gameID);
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
	}
	public void send(String Filter, int msgType, String msg){
		publisher.sendMore(Filter);
		publisher.sendMore(msgType+"");
		publisher.send(msg);
		replier.send("");
	}
}
