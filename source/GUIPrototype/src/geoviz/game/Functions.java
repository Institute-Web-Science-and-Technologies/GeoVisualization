package geoviz.game;

import java.util.List;
import java.util.Random;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class Functions {
	
	public static  float distance(LatLng d, LatLng b) {
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
	
	//funktioniert nicht!!!!??
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

}
