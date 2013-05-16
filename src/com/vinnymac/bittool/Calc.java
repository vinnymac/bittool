package com.vinnymac.bittool;

import com.vinnymac.bittool.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Calc extends Fragment {
	// OnCalcSelectedListener mCallback;

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
	double theId = -1;

	// double USD =
	// this.getActivity().getIntent().getExtras().getDouble("asking");

	/*
	 * public interface OnCalcSelectedListener { public void updateData(String
	 * price, String btc); }
	 * 
	 * @Override public void onAttach(Activity activity) { // TODO
	 * Auto-generated method stub super.onAttach(activity);
	 * 
	 * // This makes sure that the container activity has implemented // the
	 * callback interface. If not, it throws an exception try { mCallback =
	 * (OnCalcSelectedListener) activity; } catch (ClassCastException e) { throw
	 * new ClassCastException(activity.toString() +
	 * " must implement OnHeadlineSelectedListener"); } }
	 */

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
		
		textBTC = getActivity().getIntent().getStringExtra("1");
		textPrice = ((MainActivity) getActivity()).getPrice();
		// textBTC = ((MainActivity) getActivity()).getBTC();

		resultUSD.setText(textPrice);
		resultBTC.setText(textBTC);
		
		
		Log.d("ARGS: ", "" + getArguments().isEmpty());
		if (getArguments().containsKey("price") && !(getArguments().isEmpty())) {
		      theId = getArguments().getDouble("price");
		}
		resultUSD.setText("$ " + theId);

		// Buttons!
		one = (Button) view.findViewById(R.id.Btn1_id);
		two = (Button) view.findViewById(R.id.Btn2_id);
		three = (Button) view.findViewById(R.id.Btn3_id);

		four = (Button) view.findViewById(R.id.Btn4_id);
		five = (Button) view.findViewById(R.id.Btn5_id);
		six = (Button) view.findViewById(R.id.Btn6_id);

		seven = (Button) view.findViewById(R.id.Btn7_id);
		eight = (Button) view.findViewById(R.id.Btn8_id);
		nine = (Button) view.findViewById(R.id.Btn9_id);

		clear = (Button) view.findViewById(R.id.Btnclear_id);
		zero = (Button) view.findViewById(R.id.Btnzero_id);
		dot = (Button) view.findViewById(R.id.Btndot_id);

		/*
		 * plus = (Button) view.findViewById(R.id.Btnplus_id); minus = (Button)
		 * view.findViewById(R.id.Btnminus_id); divide = (Button)
		 * view.findViewById(R.id.Btndivide_id); multiply = (Button)
		 * view.findViewById(R.id.Btnmulti_id);
		 */
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
	
	public void setPrice(double price){
		resultUSD.setText("$ " + price);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		// Make sure that we are currently visible
		if (this.isVisible()) {
			
			//textPrice = ((MainActivity) getActivity()).getPrice();
			//resultUSD.setText(textPrice);
			
			/*
			double theId = -1;
			Bundle arguments = getArguments();
			if(arguments != null){
				System.out.println("Is sorta not broken.");
			}else{
				System.out.println("Broken");
			}
			
			Log.d("ARGS: ", "" + arguments.isEmpty());
			if (getArguments().containsKey("price") && !(getArguments().isEmpty())) {
			      theId = getArguments().getDouble("price");
			}
			resultUSD.setText("$ " + theId);*/
			
			// If we are becoming invisible, then...
			if (!isVisibleToUser) {
				Log.d("Calculator", "Not visible anymore.  Stopping calculator.");
				// TODO stop audio playback
			}
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//textPrice = ((MainActivity) getActivity()).getPrice();
		//resultUSD.setText(textPrice);
	}

	private void reset() {
		resultBTC.setText("0 BTC");
		resultUSD.setText("$ 0.00");
	}

	private void insert(int j) {
		String temp = resultBTC.getText().toString();
		temp = temp.replace(" BTC", "");
		Log.d("Temporary", temp);

		if (temp.equals("0")) {
			temp = "" + j;
		} else {
			temp = temp + j;
		}

		// gets the resultBTC string, removes the BTC, and sets total1 to the
		// double value.
		btcTotal = Double.valueOf(temp).doubleValue();
		Log.d("btcTotal", "" + btcTotal);

		resultBTC.setText(temp);
		resultBTC.append(" BTC");
		
		price = Double.valueOf(textPrice.replace("$ ", "")).doubleValue();
		double newPrice = theId * btcTotal;
		resultUSD.setText("$ " + String.format("%.2f", newPrice));
		

		// this.setArguments(((MainActivity) getActivity()).args);
		// ((MainActivity) getActivity()).args.putString("1",
		// resultBTC.getText().toString());
		// ((MainActivity)
		// getActivity()).setBTC(resultBTC.getText().toString());
		//getActivity().getIntent().putExtra("1", resultBTC.getText().toString());

	}

	private void perform() {
		// TODO Auto-generated method stub
		str = "";
		numtemp = num;
	}

	private void calculate() {
		// TODO Auto-generated method stub
		if (op == '+')
			num = numtemp + num;
		else if (op == '-')
			num = numtemp - num;
		else if (op == '/')
			num = numtemp / num;
		else if (op == '*')
			num = numtemp * num;
		// double total = num * USD;
		// resultUSD.setText(total + " USD");
		// Log.d("resultUSD", "" + total);
	}
}
