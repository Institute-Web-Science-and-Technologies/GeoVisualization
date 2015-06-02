package geoviz.communication;

import com.google.android.gms.maps.model.LatLng;

public class TransferToServerObject {
	
	
	public double latitude,longitude;
	public String team;
	
	public TransferToServerObject(double latitude2, double longitude2,
			String team2) {
		latitude=latitude2;
		longitude=longitude2;
		team=team2;
	}
}
