package com.vinnymac.bittool;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class BroadcastService extends Service {
	private static final String TAG = "BroadcastService";
	public static final String BROADCAST_ACTION = "com.websmithing.broadcasttest.displayevent";
	private final Handler handler = new Handler();
	Intent intent;
	int counter = 0;

	@Override
	public void onCreate() {
		super.onCreate();

		intent = new Intent(BROADCAST_ACTION);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		handler.removeCallbacks(sendUpdatesToUI);
		handler.postDelayed(sendUpdatesToUI, 1000); // 1 second

	}

	private Runnable sendUpdatesToUI = new Runnable() {
		public void run() {
			Tick data = update();
			intent.putExtra("ticker", data);
			DisplayLoggingInfo();
			handler.postDelayed(this, 30000); // 30 seconds
		}
	};

	private void DisplayLoggingInfo() {
		Log.d(TAG, "entered DisplayLoggingInfo");

		intent.putExtra("time", new Date().toString());
		intent.putExtra("counter", String.valueOf(++counter));
		sendBroadcast(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		handler.removeCallbacks(sendUpdatesToUI);
		super.onDestroy();
	}

	private Tick update() {

		String[] markets = { "com.xeiam.xchange.mtgox.v1.MtGoxExchange",
				"com.xeiam.xchange.btce.BTCEExchange",
				"com.xeiam.xchange.bitstamp.BitstampExchange",
				"com.xeiam.xchange.campbx.CampBXExchange" };

		// Ticker data = new Ticker.TickerBuilder().build();
		Tick data = new Tick("-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1");

		try {
			UpdateMarket xchange = new UpdateMarket();
			data = xchange.execute(markets[0], "USD").get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		Log.e("Market Response", data.toString());

		return data;
	}
}
