package com.vinnymac.bittool;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class QuickPref extends PreferenceActivity {

	private static int prefs = R.xml.preferences;

	private static ListPreference market;
	private static ListPreference currency;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			getClass().getMethod("getFragmentManager");
			AddResourceApi11AndGreater();
		} catch (NoSuchMethodException e) { // Api < 11
			AddResourceApiLessThan11();
		}
	}

	@SuppressWarnings("deprecation")
	protected void AddResourceApiLessThan11() {
		addPreferencesFromResource(prefs);
		
		market = (ListPreference) findPreference("market");
		currency = (ListPreference) findPreference("currency");

		market.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				final String val = newValue.toString();
				int index = market.findIndexOfValue(val);
				
				switch(index){
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

	@TargetApi(11)
	protected void AddResourceApi11AndGreater() {
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PF()).commit();
	}

	@TargetApi(11)
	public static class PF extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(QuickPref.prefs); // outer class
															// private
															// members seem
															// to be visible
															// for inner
															// class, and
															// making it
															// static made
															// things so
															// much easier
			market = (ListPreference) findPreference("market");
			currency = (ListPreference) findPreference("currency");

			market.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				public boolean onPreferenceChange(Preference preference,
						Object newValue) {
					final String val = newValue.toString();
					int index = market.findIndexOfValue(val);
					
					switch(index){
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
