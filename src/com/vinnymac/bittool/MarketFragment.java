package com.vinnymac.bittool;

import java.text.DecimalFormat;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.vinnymac.bittool.Xchange.DataDownloadListener;
import com.xeiam.xchange.dto.marketdata.Ticker;

public class MarketFragment extends SherlockFragment {

	public final static String TAG = MarketFragment.class.getSimpleName();

	public final static String URL = "https://www.bitstamp.net/api/ticker/";
	public final static String CUR = "http://rate-exchange.appspot.com/currency?from=";
	public final static String filler = "&to=";
	public final static String KEY_TICKER = "ticker";
	

	private static final String KEY_MARKET = "market";
	private static final String KEY_CURRENCY = "currency";
	
	private static final String KEY_PRICE = "price";
	private static final String KEY_BTC = "btc";

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

	String ask = "No Market Data";

	public static final String KEY_ASK = "ask";

	double asking = -1.0; // USD Asking Price
	double low = -1.0; // High of Exchange

	double high = -1.0; // Low of Exchange
	double volume = -1.0; // Volume of Exchange
	double last = -1.0; // Last Price

	private MarketSession ticker;

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
		public void onUpdatedAskingPrice(double id);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mCallbacks = (Callbacks) activity;
		} catch (ClassCastException ex) {
			Log.e(TAG, "Casting the activity as a Callbacks listener failed" + ex);
			mCallbacks = null;
		}
	}

	public void onPriceUpdated(double price, int position) {
		if (mCallbacks != null) {
			mCallbacks.onUpdatedAskingPrice(price);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "Test hello");

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (getActivity().getIntent() != null) {
			ticker = getActivity().getIntent().getParcelableExtra(KEY_TICKER);
		}

		if (ticker == null) {
			ticker = new MarketSession();
			if (savedInstanceState != null) {
				if (savedInstanceState.containsKey(KEY_TICKER)) {
					ticker = savedInstanceState.getParcelable(KEY_TICKER);
				} else {
					ticker = new MarketSession();
				}
			} else {
				ticker = new MarketSession();
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
		outState.putParcelable(KEY_TICKER, ticker);
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
		/** THIS IS THE SETUP */

		preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		String market = preferences.getString(KEY_MARKET, "0");
		String[] marketNames = getResources().getStringArray(R.array.market);
		int position = Integer.parseInt(market);
		marketID.setText(marketNames[position]);

		// Default Currency
		// preferences.getInt(key, defValue);
		String currency = preferences.getString(KEY_CURRENCY, "0");
		String[] currencyNames = getResources()
				.getStringArray(R.array.currency);
		int pos = Integer.parseInt(currency);
		currency = currencyNames[pos];

		System.out.println("Success");
		System.out.println(ticker.toString());

		Log.d(TAG, "Currency before Double Conversion: " + currency.toString());

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

		Log.d(TAG, "Position: " + position);
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
		super.onResume();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		// Make sure that we are currently visible
		if (this.isVisible()) {
			// If we are becoming invisible, then...
			if (!isVisibleToUser) {
				Log.e("Market", "Not visible anymore.  Stopping market.");
				// TODO stop audio playback
			}
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
