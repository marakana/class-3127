package com.cerner.yambadata;

import android.app.Activity;
import android.os.Bundle;

public class PrefsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			PrefsFragment fragment = new PrefsFragment();
			getFragmentManager()
					.beginTransaction()
					.add(android.R.id.content, fragment,
							PrefsFragment.class.getSimpleName()).commit();
		}
	}
}
