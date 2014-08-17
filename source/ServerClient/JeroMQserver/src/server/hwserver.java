package server;

import org.zeromq.ZMQ;

public class hwserver {

	public static void startserver() throws Exception{
		ZMQ.Context context=ZMQ.context(1);
		
		ZMQ.Socket socket = context.socket(ZMQ.REP);
		socket.bind ("tcp://*:5555");
		
		while (!Thread.currentThread().isInterrupted()){
			byte[] request = socket.recv(0);
			System.out.println("received Hello");
			
			Thread.sleep(1000);
			
			String reply = "World";
			socket.send(reply.getBytes(), 0);
		}
		socket.close();
		context.term();
	}
}
