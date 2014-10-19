package geoviz.communication;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.zeromq.ZMQ;
//handles outgoing communication via a blocking queue and request reply
public class JeroMQQueue {
	
	private static JeroMQQueue __instance;
	
	final BlockingQueue<String> bq  = new LinkedBlockingQueue();

	
	public static JeroMQQueue getInstance(){
		if(__instance ==null)
			__instance = new JeroMQQueue();
		return __instance;
	}
	
	private JeroMQQueue(){
		class Consumer implements Runnable {
			final ZMQ.Socket requester;
			final ZMQ.Context context;
			final BlockingQueue<String> bq;

			public Consumer(final BlockingQueue<String> bq) {
				this.bq = bq;
				context = ZMQ.context(1);
				requester = context.socket(ZMQ.REQ);
			}

			@Override
			public void run() {
				requester.connect(Const.serverIP + ":5557");
				while (true) {
					try {
						
						String msg = bq.take();

						if (msg != null) {
							requester.send(msg.getBytes(), 0);

							requester.recv(0);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			public void close() {
				requester.close();
				context.term();
			}

		}

		new Thread (new Consumer(bq)).start();
	}
	
	public void add(String msg){
		bq.add(msg);
	}

}
