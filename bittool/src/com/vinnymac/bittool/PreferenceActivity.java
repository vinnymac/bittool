package com.vinnymac.bittool;

import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;

public class PreferenceActivity extends SherlockPreferenceActivity {

	public final static String TAG = PreferenceActivity.class.getSimpleName();
	
	private static final String KEY_MARKET = "market";
	private static final String KEY_CURRENCY = "currency";

	private static int prefs = R.xml.preferences;

	private static ListPreference market;
	private static ListPreference currency;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setHomeButtonEnabled(true);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			AddResourceApi11AndGreater();
		} else {
			AddResourceApiLessThan11();
		}
	}

	@SuppressWarnings("deprecation")
	protected void AddResourceApiLessThan11() {
		addPreferencesFromResource(prefs);

		market = (ListPreference) findPreference(KEY_MARKET);
		currency = (ListPreference) findPreference(KEY_CURRENCY);

		market.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				final String val = newValue.toString();
				int index = market.findIndexOfValue(val);

				switch (index) {
				case 0:
					currency.setEntries(R.array.mtgox);
					break;
				case 1:
					currency.setEntries(R.array.btce);
					break;
				case 2:
					currency.setEntries(R.array.bitstamp);
					break;
				case 3:
					currency.setEntries(R.array.campbx);
					break;
				case 4:
					currency.setEntries(R.array.bitcoincentral);
					break;
				default:
					currency.setEnabled(false);
					break;
				}

				return true;
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void AddResourceApi11AndGreater() {
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PF()).commit();
	}

	public static class PF extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(PreferenceActivity.prefs); // outer class
															// private
															// members seem
															// to be visible
															// for inner
															// class, and
															// making it
															// static made
															// things so
															// much easier
			market = (ListPreference) findPreference(KEY_MARKET);
			currency = (ListPreference) findPreference(KEY_CURRENCY);

			market.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				public boolean onPreferenceChange(Preference preference,
						Object newValue) {
					final String val = newValue.toString();
					int index = market.findIndexOfValue(val);

					switch (index) {
					case 0:
						currency.setEntries(R.array.mtgox);
						break;
					case 1:
						currency.setEntries(R.array.btce);
						break;
					case 2:
						currency.setEntries(R.array.bitstamp);
						break;
					case 3:
						currency.setEntries(R.array.campbx);
						break;
					case 4:
						currency.setEntries(R.array.bitcoincentral);
						break;
					default:
						currency.setEnabled(false);
						break;
					}

					return true;
				}
			});
		}
	}
}
