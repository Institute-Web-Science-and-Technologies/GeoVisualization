import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Server {

	public static void handleCon(Socket client) throws IOException {

		Scanner in = new Scanner(client.getInputStream());
		PrintWriter out = new PrintWriter(client.getOutputStream(), true);
		String a = in.nextLine();
		String b = in.nextLine();
		out.println(a + b);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket server = new ServerSocket(1);
		while (true) {
			Socket client = null;

			try {
				client = server.accept();
				handleCon(client);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (client != null)
					try {
						client.close();
					} catch (Exception e) {
					}
			}
		}
	}

}
