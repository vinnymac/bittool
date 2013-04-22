package com.vinnymac.bittool;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class About extends SherlockActivity {

	TextView tv;
	TextView btcAddress;

	ImageView qrCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		getSupportActionBar().setHomeButtonEnabled(true);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.about);
		initialize();

		tv.setText("Developed by Vinnymac");

	}

	private void initialize() {
		tv = (TextView) findViewById(R.id.aboutText);
		btcAddress = (TextView) findViewById(R.id.tvAddress);
		qrCode = (ImageView) findViewById(R.id.ivQrcode);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent mainIntent = new Intent(this, MainActivity.class);
			mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mainIntent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
