package com.vinnymac.bittool;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class Options extends Fragment {

	Spinner markets;
	Spinner currencies;
	TextView defaults;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.options, container, false);
		
		markets = (Spinner) view.findViewById(R.id.spinMarkets);
		currencies = (Spinner) view.findViewById(R.id.spinCurrency);
		defaults = (TextView) view.findViewById(R.id.tvDefault);
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}

}
