package geoviz.communication;

import geoviz.game.Game;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.zeromq.ZMQ;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragments.ChatScreenFragment;
import com.example.fragments.GamesScreenFragment;
import com.example.guiprototype.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//subscribes smartphone to the server and handles incoming communication
public class JeroMQPoller {

	final FragmentActivity activity;
	final String serverIP;
	final List<String> subscriptions;


	public JeroMQPoller(FragmentActivity self) {
		super();
		this.activity = self;
		this.serverIP = Const.serverIP;

		subscriptions = new LinkedList<String>();
	}
	public List<String> getSubscriptions(){
		return subscriptions;
	}
	public void addSubscription(String id) {
		if (!subscriptions.contains(id))
			subscriptions.add(id);

	}

	public void deleteSubscription(String id) {
		if (subscriptions.contains(id))
			subscriptions.remove(id);
	}

	public void poll() {

		new Thread(new Runnable() {

			Gson gson;


			@Override
			public void run() {
				// TODO Auto-generated method stub
				ZMQ.Context context = ZMQ.context(1);
				ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
				subscriber.connect(serverIP + ":5558");

				for (String id : subscriptions)
					subscriber.subscribe(id.getBytes());
				List<String> currentlySubscribedTo = new LinkedList<String>();
				currentlySubscribedTo.addAll(subscriptions);
				gson = new GsonBuilder().create();

				while (true) {
					List<String> copy = new LinkedList<String>(subscriptions);
					for (String id : copy) {

						if (!currentlySubscribedTo.contains(id)) {
							subscriber.subscribe(id.getBytes());
							currentlySubscribedTo.add(id);
						}
					}
					copy = new LinkedList<String>(currentlySubscribedTo);
					for (String id : copy) {
						if (!subscriptions.contains(id)) {
							subscriber.unsubscribe(id.getBytes());
							currentlySubscribedTo.remove(id);
						}
					}
					final String topic = subscriber.recvStr();
					final String msgType = subscriber.recvStr();
					final String msg = subscriber.recvStr();
					Log.d("mytag", msgType);
					
					
					switch (Integer.parseInt(msgType)) {
					case TransferObject.TYPE_COORD:
						handleCoord(msg);
						break;
					case TransferObject.TYPE_MSG:
						handleMsg(msg);
						break;
					case TransferObject.TYPE_ADD_CHICKEN:
						handleAddChicken(msg);
						break;
					case TransferObject.TYPE_KILL_CHICKEN:
						handleKillChicken(msg);
						break;
					case TransferObject.TYPE_CREATE:
						handleCreate(msg);
						break;
					case TransferObject.TYPE_GET_GAMELIST:
						handleGetGameList(msg);
						break;
					case TransferObject.TYPE_SNAKE_DIED:
						handleSnakeDied(msg);
						break;
					case TransferObject.TYPE_JOIN_GAME:
						handleJoin(msg);
						break;
					case TransferObject.TYPE_GAME_STATUS:
						handleGameStatus(msg);
						break;
					case TransferObject.TYPE_SET_FLAG:
						handleSetFlag(msg);
						break;
					case TransferObject.TYPE_SET_BASE:
						handleSetBase(msg);
						break;
					case TransferObject.TYPE_FLAGCARRIER_SHOT:
						handleFlagcarrierShot(msg);
						break;
					case TransferObject.TYPE_DELIVER_FLAG:
						handleDeliverFlag(msg);
						break;
					default:
						TransferObject t = gson.fromJson(msg, TransferObject.class);
						Game.getGame().update(t);
						break;
					}

				}

			}

			private void handleDeliverFlag(String msg) {
				TransferToServerObject ttso = gson.fromJson(msg, TransferToServerObject.class);
				TransferObject t =new TransferObject(TransferObject.TYPE_FLAGCARRIER_SHOT, ttso.team, null,
				null, null, new LatLng(ttso.latitude,ttso.longitude), null);
				Game.getGame().update(t);
				
			}

			private void handleFlagcarrierShot(String msg) {
				TransferToServerObject ttso = gson.fromJson(msg, TransferToServerObject.class);
				TransferObject t =new TransferObject(TransferObject.TYPE_FLAGCARRIER_SHOT, ttso.team, null,
				null, null, new LatLng(ttso.latitude,ttso.longitude), null);
				Game.getGame().update(t);
			}

			private void handleSetBase(String msg) {
				TransferToServerObject ttso = gson.fromJson(msg, TransferToServerObject.class);
				TransferObject t =new TransferObject(TransferObject.TYPE_SET_BASE, ttso.team, null,
				null, null, new LatLng(ttso.latitude,ttso.longitude), null);
				Game.getGame().update(t);
			}

			private void handleSetFlag(String msg) {
				TransferToServerObject ttso = gson.fromJson(msg, TransferToServerObject.class);
				
				TransferObject t =new TransferObject(TransferObject.TYPE_SET_FLAG, ttso.team, null,
				null, null, new LatLng(ttso.latitude,ttso.longitude), null);
				Game.getGame().update(t);
			
			}

	

			private void handleGameStatus(String msg) {
				// TODO Auto-generated method stub
				TransferObject t = gson.fromJson(msg, TransferObject.class);
				Game.getGame().update(t);
			}

			private void handleJoin(String msg) {
				// TODO Auto-generated method stub
				TransferObject t = gson.fromJson(msg, TransferObject.class);
				Game.getGame().update(t);
			}

			private void handleGetGameList(String msg) {
				// TODO Auto-generated method stub
				String[] games = gson.fromJson(msg, String[].class);
				final GamesScreenFragment gsf = (GamesScreenFragment) activity
						.getSupportFragmentManager().findFragmentByTag(
								"android:switcher:" + R.id.pager + ":2");
				gsf.games = new LinkedList<String>();
				gsf.gamenames = new LinkedList<String>();
				for (String game : games) {
					String[] names = game.split(";");
					gsf.games.add(names[0]);
					if (names[0].startsWith("0"))
						gsf.gamenames.add("Snakegame von "+ names[1]);
					else
						gsf.gamenames.add("Flaggame von "+ names[1]);
				}
				activity.runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						gsf.adapter.notifyDataSetChanged();
					}
					
				});
				
			}

			private void handleCreate(String msg) {
				// Sollte niemals geschehen vll ne Exception werfen

			}

			private void handleKillChicken(String msg) {
				TransferObject t = gson.fromJson(msg, TransferObject.class);
				Game.getGame().update(t);

			}

			private void handleAddChicken(String msg) {
				TransferObject t = gson.fromJson(msg, TransferObject.class);
				Game.getGame().update(t);
			}

			private void handleMsg(String msg) {
				// TODO Auto-generated method stub
				final TransferObject t = gson.fromJson(msg,
						TransferObject.class);
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {

						SimpleDateFormat dateFormat = new SimpleDateFormat(
								"HH:mm:ss");
						ScrollView sv = (ScrollView) activity
								.findViewById(R.id.fragmentScrollView1);
						TextView scrollTv = (TextView) activity
								.findViewById(R.id.fragmentChatLog);
						if (sv != null && scrollTv != null) {
							scrollTv.append(t.senderName + " "
									+ dateFormat.format(t.timeStamp) + " :"
									+ t.msg + "\n");
							sv.fullScroll(View.FOCUS_DOWN);
						} else {
							ChatScreenFragment csf = (ChatScreenFragment) activity
									.getSupportFragmentManager()
									.findFragmentByTag(
											"android:switcher:" + R.id.pager
													+ ":0");
							if (csf != null)
								csf.msgs += t.senderName + " "
										+ dateFormat.format(t.timeStamp) + " :"
										+ t.msg + "\n";
						}
					}

				});

			}
			private void handleSnakeDied(String msg){
				TransferObject t = gson.fromJson(msg, TransferObject.class);
				Game.getGame().update(t);
			}
			private void handleCoord(String msg) {
				TransferObject t = gson.fromJson(msg, TransferObject.class);
				Game.getGame().update(t);
			}

		}).start();

	}

}
