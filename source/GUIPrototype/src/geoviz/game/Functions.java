package geoviz.game;

import java.util.List;
import java.util.Random;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class Functions {


	/**
	 * Calculates distance between two coordinates
	 * @param d point
	 * @param b point
	 * @return distance from b to d
	 */

	public static float distance(LatLng d, LatLng b) {
		Location l1 = new Location("From");
		l1.setLatitude(d.latitude);
		l1.setLongitude(d.longitude);
		Location l2 = new Location("To");
		l2.setLatitude(b.latitude);
		l2.setLongitude(b.longitude);
		return l1.distanceTo(l2);

		/*
		 * double earthRadius = 3958.75; double latDiff =
		 * Math.toRadians(b.latitude-d.latitude); double lngDiff =
		 * Math.toRadians(b.longitude-d.longitude); double a = Math.sin(latDiff
		 * /2) * Math.sin(latDiff /2) + Math.cos(Math.toRadians(d.latitude)) *
		 * Math.cos(Math.toRadians(b.latitude)) * Math.sin(lngDiff /2) *
		 * Math.sin(lngDiff /2); double c = 2 * Math.atan2(Math.sqrt(a),
		 * Math.sqrt(1-a)); double distance = earthRadius * c;
		 * 
		 * int meterConversion = 1609;
		 * 
		 * return new Float(distance * meterConversion).floatValue();
		 */
	}


	
	/**
	 * generates a random location 
	 * @param ll 
	 * @param radius
	 * @return random location in a circle of radius radius around coordinate ll
	 */

	public static LatLng randLoc(LatLng ll, int radius) {
		double y0 = ll.latitude, x0 = ll.longitude;
		Random random = new Random();

		// Convert radius from meters to degrees
		double radiusInDegrees = radius / 111000f;

		double u = random.nextDouble();
		double v = random.nextDouble();
		double w = radiusInDegrees * Math.sqrt(u);
		double t = 2 * Math.PI * v;
		double x = w * Math.cos(t);
		double y = w * Math.sin(t);

		// Adjust the x-coordinate for the shrinking of the east-west distances
		double new_x = x / Math.cos(y0);

		double foundLongitude = new_x + x0;
		double foundLatitude = y + y0;
		LatLng res = new LatLng(foundLatitude, foundLongitude);
		return res;
	}
	

	
	/**
	 * tests if the line betwenn the last two points of list1 
	 * intersects with any line between two points from list2
	 * with indexes i and i+1.
	 * @params lists of coordinates 
	 * @return true iff collision occurred
	 */

	public static boolean collides(List<LatLng> list1, List<LatLng> list2) {
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
	

	/**
	 * tests wether the distance betwenn the first point of list1 
	 * and any of the points of list2 is smaller than radius
	 * @params lists of coordinates 
	 * @return true iff collision occurred
	 */

	public static boolean collides_simple(List<LatLng> list1, List<LatLng> list2, double radius) {
		if(list1.size()<1)
			return false;
				
		LatLng head = list1.get(list1.size()-1);
		for(LatLng l:list2){
			//double dist = Math.pow(head.latitude-l.latitude,2)+Math.pow(head.longitude-l.longitude,2);
			//if(dist<Math.pow(radius, 2))
			if(distance(head,l)<radius)
				return true;
		}
		return false;
	}

}
