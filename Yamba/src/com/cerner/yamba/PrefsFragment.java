package com.cerner.yamba;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PrefsFragment extends PreferenceFragment {
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.prefs);
	}
}
