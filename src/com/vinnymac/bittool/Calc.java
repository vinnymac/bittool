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
	//OnCalcSelectedListener mCallback;

	public String str = "";
	Button one, two, three, four, five, six, seven, eight, nine, clear, dot,
			zero;
	// minus, plus, equal, multiply, divide;

	Character op = 'q';
	int i, num, numtemp;
	private double total1 = 1.0;
	TextView resultUSD, resultBTC;

	private String textPrice;
	private String textBTC;

	// double USD =
	// this.getActivity().getIntent().getExtras().getDouble("asking");
	
	/*
	public interface OnCalcSelectedListener {
		public void updateData(String price, String btc);
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnCalcSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
	}*/

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

		/*
		 * Bundle args = this.getArguments(); String textPrice = (String)
		 * args.get("0"); System.out.println(textPrice);
		 * 
		 * args.putString("0", textPrice); this.setArguments(args);
		 */

		// Retrieve the string from the argument bundle that contains textBTC in
		// main. This is used for fragment communication.
		//textBTC = this.getArguments().getString("1");
		//textPrice = this.getArguments().getString("0");
		// this.getArguments().putString("1", resultBTC.getText().toString());
		
		//textPrice = ((MainActivity) getActivity()).getPrice();
		//textBTC = ((MainActivity) getActivity()).getBTC();
		
		textPrice = getActivity().getIntent().getExtras().getString("0");
		textBTC = getActivity().getIntent().getExtras().getString("1");
		

		// EditText
		resultUSD = (TextView) view.findViewById(R.id.tvResult_USD);
		resultBTC = (TextView) view.findViewById(R.id.tvResult_BTC);
		
		//textPrice = ((MainActivity) getActivity()).getPrice();
		//textBTC = ((MainActivity) getActivity()).getBTC();
		
		resultUSD.setText(textPrice);
		resultBTC.setText(textBTC);


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

		return view;
	}

	/*
	 * public void btn1Clicked(View v) { insert(1); }
	 * 
	 * public void btn2Clicked(View v) { insert(2);
	 * 
	 * }
	 * 
	 * public void btn3Clicked(View v) { insert(3);
	 * 
	 * }
	 * 
	 * public void btn4Clicked(View v) { insert(4);
	 * 
	 * }
	 * 
	 * public void btn5Clicked(View v) { insert(5);
	 * 
	 * }
	 * 
	 * public void btn6Clicked(View v) { insert(6); }
	 * 
	 * public void btn7Clicked(View v) { insert(7);
	 * 
	 * }
	 * 
	 * public void btn8Clicked(View v) { insert(8);
	 * 
	 * }
	 * 
	 * public void btn9Clicked(View v) { insert(9);
	 * 
	 * }
	 * 
	 * public void btnplusClicked(View v) { perform(); op = '+';
	 * 
	 * }
	 * 
	 * public void btnminusClicked(View v) { perform(); op = '-';
	 * 
	 * }
	 * 
	 * public void btndivideClicked(View v) { perform(); op = '/';
	 * 
	 * }
	 * 
	 * public void btnmultiClicked(View v) { perform(); op = '*';
	 * 
	 * }
	 * 
	 * public void btnequalClicked(View v) { calculate();
	 * 
	 * }
	 * 
	 * public void btnclearClicked(View v) { reset(); }
	 */

	private void reset() {
		// TODO Auto-generated method stub
		str = "";
		op = 'q';
		num = 0;
		numtemp = 0;
		resultBTC.setText("0 BTC");
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
		total1 = Double.valueOf(temp).doubleValue();
		Log.d("TOTAL1", "" + total1);

		resultBTC.setText(temp);
		resultBTC.append(" BTC");
		
		//this.setArguments(((MainActivity) getActivity()).args);
		//((MainActivity) getActivity()).args.putString("1", resultBTC.getText().toString());
		((MainActivity) getActivity()).setBTC(resultBTC.getText().toString());
		

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
