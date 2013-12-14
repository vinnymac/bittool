package com.vinnymac.bittool;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class CalcFragment extends SherlockFragment {

	public final static String TAG = CalcFragment.class.getSimpleName();
	private static final String KEY_PRICE = "price";
	private static final String KEY_BTC = "btc";
	private static final String KEY_ASK = "ask";

	public String str = "";
	Button one, two, three, four, five, six, seven, eight, nine, clear, dot,
			zero;
	// minus, plus, equal, multiply, divide;

	Character op = 'q';
	int i, num, numtemp;
	private double btcTotal = 1.0;
	private double price = -1.0;

	TextView resultUSD, resultBTC;

	private String textPrice = "$ 0.00";
	private String textBTC;
	double askingPrice = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.calc, container, false);
		initialize(view);
		return view;
	}

	private void initialize(View view) {

		// TextViews
		resultUSD = (TextView) view.findViewById(R.id.tvResult_USD);
		resultBTC = (TextView) view.findViewById(R.id.tvResult_BTC);

		textBTC = getActivity().getIntent().getStringExtra(KEY_BTC);
		textPrice = ((MainActivity) getActivity()).getPrice();

		resultUSD.setText(textPrice);
		resultBTC.setText(textBTC);

		Log.d(TAG, "ARGS: " + getArguments().isEmpty());
		if (getArguments().containsKey(KEY_ASK)) {
			askingPrice = getArguments().getDouble(KEY_ASK);
		}
		resultUSD.setText("$ " + askingPrice);

		// Buttons!
		one = (Button) view.findViewById(R.id.button_number_one);
		two = (Button) view.findViewById(R.id.button_number_two);
		three = (Button) view.findViewById(R.id.button_number_three);

		four = (Button) view.findViewById(R.id.button_number_four);
		five = (Button) view.findViewById(R.id.button_number_five);
		six = (Button) view.findViewById(R.id.button_number_six);

		seven = (Button) view.findViewById(R.id.button_number_seven);
		eight = (Button) view.findViewById(R.id.button_number_eight);
		nine = (Button) view.findViewById(R.id.button_number_nine);

		clear = (Button) view.findViewById(R.id.button_clear);
		zero = (Button) view.findViewById(R.id.button_number_zero);
		dot = (Button) view.findViewById(R.id.button_dot);

		one.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				insert(1);
			}
		});

		two.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				insert(2);
			}
		});

		three.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				insert(3);
			}
		});

		four.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				insert(4);
			}
		});

		five.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				insert(5);
			}
		});

		six.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				insert(6);
			}
		});

		seven.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				insert(7);
			}
		});

		eight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				insert(8);
			}
		});

		nine.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				insert(9);
			}
		});

		zero.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				insert(0);
			}
		});

		clear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				reset();
			}
		});

		dot.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String temp = resultBTC.getText().toString();

				if (!(resultBTC.getText().toString().contains("."))
						|| !(resultBTC.getText().toString().equals(""))) {
					temp = temp.replace(" BTC", ". BTC");
					resultBTC.setText(temp);
				}

				// resultBTC.append(" BTC");
			}
		});
	}

	public void setPrice(double price) {
		resultUSD.setText("$ " + price);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		// Make sure that we are currently visible
		if (this.isVisible()) {

			// If we are becoming invisible, then...
			if (!isVisibleToUser) {
				Log.d(TAG,
						"Calculator: Not visible anymore.  Stopping calculator.");
				// TODO stop audio playback
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void reset() {
		resultBTC.setText("0 BTC");
		resultUSD.setText("$ 0.00");
	}

	private void insert(int j) {
		String temp = resultBTC.getText().toString();
		temp = temp.replace(" BTC", "");
		Log.e(TAG, "Temporary: " + temp);

		if (temp.equals("0")) {
			temp = String.valueOf(j);
		} else {
			temp = temp + j;
		}

		// gets the resultBTC string, removes the BTC, and sets total1 to the
		// double value.
		btcTotal = Double.valueOf(temp).doubleValue();
		Log.e(TAG, "btcTotal: " + btcTotal);

		resultBTC.setText(temp);
		resultBTC.append(" BTC");

		price = Double.valueOf(textPrice.replace("$ ", "")).doubleValue();
		double newPrice = askingPrice * btcTotal;
		resultUSD.setText("$ " + String.format("%.2f", newPrice));
	}

	private void perform() {
		str = "";
		numtemp = num;
	}

	private void doMath() {
		if (op == '+')
			num = numtemp + num;
		else if (op == '-')
			num = numtemp - num;
		else if (op == '/')
			num = numtemp / num;
		else if (op == '*')
			num = numtemp * num;
	}
}
