package com.vinnymac.bittool;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.xeiam.xchange.dto.marketdata.Ticker;

public class Splash extends Activity {

	// JSONContact jsonData = new JSONContact();

	UpdateMarket xchange = new UpdateMarket();

	// Bitstamp Market
	// final static String URL = "https://www.bitstamp.net/api/ticker/";

	// BTC-E Market https://btc-e.com/api/2/btc_usd/ticker
	// CampBX Market http://campbx.com/api/xticker.php
	// MTGox Market http://data.mtgox.com/api/1/BTCUSD/ticker

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
		// Gets latest JSON data from a site like bitstamp
		// final String bitstampArray = updateJSON(URL);

		// Start sound, don't let it carry on to next class, below ContentView
		ourSong = MediaPlayer.create(Splash.this, R.raw.splashsound);

		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
		// sends default preferences to the update which gathers market/currency
		// prefs and calls for an Xchange Ticker
		
		Ticker data = update(getPrefs);
		
		if(data == null){
			data = new Ticker.TickerBuilder().build();
		}
		
		if (data != null) {

			// Make the data parceable to send to the next activity.
			final Tick ticker = new Tick(data.getTradableIdentifier()
					.toString(), data.getLast().toString(), data.getBid()
					.toString(), data.getAsk().toString(), data.getHigh()
					.toString(), data.getLow().toString(), data.getVolume()
					.toString(), data.getTimestamp().toString());
			
			final boolean music = getPrefs.getBoolean("sound", true);

			if (music == true) {
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
		} else {
			// Show splashscreen telling user cannot get data at this time.
			Log.d("Failed to start: ", "Market Data NUll");
			Log.e("Failed to start: ", "Market Data NUll");
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ourSong.release();
		finish();
	}

	private Ticker update(SharedPreferences preferences) {
		
		String[] markets = {"com.xeiam.xchange.mtgox.v1.MtGoxExchange","com.xeiam.xchange.btce.BTCEExchange",
				"com.xeiam.xchange.bitstamp.BitstampExchange","com.xeiam.xchange.campbx.CampBXExchange",
				"com.xeiam.xchange.bitcoincentral.BitcoinCentralExchange"};

		// Default Market
		String market = preferences.getString("market", "0");
		System.out.println("WTF IS THE MARKET: " + market);
		int position = Integer.parseInt(market);
		String[] marketNames = getResources()
				.getStringArray(R.array.market);

		// Default Currency
		String currency = preferences.getString("currency", "0");
		String[] currencyNames = getResources()
				.getStringArray(R.array.currency);
		int pos = Integer.parseInt(currency);
		currency = currencyNames[pos];

		Log.d("Market and Currency: ", "Market: " + market + "|||Currency: "
				+ currency);

		Ticker data = new Ticker.TickerBuilder().build();

		try {
			System.out.println(markets[position]);
			System.out.println(currency);
			data = xchange.execute(markets[position], currency).get();
			//data = xchange.execute("com.xeiam.xchange.bitstamp.BitstampExchange", "USD").get();
			// data = jsonData.execute(URL).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();	}

		Log.e("Market Response", data.toString());

		return data;
	}

}