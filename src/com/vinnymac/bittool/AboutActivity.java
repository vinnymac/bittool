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

public class AboutActivity extends SherlockActivity {

	TextView tv;
	TextView btcAddress;

	ImageView qrCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
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
		switch (item.getItemId()) {

		case android.R.id.home:
			// app icon in action bar clicked; go home
			finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
