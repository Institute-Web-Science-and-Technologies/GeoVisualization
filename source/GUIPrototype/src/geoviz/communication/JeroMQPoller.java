package geoviz.communication;

import geoviz.game.Game;

import java.text.SimpleDateFormat;
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
					default:
						break;
					}
					/*
					 * String msg = subscriber.recvStr(); msg =
					 * msg.substring(msg.indexOf("{"),msg.length()); final
					 * TransferObject t = gson.fromJson(msg,
					 * TransferObject.class); if (t.msgType ==
					 * TransferObject.TYPE_MSG) activity.runOnUiThread(new
					 * Runnable() {
					 * 
					 * @Override public void run() {
					 * 
					 * SimpleDateFormat dateFormat = new SimpleDateFormat(
					 * "HH:mm:ss"); ScrollView sv = (ScrollView) activity
					 * .findViewById(R.id.fragmentScrollView1); TextView
					 * scrollTv = (TextView) activity
					 * .findViewById(R.id.fragmentChatLog); if (sv != null &&
					 * scrollTv != null) { scrollTv.append(t.senderName + " " +
					 * dateFormat.format(t.timeStamp) + " :" + t.msg + "\n");
					 * sv.fullScroll(View.FOCUS_DOWN); } else {
					 * ChatScreenFragment csf = (ChatScreenFragment) activity
					 * .getSupportFragmentManager() .findFragmentByTag(
					 * "android:switcher:" + R.id.pager + ":0"); if (csf !=
					 * null) csf.msgs += t.senderName + " " + dateFormat
					 * .format(t.timeStamp) + " :" + t.msg + "\n"; } }
					 * 
					 * }); if (t.msgType != TransferObject.TYPE_MSG) {
					 * Game.getGame().update(t);
					 * 
					 * }
					 */

				}

			}

			private void handleGetGameList(String msg) {
				// TODO Auto-generated method stub
				String[] games = gson.fromJson(msg, String[].class);
				final GamesScreenFragment gsf = (GamesScreenFragment) activity
						.getSupportFragmentManager().findFragmentByTag(
								"android:switcher:" + R.id.pager + ":2");
				gsf.games = new LinkedList<String>();
				for (String game : games) {
					gsf.games.add(game);
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

			private void handleCoord(String msg) {
				TransferObject t = gson.fromJson(msg, TransferObject.class);
				Game.getGame().update(t);
			}

		}).start();

	}

}
