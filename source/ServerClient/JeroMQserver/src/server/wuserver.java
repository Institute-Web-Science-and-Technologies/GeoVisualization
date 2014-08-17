package server;

import java.util.Random;

import org.zeromq.ZMQ;

public class wuserver {
	public static void startserver() {
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket publisher = context.socket(ZMQ.PUB);
		publisher.bind("tcp://*:5556");
		publisher.bind("ipc://weather");
		Random srandom = new Random(System.currentTimeMillis());
		while (!Thread.currentThread().isInterrupted()) {
			// Get values that will fool the boss
			int zipcode, temperature, relhumidity;
			zipcode = 10000 + srandom.nextInt(10000);
			temperature = srandom.nextInt(215) - 80 + 1;
			relhumidity = srandom.nextInt(50) + 10 + 1;

			// Send message to all subscribers
			String update = String.format("%05d %d %d", zipcode, temperature,
					relhumidity);
			publisher.send(update, 0);
		}

		publisher.close();
		context.term();
	}
}
