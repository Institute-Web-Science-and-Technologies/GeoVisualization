package client;

public class startclient {
	public static void main(String[] args) throws Exception {

		new Thread(new Subscriber()).start();
		ReqRepSubscriber.startClient();
	}
}
