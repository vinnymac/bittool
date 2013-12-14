package com.vinnymac.bittool;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class BroadcastService extends Service {

	public final static String TAG = BroadcastService.class.getSimpleName();
	public static final String BROADCAST_ACTION = "com.vinnymac.bittool.broadcasttest";
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
			MarketSession data = update();
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

	private MarketSession update() {

		String[] markets = { "com.xeiam.xchange.mtgox.v1.MtGoxExchange",
				"com.xeiam.xchange.btce.BTCEExchange",
				"com.xeiam.xchange.bitstamp.BitstampExchange",
				"com.xeiam.xchange.campbx.CampBXExchange" };

		MarketSession data = new MarketSession();

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

		Log.e(TAG, "Market Response: " + data.toString());

		return data;
	}
}
