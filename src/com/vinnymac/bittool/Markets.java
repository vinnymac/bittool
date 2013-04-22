package com.vinnymac.bittool;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.vinnymac.bittool.Xchange.DataDownloadListener;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.bitcoincentral.BitcoinCentralExchange;
import com.xeiam.xchange.bitstamp.BitstampExchange;
import com.xeiam.xchange.btce.BTCEExchange;
import com.xeiam.xchange.campbx.CampBXExchange;
import com.xeiam.xchange.currency.Currencies;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.mtgox.v1.MtGoxExchange;
import com.xeiam.xchange.service.marketdata.polling.PollingMarketDataService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
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

public class Markets extends Fragment {

	final static String URL = "https://www.bitstamp.net/api/ticker/";
	final static String CUR = "http://rate-exchange.appspot.com/currency?from=";
	final static String filler = "&to=";

	// Some Global ui values
	TextView tvRate;
	TextView tvBTC;
	TextView tvPrice;
	TextView tvLow;
	TextView tvHigh;
	TextView tvLast;
	TextView tvVolume;
	TextView marketID;
	// TextView USD;
	// TextView EUR;

	// Buttons
	Button btnUSD;
	Button btnEUR;
	Button btnJPY;
	Button btnGBP;

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
	double low = -1.0; // High of Exchange
	double high = -1.0; // Low of Exchange
	double volume = -1.0; // Volume of Exchange
	double last = -1.0; // Last Price

	/*
	 * double bitStampLow = -1.0; double bitStampHigh = -1.0; double
	 * bitStampYesterday = -1.0;
	 * 
	 * double bitFloorPrice = -1.0; double bitFloorLow = -1.0; double
	 * bitFloorHigh = -1.0;
	 * 
	 * // ALTERNATE CURRENCY VARIABLES double euro = -1.0; double yen = -1.0;
	 * double pound = -1.0;
	 * 
	 * // double hkd; // double chf; // double aud; // double cny; // double
	 * dkk; // double rub; // double nzd; // double pln; // double sek; //
	 * double sgd; // double thb;
	 */
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
		View view = inflater.inflate(R.layout.markets, container, false);

		update = isNetworkAvailable();

		// Retrieve the string from the argument bundle that contains textBTC in
		// main. This is used for fragment communication.
		/*
		 * textBTC = getActivity().getIntent().getExtras().getString("1");
		 * 
		 * if (!(textBTC == null || textBTC.equals(""))) { Log.d("huh?",
		 * textBTC); } else { Log.d("SHIT", "SHIT"); }
		 */
		initialize(view);
		LoadPreferences();
		if (update) {

			update = false;
		}

		addItemsOnspinCur();
		// addListenerOnButton();

		// This sets the Asking Price data from BitStamp
		// tvPrice.setText("No Data");

		// tvBTC.setText(this.getArguments().getString("1"));

		// getActivity().getIntent().putExtra("asking", asking);

		/*
		 * 
		 * // If -1.0 gets the data on start, and after never gets it. Could
		 * change // to boolean on/off if (update) { // USDAsk();
		 * 
		 * asking = queryJSON("ask", "bitstamp"); asking =
		 * RoundTo2Decimals(asking);
		 * getActivity().getIntent().putExtra("asking", asking);
		 * 
		 * bitStampLow = queryJSON("low", "bitstamp"); bitStampHigh =
		 * queryJSON("high", "bitstamp"); // bitStampYesterday = query(URL, "");
		 * 
		 * // bitFloorPrice = query("https://api.bitfloor.com/ticker/1", //
		 * "price"); // bitFloorLow =
		 * query("https://api.bitfloor.com/day-info/1", // "low"); //
		 * bitFloorHigh = query("https://api.bitfloor.com/day-info/1", //
		 * "high");
		 * 
		 * update = false; }
		 * 
		 * tvPrice.setText("$ " + asking); tvLow.setText("Low: " + bitStampLow);
		 * tvHigh.setText("High: " + bitStampHigh);
		 * 
		 * ((MainActivity)
		 * getActivity()).setPrice(tvPrice.getText().toString());
		 * 
		 * // args.putString("0", tvPrice.getText().toString());
		 * 
		 * // Currency Conversion Buttons!
		 * 
		 * btnUSD.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { tvPrice.setText("$ " +
		 * asking); } });
		 * 
		 * btnEUR.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { if (euro == -1.0) { euro =
		 * currencyChange("EUR", tvRate); } tvPrice.setText("€ " + euro); } });
		 * 
		 * btnJPY.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { if (yen == -1.0) { yen =
		 * currencyChange("JPY", tvRate); } tvPrice.setText("¥ " + yen); } });
		 * 
		 * btnGBP.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { if (pound == -1.0) { pound
		 * = currencyChange("GBP", tvRate); } tvPrice.setText("£ " + pound); }
		 * });
		 */
		return view;
	}

	private void initialize(View view) {
		// textPrice = ((MainActivity) getActivity()).getPrice();
		// textBTC = ((MainActivity) getActivity()).getBTC();

		// Setup buttons, views, etc
		tvPrice = (TextView) view.findViewById(R.id.price);
		tvBTC = (TextView) view.findViewById(R.id.tvBTC);
		//tvRate = (TextView) view.findViewById(R.id.tv_Rate);
		tvLow = (TextView) view.findViewById(R.id.tvLow);
		tvHigh = (TextView) view.findViewById(R.id.tvHigh);
		tvLast = (TextView) view.findViewById(R.id.tvLast);
		tvVolume = (TextView) view.findViewById(R.id.tvVolume);

		marketID = (TextView) view.findViewById(R.id.tvMarketID);

		// USD = (TextView) view.findViewById(R.id.USD);
		// EUR = (TextView) view.findViewById(R.id.EUR);

		//btnUSD = (Button) view.findViewById(R.id.btn_USD);
		//btnEUR = (Button) view.findViewById(R.id.btn_EUR);
		//btnJPY = (Button) view.findViewById(R.id.btn_JPY);
		//btnGBP = (Button) view.findViewById(R.id.btn_GBP);

		//spinCur = (Spinner) view.findViewById(R.id.spinCur);

	}

	private void LoadPreferences() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this.getActivity());
		String market = preferences.getString("market", "-1");
		Map map = preferences.getAll();
		System.out.println("WTF : " + map.values() + " HUH " + map.toString()
				+ " WHAT! " + map.containsKey(0));
		System.out.println(market);

		String[] marketNames = getResources().getStringArray(R.array.market);
		int position = Integer.parseInt(market);
		marketID.setText(marketNames[position]);

		if (update) {
			// XChange Library connects, and sets the layout up.
			download(position);
			update = false;
		}

		/*
		 * if (ticker != null) { tvPrice.setText(ticker.getAsk().toString());
		 * tvLow.setText(ticker.getLow().toString());
		 * tvHigh.setText(ticker.getHigh().toString());
		 * tvLast.setText(ticker.getLast().toString());
		 * tvVolume.setText(ticker.getVolume().toString()); }
		 */
		textBTC = getActivity().getIntent().getExtras().getString("1");
		System.out.println(textBTC);
		textBTC = getActivity().getIntent().getStringExtra("1");
		System.out.println(textBTC);
		textBTC = (String) getActivity().getIntent().getExtras().get("1");
		System.out.println(textBTC);
		tvBTC.setText(textBTC);

	}

	public void download(int position) {

		Xchange xchange = new Xchange();
		xchange.setDataDownloadListener(new DataDownloadListener() {
			@Override
			public void dataDownloadedSuccessfully(Ticker ticker) {
				// handler result
				System.out.println("Success");
				System.out.println(ticker.toString());

				asking = Double.parseDouble(ticker.getAsk().toString()
						.replace("USD ", ""));
				tvPrice.setText("$ " + String.format("%.2f", asking));
				
				((MainActivity) getActivity()).setPrice(tvPrice.getText().toString());
				/*
				String price = tvPrice.getText().toString();
				System.out.println(price);
				getActivity().getIntent().putExtra("0", price);
				System.out.println(price);
				getActivity().getIntent().putExtra("asking", price);
				System.out.println(price);*/

				low = Double.parseDouble(ticker.getLow()
						.toString().replace("USD ", ""));
				tvLow.setText("Low: " + String.format("%.2f", low));

				high = Double.parseDouble(ticker.getHigh()
						.toString().replace("USD ", ""));
				tvHigh.setText("High: " + String.format("%.2f", high));

				last = Double.parseDouble(ticker.getLast()
						.toString().replace("USD ", ""));
				tvLast.setText("Last: " + String.format("%.2f", last));

				volume = Double.parseDouble(ticker.getVolume()
						.toString());
				tvVolume.setText("Volume: " + String.format("%.2f", volume));
			}

			@Override
			public void dataDownloadFailed() {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						getActivity());
				alertDialog
						.setTitle("Failure to Download Market Data\n Try Again Later");
				alertDialog.setMessage("The App Will Close");
				alertDialog.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// here you can add functions
								// getActivity().finish();
							}
						});
				alertDialog.setIcon(R.drawable.ic_launcher);
				alertDialog.show();
				System.out.println("Failure");
				tvPrice.setText("$ 0.00");
				tvLow.setText("Low: 0.00");
				tvHigh.setText("High: 0.00");
				tvLast.setText("0.00");
				tvVolume.setText("0000");
				// handler failure (e.g network not available etc.)
			}
		});

		switch (position) {
		// mtgox
		case 0:
			xchange.execute("com.xeiam.xchange.mtgox.v1.MtGoxExchange", "USD");
			break;
		// btce
		case 1:
			xchange.execute("com.xeiam.xchange.btce.BTCEExchange", "USD");
			break;
		// bitstamp
		case 2:
			xchange.execute("com.xeiam.xchange.bitstamp.BitstampExchange",
					"USD");
			break;
		// campbx
		case 3:
			xchange.execute("com.xeiam.xchange.campbx.CampBXExchange", "USD");
			break;
		// bitcoincentral
		case 4:
			xchange.execute(
					"com.xeiam.xchange.bitcoincentral.BitcoinCentralExchange",
					"USD");
			break;
		default:
			xchange.execute("com.xeiam.xchange.mtgox.v1.MtGoxExchange", "USD");
			break;
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// LoadPreferences();
		//getActivity().getIntent().putExtra("asking", tvPrice.getText().toString());
		//System.out.println("" + tvPrice.getText().toString());
		//getActivity().getIntent().putExtra("0", tvPrice.getText().toString());
		//System.out.println("" + tvPrice.getText().toString());
		// tvPrice.getText().toString());
		
		textBTC = getActivity().getIntent().getExtras().getString("1");
		System.out.println(textBTC);
		textBTC = getActivity().getIntent().getStringExtra("1");
		System.out.println(textBTC);
		textBTC = (String) getActivity().getIntent().getExtras().get("1");
		System.out.println(textBTC);
		tvBTC.setText(textBTC);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		// Make sure that we are currently visible
		if (this.isVisible()) {
			// If we are becoming invisible, then...
			if (!isVisibleToUser) {
				Log.d("Market", "Not visible anymore.  Stopping market.");
				// TODO stop audio playback
			}
		}
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
		// Change look of Spinner with:
		// dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spinCur.setAdapter(dataAdapter);
	}

	public void addListenerOnSpinnerItemSelection() {
		spinCur.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}
	/*
	// get the selected dropdown list value
	public void addListenerOnButton() {

		Button btnSave = (Button) getActivity().findViewById(R.id.btnSave);

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Toast.makeText(
						getActivity(),
						"OnClickListener : " + "\nSpinner 2 : "
								+ String.valueOf(spinCur.getSelectedItem()),
						Toast.LENGTH_SHORT).show();
				Log.d("Spin", spinCur.getSelectedItem().toString());
			}

		});

	}

	
	 * public double currencyChange(String cur1, TextView tv) { // This sets the
	 * Rate for Euros and USD, NEED TO AUTOMATE VIA SELECTION! JsonReader reader
	 * = new JsonReader();
	 * 
	 * String rate = "Rate"; // reader.execute(URL); try { rate =
	 * reader.execute(CUR + "USD" + filler + cur1, "rate").get(); } catch
	 * (InterruptedException e) { e.printStackTrace(); } catch
	 * (ExecutionException e) { e.printStackTrace(); } // ask =
	 * reader.doInBackground(URL); tv.setText(rate);
	 * 
	 * // if rate isn't a double if (rate.equals("Rate")) { return -1.0; } //
	 * returns the value parsed as a double, then multiplied by the USD //
	 * asking price, and rounded to 2 decimals return
	 * RoundTo2Decimals(Double.parseDouble(rate) * asking); }
	 * 
	 * // This query uses the JSONReader public double query(String url, String
	 * key) { JsonReader reader = new JsonReader();
	 * 
	 * String value = "No Market Data";
	 * 
	 * try { value = reader.execute(url, key).get(); } catch
	 * (InterruptedException e) { e.printStackTrace(); } catch
	 * (ExecutionException e) { e.printStackTrace(); }
	 * 
	 * Log.e("JSON Response", value);
	 * 
	 * double price = -1.0; if (!(value.equals("No Market Data") || value ==
	 * null)) { price = Double.parseDouble(value); }
	 * 
	 * return price; }
	 * 
	 * // queryJSON uses the JSONContact class called by Splash. Less work.
	 * private double queryJSON(String key, String extra) { String data =
	 * getActivity().getIntent().getExtras().getString(extra);
	 * 
	 * String value = "No Market Data";
	 * 
	 * value = jsonData.convert(data, key);
	 * 
	 * Log.e("JSON Response", value);
	 * 
	 * double price = -1.0; if (!(value.equals("No Market Data") || value ==
	 * null)) { price = Double.parseDouble(value); }
	 * 
	 * return price;
	 * 
	 * }
	 * 
	 * public void USDAsk() { JsonReader reader = new JsonReader(); //
	 * reader.execute(URL); try { ask = reader.execute(URL, "ask").get(); }
	 * catch (InterruptedException e) { e.printStackTrace(); } catch
	 * (ExecutionException e) { e.printStackTrace(); } // ask =
	 * reader.doInBackground(URL); tvPrice.setText("$ " + ask); if
	 * (!(ask.equals("No Market Data") || ask == null)) { // asking =
	 * Double.parseDouble(ask); } }
	 */

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
