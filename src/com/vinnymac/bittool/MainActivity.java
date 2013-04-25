package com.vinnymac.bittool;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.vinnymac.bittool.TabsAdapter.TabInfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class MainActivity extends SherlockFragmentActivity implements
		Markets.Callbacks {

	SharedPreferences prefs;

	private String textPrice = "$ 0.00";
	private String textBTC = "1 BTC";

	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;
	private ActionBar bar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initialize();
	}

	private void initialize() {
		Bundle args = new Bundle();
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager);
		setContentView(mViewPager);

		bar = getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		this.getIntent().putExtra("0", textPrice);
		this.getIntent().putExtra("1", textBTC);

		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(bar.newTab().setText("Notify"), Notify.class, null);
		mTabsAdapter.addTab(bar.newTab().setText("Exchange"), Markets.class,
				null);
		mTabsAdapter.addTab(bar.newTab().setText("Calculate"), Calc.class, args);

		mViewPager.setCurrentItem(1);

		getSupportFragmentManager().beginTransaction().commit();

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
		/*
		 * This is how you would do an Alert Dialog! new
		 * AlertDialog.Builder(this).setTitle("About")
		 * .setMessage("This is an AlertDialog!") .setNeutralButton("OK", new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) { //
		 * TODO Auto-generated method stub
		 * 
		 * } }).show(); System.out.println("Hello About!");
		 */
		Intent about = new Intent("com.vinnymac.bittool.ABOUT");
		startActivity(about);
	}

	private void settings() {
		/*
		 * new AlertDialog.Builder(this).setTitle("Settings")
		 * .setMessage("This is an AlertDialog also!") .setNeutralButton("OK",
		 * new DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) { //
		 * TODO Auto-generated method stub
		 * 
		 * } }).show(); System.out.println("Hello Settings!");
		 */
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mTabsAdapter.notifyDataSetChanged();
	}

	public void onItemSelected(double id) {
		//Bundle args = new Bundle();
		 /*Bundle args = new Bundle(); args.putDouble(Markets.ARG_ASK_KEY, id);
		 
		 mTabsAdapter .addTab(bar.newTab().setText("Calculate"), Calc.class,
		 args);*/
		
		System.out.println("Tag 0: " + mTabsAdapter.getTag(0));
		System.out.println("Tag 1: " + mTabsAdapter.getTag(1));
		System.out.println("Tag 2: " + mTabsAdapter.getTag(2));
		 
		Bundle args = mTabsAdapter.getTag(2).getArgs();

		Log.d("ID in ACTIVITY: ", "" + id);
		args.putDouble(Markets.ARG_ASK_KEY, id);
		// ((Calc) mTabsAdapter.getItem(2)).setPrice(id);
		/*
		 //mViewPager.find
		TabInfo tag = mTabsAdapter.getTag(2);
		Calc frag = (Calc) getSupportFragmentManager().findFragmentByTag(tag.toString());
		
		 
		 //Calc frag = (Calc) mTabsAdapter.getItem(2);
		 frag.setArguments(args);*/
		 
		 
	}

}
