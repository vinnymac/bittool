package com.vinnymac.bittool;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DetailFragment extends Fragment {

	final static String URL = "https://www.bitstamp.net/api/ticker/";
	final static String CUR = "http://rate-exchange.appspot.com/currency?from=";
	final static String filler = "&to=";

	// Some Global ui values
	TextView tvRate;
	TextView tvBTC;
	TextView tvPrice;
	TextView tvLow;
	TextView tvHigh;
	TextView tvYesterday;
	private Spinner spinCur;
	private Context context;
	
	private String textPrice;
	private String textBTC;

	// UPDATE PROGRAM SO THAT ALL VARIABLES FROM INTERNET ARE DOWNLOADED AT THE
	// LAUNCH! Maybe even an update java app. Or a function to update.

	boolean update = true;

	// MARKET VARIABLES
	JSONContact jsonData = new JSONContact();
	
	String ask = "No Market Data";

	double asking = -1.0; // USD Asking Price

	double bitStampLow = -1.0;
	double bitStampHigh = -1.0;
	double bitStampYesterday = -1.0;

	double bitFloorPrice = -1.0;
	double bitFloorLow = -1.0;
	double bitFloorHigh = -1.0;

	// ALTERNATE CURRENCY VARIABLES
	double euro = -1.0;
	double yen = -1.0;
	double pound = -1.0;

	// double hkd;
	// double chf;
	// double aud;
	// double cny;
	// double dkk;
	// double rub;
	// double nzd;
	// double pln;
	// double sek;
	// double sgd;
	// double thb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("Test", "hello");
		
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.details, container, false);
		
		// Retrieve the string from the argument bundle that contains textBTC in main. This is used for fragment communication.
		//Bundle args = this.getArguments();
		
		//String textBTC = args.getString("1");
		//System.out.println(textBTC);
		
		textPrice = ((MainActivity) getActivity()).getPrice();
		textBTC = ((MainActivity) getActivity()).getBTC();

		// Setup buttons, views, etc
		tvPrice = (TextView) view.findViewById(R.id.price);
		tvBTC = (TextView) view.findViewById(R.id.tvBTC);
		tvRate = (TextView) view.findViewById(R.id.tv_Rate);
		tvLow = (TextView) view.findViewById(R.id.tvLow);
		tvHigh = (TextView) view.findViewById(R.id.tvHigh);
		tvYesterday = (TextView) view.findViewById(R.id.tvYesterday);

		TextView USD = (TextView) view.findViewById(R.id.USD);
		TextView EUR = (TextView) view.findViewById(R.id.EUR);

		Button btnUSD = (Button) view.findViewById(R.id.btn_USD);
		Button btnEUR = (Button) view.findViewById(R.id.btn_EUR);
		Button btnJPY = (Button) view.findViewById(R.id.btn_JPY);
		Button btnGBP = (Button) view.findViewById(R.id.btn_GBP);

		spinCur = (Spinner) view.findViewById(R.id.spinCur);
		addItemsOnspinCur();
		//addListenerOnButton();
		


		// This sets the Asking Price data from BitStamp
		tvPrice.setText("No Data");
		
		tvBTC.setText(textBTC);
		// tvBTC.setText(this.getArguments().getString("1"));
		update = isNetworkAvailable();
		
		getActivity().getIntent().putExtra("asking", asking);

		// If -1.0 gets the data on start, and after never gets it. Could change
		// to boolean on/off
		if (update) {
			// USDAsk();

			asking = queryJSON("ask", "bitstamp");
			asking = RoundTo2Decimals(asking);
			getActivity().getIntent().putExtra("asking", asking);

			bitStampLow = queryJSON("low", "bitstamp");
			bitStampHigh = queryJSON("high", "bitstamp");
			//bitStampYesterday = query(URL, "");

			//bitFloorPrice = query("https://api.bitfloor.com/ticker/1", "price");
			//bitFloorLow = query("https://api.bitfloor.com/day-info/1", "low");
			//bitFloorHigh = query("https://api.bitfloor.com/day-info/1", "high");
			

			update = false;
		}

		tvPrice.setText("$ " + asking);
		tvLow.setText("Low: " + bitStampLow);
		tvHigh.setText("High: " + bitStampHigh);
		
		((MainActivity) getActivity()).setPrice(tvPrice.getText().toString());
		
		//args.putString("0", tvPrice.getText().toString());

		// Currency Conversion Buttons!

		btnUSD.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				tvPrice.setText("$ " + asking);
			}
		});

		btnEUR.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (euro == -1.0) {
					euro = currencyChange("EUR", tvRate);
				}
				tvPrice.setText("€ " + euro);
			}
		});

		btnJPY.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (yen == -1.0) {
					yen = currencyChange("JPY", tvRate);
				}
				tvPrice.setText("¥ " + yen);
			}
		});

		btnGBP.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (pound == -1.0) {
					pound = currencyChange("GBP", tvRate);
				}
				tvPrice.setText("£ " + pound);
			}
		});

		return view;
	}

	// add items into spinner dynamically
	public void addItemsOnspinCur() {
		List<String> list = new ArrayList<String>();
		list.add("USD");
		list.add("EUR");
		list.add("JPY");
		list.add("GBP");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_spinner_item, list);

		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spinCur.setAdapter(dataAdapter);
	}

	public void addListenerOnSpinnerItemSelection() {
		spinCur.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	// get the selected dropdown list value
	public void addListenerOnButton() {
		
		Button btnSave = (Button) getActivity().findViewById(R.id.btnSave);
		
		btnSave.setOnClickListener(new OnClickListener() {
			
			 @Override
			  public void onClick(View v) {
		 
			    Toast.makeText(getActivity(),
				"OnClickListener : " +  
		                "\nSpinner 2 : "+ String.valueOf(spinCur.getSelectedItem()),
					Toast.LENGTH_SHORT).show();
			    Log.d("Spin", spinCur.getSelectedItem().toString());
			  }
		 
			});

	}

	public double currencyChange(String cur1, TextView tv) {
		// This sets the Rate for Euros and USD, NEED TO AUTOMATE VIA SELECTION!
		JsonReader reader = new JsonReader();

		String rate = "Rate";
		// reader.execute(URL);
		try {
			rate = reader.execute(CUR + "USD" + filler + cur1, "rate").get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		// ask = reader.doInBackground(URL);
		tv.setText(rate);

		// if rate isn't a double
		if (rate.equals("Rate")) {
			return -1.0;
		}
		// returns the value parsed as a double, then multiplied by the USD
		// asking price, and rounded to 2 decimals
		return RoundTo2Decimals(Double.parseDouble(rate) * asking);
	}

	// This query uses the JSONReader
	public double query(String url, String key) {
		JsonReader reader = new JsonReader();

		String value = "No Market Data";

		try {
			value = reader.execute(url, key).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		Log.e("JSON Response", value);

		double price = -1.0;
		if (!(value.equals("No Market Data") || value == null)) {
			price = Double.parseDouble(value);
		}

		return price;
	}
	
	
	// queryJSON uses the JSONContact class called by Splash. Less work.
	private double queryJSON(String key, String extra){
		String data = getActivity().getIntent().getExtras().getString(extra);
		
		String value = "No Market Data";

		value = jsonData.convert(data, key);

		Log.e("JSON Response", value);

		double price = -1.0;
		if (!(value.equals("No Market Data") || value == null)) {
			price = Double.parseDouble(value);
		}

		return price;
		
		
		
	}

	public void USDAsk() {
		JsonReader reader = new JsonReader();
		// reader.execute(URL);
		try {
			ask = reader.execute(URL, "ask").get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		// ask = reader.doInBackground(URL);
		tvPrice.setText("$ " + ask);
		if (!(ask.equals("No Market Data") || ask == null)) {
			// asking = Double.parseDouble(ask);
		}
	}

	public double RoundTo2Decimals(double val) {
		DecimalFormat df2 = new DecimalFormat("###.##");

		return Double.valueOf(df2.format(val));
	}

	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}

}
