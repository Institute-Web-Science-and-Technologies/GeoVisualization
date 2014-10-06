package client;
import org.zeromq.ZMQ;
public class hwclient {
public static void startClient() {
	ZMQ.Context context = ZMQ.context(1);
	
	System.out.println("Connecting to hello world server...");
	
	ZMQ.Socket requester = context.socket(ZMQ.REQ);
	requester.connect("tcp://localhost:5555");
	
	for (int requestNbr = 0; requestNbr !=10; requestNbr++){
		String request="Hello";
		System.out.println("Sending Hello "+ requestNbr);
		requester.send(request.getBytes(),0);
		
		byte[] reply = requester.recv(0);
		System.out.println("Received "+ new String(reply) + " "+ requestNbr);
	}
	requester.close();
	context.term();
}
}
