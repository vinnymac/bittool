package com.vinnymac.bittool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class Notify extends Fragment implements OnClickListener {

	Button edit;
	Button add;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.notify, container, false);
		initialize(view);

		return view;
	}

	private void initialize(View view) {

		edit = (Button) view.findViewById(R.id.btnEdit);
		add = (Button) view.findViewById(R.id.btnAdd);
		edit.setOnClickListener(this);
		add.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		
		// TODO Auto-generated method stub

		Intent i = new Intent();

		// Bundle basket = new Bundle();
		// basket.putString("key", bread);
		// i.putExtras(basket);

		switch (arg0.getId()) {
		case R.id.btnEdit:
			Toast.makeText(Notify.this.getActivity().getBaseContext(),
					"Clicked Edit Button", Toast.LENGTH_LONG).show();
			// startActivity(i);
			break;
		case R.id.btnAdd:
			Toast.makeText(Notify.this.getActivity().getBaseContext(),
					"Clicked Add Button", Toast.LENGTH_LONG).show();
			// Intent i = new Intent( Data.this, OpenedClass.class );

			// startActivityForResult(i, 0);
			break;
		}

	}

}
