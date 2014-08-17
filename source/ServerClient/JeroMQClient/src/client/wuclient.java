package client;
import java.util.StringTokenizer;

import org.zeromq.ZMQ;
public class wuclient {
	
	public static void startClient(String zipcode){
		ZMQ.Context con = ZMQ.context(1);
		
		System.out.println("collecting updates from weather station");
		ZMQ.Socket subscriber = con.socket(ZMQ.SUB);
		subscriber.connect("tcp://localhost:5556");
		
		subscriber.subscribe(zipcode.getBytes());
		
		int update_nbr;
		long total_temp=0;
		for (update_nbr=0; update_nbr < 100; update_nbr++){
			String str = subscriber.recvStr(0).trim();
			StringTokenizer sscanf = new StringTokenizer (str, " ");
			int code = Integer.valueOf(sscanf.nextToken());
			int temp = Integer.valueOf(sscanf.nextToken());
			int relhum = Integer.valueOf(sscanf.nextToken());
			System.out.println(code+" "+temp);
			
			total_temp+=temp;
		}
		System.out.println("average temperature for zipcode '"+zipcode+"' was "+(int)(total_temp/update_nbr));
		subscriber.close();
		con.term();
		
	}

}
