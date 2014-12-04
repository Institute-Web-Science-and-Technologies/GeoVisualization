import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;
import org.zeromq.*;
import org.zeromq.ZMQ.Poller;

public class Router {
static String[] gameids ={"Id1","Id2"};

private static class SenderTask extends Thread {
	int ID;
	public SenderTask(int ID){
		this.ID=ID;
	}
	
	public void run(){
		Context ctx = ZMQ.context(1);
		Socket socket = ctx.socket(ZMQ.REQ);
		socket.setIdentity(("Sendertask"+ID).getBytes());
		socket.connect("ipc://frontend.ipc");
		socket.sendMore(gameids[ID%2]);
		socket.sendMore(""+ID);
		socket.send("msg"+" gameid: "+gameids[ID%2]+" senderid: "+ID);
		
		socket.close();
		ctx.term();
	}
}
private static class ReceiverTask extends Thread{
	int ID;
	public ReceiverTask(int ID){
		this.ID=ID;
	}
	
	public void run(){
		Context ctx = ZMQ.context(1);
		Socket socket = ctx.socket(ZMQ.REQ);
		socket.setIdentity(("ReceiverTask"+ID).getBytes());
		socket.connect("ipc://backend.ipc");
		socket.send(""+ID);
		while (!Thread.currentThread().isInterrupted()){
			String msg = socket.recvStr();
			System.out.println(ID +" :"+msg);
			socket.send("");
		}
		socket.close();
		ctx.term();
	}
	
	public static void main(String[] args) {
		Context ctx = ZMQ.context(1);
		Socket frontend = ctx.socket(ZMQ.ROUTER);
		Socket backend = ctx.socket(ZMQ.ROUTER);
		frontend.bind("ipc://frontend.ipc");
		backend.bind("ipc://backend.ipc");
		
		for (int i = 0; i<10;i++){
			new ReceiverTask(i).start();
			//new SenderTask(i).start();
		}
		Map<String,List<String>> gamesToClients = new HashMap<String,List<String>>();
		Map<String,String> clientsToReceivers = new HashMap<String,String>();
		Poller items = new Poller(2);
		items.register(backend,Poller.POLLIN);
		items.register(frontend,Poller.POLLIN);
	
		while(!Thread.currentThread().isInterrupted()){
		items.poll();
	
		if (items.pollin(0)){
			String sender =backend.recvStr();
			backend.recvStr();
			String id = backend.recvStr();
			if (!id.equals("")){
				clientsToReceivers.put(id, sender);
				System.out.println("Receiver eingetragen :"+id +" "+sender);
				new SenderTask(Integer.valueOf(id)).start();
				}
		}
		if (items.pollin(1)){
			String sender = frontend.recvStr();
			frontend.recvStr();
			String GameID = frontend.recvStr();
			String ID = frontend.recvStr();
			String msg = frontend.recvStr();
			List<String> IDs;
			if (!gamesToClients.containsKey(GameID)){
				IDs = new LinkedList<String>();
				IDs.add(ID);
				gamesToClients.put(GameID, IDs);
			}
			else {
				IDs= gamesToClients.get(GameID);
				if (!IDs.contains(ID))
					IDs.add(ID);
			}
			for (String str : IDs){
				if (clientsToReceivers.containsKey(str)){
				backend.sendMore(clientsToReceivers.get(str));
				backend.sendMore("");
				backend.send(msg);
				}
				else {
					System.out.println("ERROR receiver: "+str+" noch nicht eingetragen sender: "+ID);
				}
			}
			
			
		}
		
			
		
		}
		 frontend.close();
		 backend.close();
		 ctx.term();
	}
}

}
