package client;
import org.zeromq.ZMQ;
public class ReqRepSubscriber {
	public static void startClient() throws Exception{
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket requester = context.socket(ZMQ.REQ);
		requester.connect("tcp://localhost:5557");
		for (int msgcount=0; msgcount!=20;msgcount++){
			String msg = "message "+msgcount;
			requester.send(msg.getBytes(),0);
			Thread.sleep(1000);
			requester.recv(0);
		}
		requester.close();
		context.term();
	}
}
