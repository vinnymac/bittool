package com.vinnymac.bittool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import br.com.dina.ui.model.BasicItem;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;

public class Notify extends Fragment implements OnClickListener {

	Button edit;
	Button add;

	UITableView tableView;

	int count = 0;

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

		tableView = (UITableView) view.findViewById(R.id.uITVAlerts);
		Log.d("Notify", "total items: " + tableView.getCount());

		CustomClickListener listener = new CustomClickListener();
		tableView.setClickListener(listener);

		tableView.commit();

	}

	private void createList() {
		BasicItem i1 = new BasicItem("New Alert");
		i1.setSubtitle("Click here to edit the alert");
		i1.setDrawable(R.drawable.ic_launcher);
		tableView.addBasicItem(i1);
		tableView.commit();

	}

	private int count() {
		count++;
		return count;
	}

	private class CustomClickListener implements ClickListener {

		@Override
		public void onClick(int index) {
			Toast.makeText(getActivity(), "item clicked: " + index,
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onClick(View arg0) {

		// TODO Auto-generated method stub

		// Intent in = new Intent();

		// Bundle basket = new Bundle();
		// basket.putString("key", bread);
		// i.putExtras(basket);

		switch (arg0.getId()) {
		case R.id.btnEdit:

			count++;
			if ((count % 2) == 1) {
				edit.setText("Done");
			} else {
				edit.setText("Edit");

				for (int i = 0; i < tableView.getHeight(); i++) {
					CheckBox ch = new CheckBox(getActivity().getBaseContext());
					ch.setText("I'm dynamic!");

					tableView.addView(ch);
				}

				Toast.makeText(Notify.this.getActivity().getBaseContext(),
						"Clicked Edit Button", Toast.LENGTH_LONG).show();
				// startActivity(i);
			}
			break;
		case R.id.btnAdd:

			createList();

			// tableView.addBasicItem("New List Item");
			Toast.makeText(Notify.this.getActivity().getBaseContext(),
					"Clicked Add Button", Toast.LENGTH_LONG).show();
			// Intent i = new Intent( Data.this, OpenedClass.class );

			// startActivityForResult(i, 0);
			break;
		}

	}
}
