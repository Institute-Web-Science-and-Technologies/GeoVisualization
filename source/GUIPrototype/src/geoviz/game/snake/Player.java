package geoviz.game.snake;

import geoviz.communication.TransferObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.widget.Toast;

import com.example.fragments.MapScreenFragment;
import com.example.guiprototype.SwipeScreen;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

public class Player {

	private String name;

	private Polyline snake;

	List<LatLng> poss = new LinkedList();
	
	Map<String, Player> players;

	public Player(String name,Map<String, Player> players) {
		this.players=players;
		this.name = name;
		SwipeScreen.getInstance().runOnUiThread(new Runnable() {
			public void run() {
				snake = MapScreenFragment.getMSF().initLine();

				snake.setPoints(poss);
			}
		});

	}
	
	void normalize(float max_length){
		while(length()>max_length)
			poss.remove(0);
	}
	
	float length(){
		float length=0;
		for (int i = 0; i<poss.size()-1;i++){
			length+= distance(poss.get(i),poss.get(i+1));
		}
		return length;
	}
	
	public float distance (LatLng d, LatLng b ) 
	{
	    double earthRadius = 3958.75;
	    double latDiff = Math.toRadians(b.latitude-d.latitude);
	    double lngDiff = Math.toRadians(b.longitude-d.longitude);
	    double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
	    Math.cos(Math.toRadians(d.latitude)) * Math.cos(Math.toRadians(b.latitude)) *
	    Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double distance = earthRadius * c;

	    int meterConversion = 1609;

	    return new Float(distance * meterConversion).floatValue();
	}

	void update(TransferObject t) {

		poss.add(t.pos);
		
		normalize(10);

		snake.setPoints(poss);
		
	Toast.makeText(SwipeScreen.getInstance(), length()+"", Toast.LENGTH_SHORT).show();
	
	
	/*	
		for(String key: players.keySet()){
			Player p=players.get(key);
			if(p.name==this.name)
				continue;
			if(collides(this.poss,p.poss)){
				//Toast.makeText(context, text, duration);
				JeroMQQueue.getInstance().add("Helllllllow!");
			}
		}*/
	}

	public String getName() {
		return this.name;
	}

	public Player setName(String name) {
		this.name = name;
		return this;
	}

	public Polyline getSnake() {
		return this.snake;
	}

	private static boolean collides(List<LatLng> list1, List<LatLng> list2) {
		if ((list1.size() <= 1) || (list2.size() <= 1))
			return false;

		// Den letzten und vorletzten Punkt aus liste1 holen
		LatLng list1punkt1 = list1.get(list1.size() - 1);
		LatLng list1punkt2 = list1.get(list1.size() - 2);
		double x1 = list1punkt1.latitude;
		double y1 = list1punkt1.longitude;
		double x2 = list1punkt2.latitude;
		double y2 = list1punkt2.longitude;

		// Mit dem ersten Punkt beginnend immer zwei Punkte aus liste2 holen
		for (int i = 0; i < (list2.size() - 1); i++) {

			LatLng list2punkt1 = list2.get(i);
			LatLng list2punkt2 = list2.get(i + 1);
			double x3 = list2punkt1.latitude;
			double y3 = list2punkt1.longitude;
			double x4 = list2punkt2.latitude;
			double y4 = list2punkt2.longitude;

			// Zaehler
			double zx = (x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2)
					* (x3 * y4 - y3 * x4);
			double zy = (x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2)
					* (x3 * y4 - y3 * x4);

			// Nenner
			double n = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

			// Koordinaten des Schnittpunktes
			double x = zx / n;
			double y = zy / n;

			// Vielleicht ist bei der Division durch n etwas schief gelaufen
			if (Double.isNaN(x) & Double.isNaN(y)) {
				throw new IllegalArgumentException(
						"Schnittpunkt nicht eindeutig.");
			}
			// Test ob der Schnittpunkt auf den angebenen Strecken liegt oder
			// außerhalb.
			if (((x - x1) / (x2 - x1) > 1 || (x - x3) / (x4 - x3) > 1
					|| (y - y1) / (y2 - y1) > 1 || (y - y3) / (y4 - y3) > 1))
				continue;

			return true;

		}
		return false;
	}
}
