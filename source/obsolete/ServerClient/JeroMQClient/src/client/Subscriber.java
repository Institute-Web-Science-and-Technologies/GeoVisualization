package client;

import org.zeromq.ZMQ;

public class Subscriber implements Runnable {


	@Override
	public void run() {
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
		subscriber.connect("tcp://localhost:5558");
		subscriber.subscribe("".getBytes());
		while (true) {
			System.out.println(new String(subscriber.recv(0)));
		}
	}
}
