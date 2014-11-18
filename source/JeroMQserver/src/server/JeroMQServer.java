package server;

public class JeroMQServer {
	public static void main(String[] args) throws Exception {
		new ReqRepPublisher().startServer();
	}
}
