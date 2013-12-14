package com.vinnymac.bittool;

import java.util.concurrent.ExecutionException;

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

import com.actionbarsherlock.app.SherlockActivity;

public class SplashActivity extends SherlockActivity {

	public final static String TAG = SplashActivity.class.getSimpleName();
	private static final String KEY_MAIN_ACTIVITY = Constants.BUNDLE_KEY_ROOT
			+ "MAINACTIVITY";
	private static final String KEY_TICKER = "ticker";
	private static final String KEY_SOUND = "sound";
	private static final String KEY_MARKET = "market";
	private static final String KEY_CURRENCY = "currency";

	private UpdateMarket xchange = new UpdateMarket();
	private MediaPlayer ourSong;

	@Override
	protected void onCreate(Bundle vinnyLovesBacon) {
		super.onCreate(vinnyLovesBacon);
		setContentView(R.layout.splash);
		initialize();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void initialize() {
		// Start sound, don't let it carry on to next class, below ContentView
		ourSong = MediaPlayer.create(SplashActivity.this, R.raw.splashsound);

		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
		// sends default preferences to the update which gathers market/currency
		// prefs and calls for an Xchange Ticker

		MarketSession data = update(getPrefs);

		// BEGIN TESTING CONNECTION AND DATA DOWNLOADING

		// CASE 1: DATA FAILS TO DOWNLOAD FROM ONLINE.
		if (data.getAsk().equals("-1")) {
			// CASE 0: NO CONNECTION FOUND
			if (!(isNetworkAvailable())) {
				Log.e(TAG, "Failed to connect: Connection False");
				showAlertDialog(
						"No Connection Found, try connecting to a network.",
						"The App Will Now Close");
			} else {
				Log.e(TAG, "Failed to download: Market Data NUll");
				showAlertDialog(
						"Failure to Download Market Data\n Try Again Later",
						"The App Will Close");
			}
		}

		// CASE 2: DATA DOWNLOADS SUCCESSFULLY
		else {
			Log.e(TAG, "Successfully Connected: Connection True.");
			Log.e(TAG, "Successfully Downloaded: Market Data GOOD");

			// Tick is an Array of Strings Object
			// Make the data parsable to send to the next activity.
			final MarketSession ticker = data;

			final boolean music = getPrefs.getBoolean(KEY_SOUND, true);

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

						Intent openMainActivity = new Intent(KEY_MAIN_ACTIVITY);
						openMainActivity.putExtra(KEY_TICKER, ticker);
						startActivity(openMainActivity);
					}
				}
			};
			timer.start();
		}
	}

	private void showAlertDialog(String title, String message) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// OK closes app.
						finish();
					}
				});
		dialog.show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		ourSong.release();
		finish();
	}

	private MarketSession update(SharedPreferences preferences) {

		String[] markets = getResources().getStringArray(R.array.exchangeClass);

		// Default Market
		String market = preferences.getString(KEY_MARKET, "0");
		Log.e(TAG, "WTF IS THE MARKET: " + market);
		int position = Integer.parseInt(market);
		String[] marketNames = getResources().getStringArray(R.array.market);

		// Default Currency
		String currency = preferences.getString(KEY_CURRENCY, "0");
		String[] currencyNames = getResources()
				.getStringArray(R.array.currency);
		int pos = Integer.parseInt(currency);
		currency = currencyNames[pos];

		Log.e(TAG, "Market and Currency: " + market + "|||Currency: "
				+ currency);

		// Ticker data = new Ticker.TickerBuilder().build();
		MarketSession data = new MarketSession();

		try {
			Log.e(TAG, "Markets Item: " + markets[position]);
			Log.e(TAG, "Currency: " + currency);
			data = xchange.execute(markets[position], currency).get();
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

	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}

}