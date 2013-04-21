package com.vinnymac.bittool;

import java.util.concurrent.ExecutionException;

import com.vinnymac.bittool.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

public class Splash extends Activity {

	JSONContact jsonData = new JSONContact();
	
	//Bitstamp Market
	//final static String URL = "https://www.bitstamp.net/api/ticker/";
	
	//BTC-E Market https://btc-e.com/api/2/btc_usd/ticker
	//CampBX Market http://campbx.com/api/xticker.php
	//MTGox Market http://data.mtgox.com/api/1/BTCUSD/ticker

	MediaPlayer ourSong;

	@Override
	protected void onCreate(Bundle vinnyLovesBacon) {
		// TODO Auto-generated method stub
		super.onCreate(vinnyLovesBacon);
		setContentView(R.layout.splash);

		// Gets latest JSON data from a site like bitstamp
		//final String bitstampArray = updateJSON(URL);

		// Start sound, don't let it carry on to next class, below ContentView
		ourSong = MediaPlayer.create(Splash.this, R.raw.splashsound);

		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		final boolean music = getPrefs.getBoolean("checkbox", true);

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
					//openMainActivity.putExtra("bitstamp", bitstampArray);
					startActivity(openMainActivity);
				}
			}
		};
		timer.start();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ourSong.release();
		finish();
	}

	private String updateJSON(String URL) {

		String data = "No Market Data";

		try {
			data = jsonData.execute(URL).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		Log.e("JSON Response", data);

		return data;
	}

}
