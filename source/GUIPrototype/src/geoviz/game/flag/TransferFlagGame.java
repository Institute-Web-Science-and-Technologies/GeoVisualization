package geoviz.game.flag;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class TransferFlagGame {
	List<String> teamRed,teamBlue;
	List<LatLng> teamRedPos,teamBluePos;
	List<Float>  teamRedSpeed,teamBlueSpeed;
	List<Long> teamRedLMA,teamBlueLMA;
	LatLng teamRedFlag,teamBlueFlag,teamRedBase,teamBlueBase;
	
	
	
	public TransferFlagGame(List<String> teamRed, List<String> teamBlue,
			List<LatLng> teamRedPos, List<LatLng> teamBluePos,
			List<Float> teamRedSpeed, List<Float> teamBlueSpeed,
			List<Long> teamRedLMA, List<Long> teamBlueLMA,
			LatLng teamRedFlag, LatLng teamBlueFlag, LatLng teamRedBase,
			LatLng teamBlueBase) {
		this.teamRed = teamRed;
		this.teamBlue = teamBlue;
		this.teamRedPos = teamRedPos;
		this.teamBluePos = teamBluePos;
		this.teamRedSpeed = teamRedSpeed;
		this.teamBlueSpeed = teamBlueSpeed;
		this.teamRedLMA = teamRedLMA;
		this.teamBlueLMA = teamBlueLMA;
		this.teamRedFlag = teamRedFlag;
		this.teamBlueFlag = teamBlueFlag;
		this.teamRedBase = teamRedBase;
		this.teamBlueBase = teamBlueBase;
	}
	

}
