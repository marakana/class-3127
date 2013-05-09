package com.cerner.yambadata;

import com.cerner.yambalib.StatusContract;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class PrefsFragment extends PreferenceFragment {
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.prefs);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		prefs.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {

			@Override
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				if ("interval".equals(key)) {
					getActivity().sendBroadcast(
							new Intent(StatusContract.ACTION_PREFS_CHANGED));
				}
			}
		});
	}
}
