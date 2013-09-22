package com.vinnymac.bittool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity implements
		MarketFragment.Callbacks {

	public final static String TAG = MainActivity.class.getSimpleName();
	
	private static final String KEY_QUICKPREF = Constants.BUNDLE_KEY_ROOT + "QUICKPREF";
	private static final String KEY_PRICE = "price";
	private static final String KEY_BTC = "btc";
	
	private static final String FRAGMENT_ABOUT_DIALOG = "about_dialog";
	private static final String NOTIFY_FRAGMENT = "Notify";
	private static final String MARKET_FRAGMENT = "Exchange";
	private static final String CALCULATE_FRAGMENT = "Calculate";
	
	SharedPreferences prefs;

	private String textPrice = "$ 0.00";
	private String textBTC = "1 BTC";

	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;
	private ActionBar mActionBar;

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

		mActionBar = getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		getIntent().putExtra(KEY_PRICE, textPrice);
		getIntent().putExtra(KEY_BTC, textBTC);

		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(mActionBar.newTab().setText(NOTIFY_FRAGMENT), NotifyFragment.class, null);
		mTabsAdapter.addTab(mActionBar.newTab().setText(MARKET_FRAGMENT), MarketFragment.class, null);
		mTabsAdapter.addTab(mActionBar.newTab().setText(CALCULATE_FRAGMENT), CalcFragment.class, args);

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
			showAboutDialog();
			return true;
		case R.id.menu_settings:
			startActivitySettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showAboutDialog() {
		AboutDialogFragment fragment = (AboutDialogFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_ABOUT_DIALOG);
		if (fragment == null) {
			fragment = AboutDialogFragment.newInstance();
		}
		showDialogFragment(fragment, FRAGMENT_ABOUT_DIALOG);
	}

	private void startActivitySettings() {
		Intent pref = new Intent(KEY_QUICKPREF);
		startActivity(pref);
	}
	
	protected void showDialogFragment(SherlockDialogFragment dialog, String tag) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		dialog.show(ft, tag);
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
		super.onResume();
		mTabsAdapter.notifyDataSetChanged();
	}

	public void onUpdatedAskingPrice(double price) {
		Log.e(TAG, "Tag 0: " + mTabsAdapter.getTag(0));
		Log.e(TAG, "Tag 1: " + mTabsAdapter.getTag(1));
		Log.e(TAG, "Tag 2: " + mTabsAdapter.getTag(2));

		Bundle args = mTabsAdapter.getTag(2).getArgs();

		Log.d(TAG, "Price: " + price);
		args.putDouble(MarketFragment.KEY_ASK, price);
	}

}
