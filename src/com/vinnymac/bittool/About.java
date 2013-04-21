package com.vinnymac.bittool;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class About extends Activity {
	
	TextView tv;
	TextView btcAddress;
	ImageView qrCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		initialize();
		
		tv.setText("Vinnymac");
		
	}
	
	private void initialize(){
		tv = (TextView) findViewById(R.id.aboutText);
		btcAddress = (TextView) findViewById(R.id.tvAddress);
		qrCode = (ImageView) findViewById(R.id.ivQrcode);
		
	}
}
