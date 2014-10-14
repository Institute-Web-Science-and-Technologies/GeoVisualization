package server;

import org.zeromq.ZMQ;

public class ReqRepPublisher {
	public static void startServer() {
		System.out.println("Starting server ..");
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket replier = context.socket(ZMQ.REP);
		ZMQ.Socket publisher = context.socket(ZMQ.PUB);
		replier.bind("tcp://*:5557");
		publisher.bind("tcp://*:5558");
		ZMQ.Poller items = new ZMQ.Poller(2);
		items.register(replier, ZMQ.Poller.POLLIN);
		items.register(publisher, ZMQ.Poller.POLLIN);
		System.out.println("Server started.");
		
		while (!Thread.currentThread().isInterrupted()) {
			byte[] message;
			items.poll();
			if (items.pollin(0)) {
				message = replier.recv(0);
				System.out.println(new String(message));
				replier.send("".getBytes(), 0);
				publisher.send(message);
			}
			if (items.pollin(1)) {
				message = publisher.recv(0);
				System.out.println("Process weather update");
			}
		}
		replier.close();
		publisher.close();
		context.term();
		
	}
}
