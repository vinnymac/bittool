package com.vinnymac.bittool;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class MainActivity extends SherlockFragmentActivity {

	private String textPrice = "$ 0.00";
	private String textBTC = "1 BTC";

	Bundle args = new Bundle();

	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager);
		setContentView(mViewPager);

		final ActionBar bar = getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(bar.newTab().setText("Notify"), Notify.class,
				null);
		mTabsAdapter.addTab(bar.newTab().setText("Exchange"),
				DetailFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText("Calculate"), Calc.class,
				null);

		args.putString("0", textPrice);
		args.putString("1", textBTC);

		
		mViewPager.setCurrentItem(1);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.about:
			about();
			return true;
		case R.id.menu_settings:
			settings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void about() {
		/* This is how you would do an Alert Dialog!
		 * new AlertDialog.Builder(this).setTitle("About")
				.setMessage("This is an AlertDialog!")
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).show();
		System.out.println("Hello About!");*/
		Intent about = new Intent("com.vinnymac.bittool.ABOUT");
		startActivity(about);
	}

	private void settings() {
		/*
		new AlertDialog.Builder(this).setTitle("Settings")
				.setMessage("This is an AlertDialog also!")
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).show();
		System.out.println("Hello Settings!");*/
		Intent pref = new Intent("com.vinnymac.bittool.QUICKPREF");
		startActivity(pref);
	}

	protected String getPrice() {
		return textPrice;
	}

	protected void setPrice(String price) {
		textPrice = price;
	}

	protected String getBTC() {
		return textBTC;
	}

	protected void setBTC(String btc) {
		textBTC = btc;
	}

}
