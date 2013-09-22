package com.vinnymac.bittool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class NotifyFragment extends SherlockFragment implements OnClickListener {

	private static final String TAG = NotifyFragment.class.getSimpleName();

	Button edit;
	Button add;

	int count = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
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
		edit = (Button) view.findViewById(R.id.button_edit);
		add = (Button) view.findViewById(R.id.button_add);
		edit.setOnClickListener(this);
		add.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.button_edit:
			Toast.makeText(getActivity(), "Clicked Edit Button",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.button_add:
			Toast.makeText(getActivity(), "Clicked Add Button",
					Toast.LENGTH_SHORT).show();
			break;
		}

	}
}
