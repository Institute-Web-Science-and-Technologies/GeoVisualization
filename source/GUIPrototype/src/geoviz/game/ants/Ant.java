package geoviz.game.ants;

import java.util.List;
import java.util.Map;

import com.example.fragments.MapScreenFragment;
import com.example.guiprototype.SwipeScreen;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class Ant {

	static final int ROLE_DEAD = 0, ROLE_WORKER = 1, ROLE_WARRIOR = 2;

	private LatLng pos;
	final String name;
	private Marker marker;
	private int team, role = ROLE_WORKER;

	public Ant(final String name, final Team team) {
		super();
		this.name = name;
		this.team = team.id;
		SwipeScreen.getInstance().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				marker = MapScreenFragment.getMSF().initMarker(team.color, pos,
						name);

			}

		});
		
	}

	public void updatePos(final LatLng newpos) {
		pos = newpos;
		if (marker != null)
			SwipeScreen.getInstance().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					marker.setPosition(newpos);
				}
			});
		changeRole(ROLE_DEAD);
	}

	public void checkCollision(Map<String, Ant> as, List<FoodSource> fss,
			List<Team> ts) {

	}

	public void changeRole(int newrole) {
		if (marker != null) {
			role = newrole;
			SwipeScreen.getInstance().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					marker.setAlpha(role ==ROLE_DEAD?0.50f:1.f);
				}
			});
		}
	}

}
