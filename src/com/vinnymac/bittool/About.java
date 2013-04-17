package com.vinnymac.bittool;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		TextView tv = (TextView) findViewById(R.id.aboutText);
		TextView btcAddress = (TextView) findViewById(R.id.tvAddress);
		ImageView qrCode = (ImageView) findViewById(R.id.ivQrcode);

		tv.setText("Vinnymac");
	}

}
