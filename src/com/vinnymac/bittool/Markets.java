package com.vinnymac.bittool;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.vinnymac.bittool.Xchange.DataDownloadListener;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.bitstamp.BitstampExchange;
import com.xeiam.xchange.btce.BTCEExchange;
import com.xeiam.xchange.campbx.CampBXExchange;
import com.xeiam.xchange.currency.Currencies;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.mtgox.v1.MtGoxExchange;
import com.xeiam.xchange.service.marketdata.polling.PollingMarketDataService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Markets extends Fragment {
	
	public final static String TAG = Markets.class.getSimpleName();

	final static String URL = "https://www.bitstamp.net/api/ticker/";
	final static String CUR = "http://rate-exchange.appspot.com/currency?from=";
	final static String filler = "&to=";
	public final static String TICKER = "ticker";

	// Some Global ui values
	TextView tvRate;
	TextView tvChange;
	TextView tvPrice;
	TextView tvLow;
	TextView tvHigh;
	TextView tvLast;
	TextView tvVolume;
	TextView marketID;
	// TextView USD;
	// TextView EUR;

	// Image Views
	ImageView ivChange;

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
	// JSONContact jsonData = new JSONContact();

	String ask = "No Market Data";

	final static String ARG_ASK_KEY = "price";

	double asking = -1.0; // USD Asking Price
	double low = -1.0; // High of Exchange

	double high = -1.0; // Low of Exchange
	double volume = -1.0; // Volume of Exchange
	double last = -1.0; // Last Price

	private Tick ticker;

	private Callbacks mCallbacks;

	private SharedPreferences preferences;

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

	public interface Callbacks {
		// Rename to onMarketUpdated later. Right now this will happen when the
		// market data is downloaded.
		public void onItemSelected(double id);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mCallbacks = (Callbacks) activity;
		} catch (ClassCastException ex) {
			Log.e(getTag(),
					"Casting the activity as a Callbacks listener failed" + ex);
			mCallbacks = null;
		}
	}

	public void onPriceUpdated(double price, int position) {
		if (mCallbacks != null) {
			mCallbacks.onItemSelected(price);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("Test", "hello");

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (getActivity().getIntent() != null) {
			ticker = getActivity().getIntent().getParcelableExtra(TICKER);
		}
		
		if (ticker == null) {
			ticker = new Tick("-1", "-1", "-1", "-1", "-1", "-1", "-1", "-1");
			if (savedInstanceState != null) {
				if (savedInstanceState.containsKey(TICKER)) {
					ticker = savedInstanceState.getParcelable(TICKER);
				} else {
					ticker = new Tick("-1", "-1", "-1", "-1", "-1", "-1", "-1",
							"-1");
				}
			} else {
				ticker = new Tick("-1", "-1", "-1", "-1", "-1", "-1", "-1",
						"-1");
			}
		}
		
		// Actually updates and sets up the text and data.
		setup();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.markets, container, false);

		// Check if it is safe to update.
		update = isNetworkAvailable();

		initialize(view);

		/*
		 * LoadPreferences(); if (update) {
		 * 
		 * update = false; }
		 */
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(TICKER, ticker);
	}

	private void initialize(View view) {

		// Setup buttons, views, etc
		tvPrice = (TextView) view.findViewById(R.id.price);
		tvChange = (TextView) view.findViewById(R.id.tvChange);

		tvLow = (TextView) view.findViewById(R.id.tvLow);
		tvHigh = (TextView) view.findViewById(R.id.tvHigh);
		tvLast = (TextView) view.findViewById(R.id.tvLast);
		tvVolume = (TextView) view.findViewById(R.id.tvVolume);

		marketID = (TextView) view.findViewById(R.id.tvMarketID);

		ivChange = (ImageView) view.findViewById(R.id.ivChange);

	}

	public void setup() {
		// ///////*********THIS IS THE SETUP***********/////////

		preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String market = preferences.getString("market", "0");
		String[] marketNames = getResources().getStringArray(R.array.market);
		int position = Integer.parseInt(market);
		marketID.setText(marketNames[position]);

		// Default Currency
		// preferences.getInt(key, defValue);
		String currency = preferences.getString("currency", "0");
		String[] currencyNames = getResources()
				.getStringArray(R.array.currency);
		int pos = Integer.parseInt(currency);
		currency = currencyNames[pos];

		System.out.println("Success");
		System.out.println(ticker.toString());

		Log.d("Currency before Double Conversion", currency.toString());

		asking = Double.parseDouble(ticker.getAsk().toString()
				.replace(currency + " ", ""));
		tvPrice.setText("$ " + String.format("%.2f", asking));

		onPriceUpdated(asking, 0);

		((MainActivity) getActivity()).setPrice(tvPrice.getText().toString());

		low = Double.parseDouble(ticker.getLow().toString()
				.replace(currency + " ", ""));
		tvLow.setText("Low: " + String.format("%.2f", low));

		high = Double.parseDouble(ticker.getHigh().toString()
				.replace(currency + " ", ""));
		tvHigh.setText("High: " + String.format("%.2f", high));

		last = Double.parseDouble(ticker.getLast().toString()
				.replace(currency + " ", ""));
		tvLast.setText("Last: " + String.format("%.2f", last));

		volume = Double.parseDouble(ticker.getVolume().toString());
		tvVolume.setText("Volume: " + String.format("%.2f", volume));

		UpDownEqual();

	}

	private void UpDownEqual() {
		// Math for Equal/Up/Down Change between Asking Price and Last Price
		double change = asking - last;
		tvChange.setText(String.format("%.2f", change));
		if (asking > last) {
			ivChange.setImageResource(R.drawable.up);
		} else if (asking < last) {
			ivChange.setImageResource(R.drawable.down);
		}
	}

	private void LoadPreferences() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this.getActivity());
		String market = preferences.getString("market", "0");
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
		/*
		 * textBTC = getActivity().getIntent().getExtras().getString("1");
		 * System.out.println(textBTC); textBTC =
		 * getActivity().getIntent().getStringExtra("1");
		 * System.out.println(textBTC); textBTC = (String)
		 * getActivity().getIntent().getExtras().get("1");
		 * System.out.println(textBTC); tvChange.setText(textBTC);
		 */

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

				onPriceUpdated(asking, 0);

				((MainActivity) getActivity()).setPrice(tvPrice.getText()
						.toString());
				/*
				 * String price = tvPrice.getText().toString();
				 * System.out.println(price);
				 * getActivity().getIntent().putExtra("0", price);
				 * System.out.println(price);
				 * getActivity().getIntent().putExtra("asking", price);
				 * System.out.println(price);
				 */

				low = Double.parseDouble(ticker.getLow().toString()
						.replace("USD ", ""));
				tvLow.setText("Low: " + String.format("%.2f", low));

				high = Double.parseDouble(ticker.getHigh().toString()
						.replace("USD ", ""));
				tvHigh.setText("High: " + String.format("%.2f", high));

				last = Double.parseDouble(ticker.getLast().toString()
						.replace("USD ", ""));
				tvLast.setText("Last: " + String.format("%.2f", last));

				volume = Double.parseDouble(ticker.getVolume().toString());
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

		Log.d("Position: ", "" + position);
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
		// getActivity().getIntent().putExtra("asking",
		// tvPrice.getText().toString());
		// System.out.println("" + tvPrice.getText().toString());
		// getActivity().getIntent().putExtra("0",
		// tvPrice.getText().toString());
		// System.out.println("" + tvPrice.getText().toString());
		// tvPrice.getText().toString());

		/*
		 * textBTC = getActivity().getIntent().getExtras().getString("1");
		 * System.out.println(textBTC); textBTC =
		 * getActivity().getIntent().getStringExtra("1");
		 * System.out.println(textBTC); textBTC = (String)
		 * getActivity().getIntent().getExtras().get("1");
		 * System.out.println(textBTC); tvBTC.setText(textBTC);
		 */
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

	/*
	 * // add items into spinner dynamically public void addItemsOnspinCur() {
	 * List<String> list = new ArrayList<String>(); list.add("USD");
	 * list.add("EUR"); list.add("JPY"); list.add("GBP"); ArrayAdapter<String>
	 * dataAdapter = new ArrayAdapter<String>( this.getActivity(),
	 * android.R.layout.simple_spinner_item, list); // Change look of Spinner
	 * with: //
	 * dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item
	 * ); spinCur.setAdapter(dataAdapter); }
	 * 
	 * 
	 * public void addListenerOnSpinnerItemSelection() {
	 * spinCur.setOnItemSelectedListener(new CustomOnItemSelectedListener()); }
	 * 
	 * // get the selected dropdown list value public void addListenerOnButton()
	 * {
	 * 
	 * Button btnSave = (Button) getActivity().findViewById(R.id.btnSave);
	 * 
	 * btnSave.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * Toast.makeText( getActivity(), "OnClickListener : " + "\nSpinner 2 : " +
	 * String.valueOf(spinCur.getSelectedItem()), Toast.LENGTH_SHORT).show();
	 * Log.d("Spin", spinCur.getSelectedItem().toString()); }
	 * 
	 * });
	 * 
	 * }
	 * 
	 * 
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
