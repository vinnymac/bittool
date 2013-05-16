package com.vinnymac.bittool;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class Splash extends Activity {

	UpdateMarket xchange = new UpdateMarket();

	MediaPlayer ourSong;

	@Override
	protected void onCreate(Bundle vinnyLovesBacon) {
		// TODO Auto-generated method stub
		super.onCreate(vinnyLovesBacon);
		setContentView(R.layout.splash);
		initialize();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	private void initialize() {

		// Start sound, don't let it carry on to next class, below ContentView
		ourSong = MediaPlayer.create(Splash.this, R.raw.splashsound);

		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
		// sends default preferences to the update which gathers market/currency
		// prefs and calls for an Xchange Ticker

		Tick data = update(getPrefs);

		// BEGIN TESTING CONNECTION AND DATA DOWNLOADING

		// CASE 1: DATA FAILS TO DOWNLOAD FROM ONLINE.
		if (data.getAsk().equals("-1")) {
			// CASE 0: NO CONNECTION FOUND
			if (!(isNetworkAvailable())) {
				Log.d("Failed to connect: ", "Connection False");
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
				alertDialog
						.setTitle("No Connection Found, try connecting to a network.");
				alertDialog.setMessage("The App Will Now Close");
				alertDialog.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// OK closes app.
								finish();
							}
						});
			} else {
				Log.d("Failed to download: ", "Market Data NUll");
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
				alertDialog
						.setTitle("Failure to Download Market Data\n Try Again Later");
				alertDialog.setMessage("The App Will Close");
				alertDialog.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// OK closes app.
								finish();
							}
						});
			}
		}

		// CASE 2: DATA DOWNLOADS SUCCESSFULLY
		else {
			Log.d("Successfully Connected: ", "Connection True.");
			Log.d("Successfully Downloaded: ", "Market Data GOOD");

			// Tick is an Array of Strings Object
			// Make the data parceable to send to the next activity.
			final Tick ticker = data;

			final boolean music = getPrefs.getBoolean("sound", true);

			if (music) {
				ourSong.start();
			}

			Thread timer = new Thread() {
				public void run() {
					try {
						// 5000 Milliseconds = 5 seconds
						sleep(500);
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					} finally {

						Intent openMainActivity = new Intent(
								"com.vinnymac.bittool.MAINACTIVITY");
						openMainActivity.putExtra("ticker", ticker);
						startActivity(openMainActivity);
					}
				}
			};
			timer.start();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ourSong.release();
		finish();
	}

	private Tick update(SharedPreferences preferences) {

		String[] markets = { "com.xeiam.xchange.mtgox.v1.MtGoxExchange",
				"com.xeiam.xchange.btce.BTCEExchange",
				"com.xeiam.xchange.bitstamp.BitstampExchange",
				"com.xeiam.xchange.campbx.CampBXExchange" };

		// Default Market
		String market = preferences.getString("market", "0");
		System.out.println("WTF IS THE MARKET: " + market);
		int position = Integer.parseInt(market);
		String[] marketNames = getResources().getStringArray(R.array.market);

		// Default Currency
		String currency = preferences.getString("currency", "0");
		String[] currencyNames = getResources()
				.getStringArray(R.array.currency);
		int pos = Integer.parseInt(currency);
		currency = currencyNames[pos];

		Log.d("Market and Currency: ", "Market: " + market + "|||Currency: "
				+ currency);

		// Ticker data = new Ticker.TickerBuilder().build();
		Tick data = new Tick("-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1");

		try {
			System.out.println(markets[position]);
			System.out.println(currency);
			data = xchange.execute(markets[position], currency).get();
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

	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}

}