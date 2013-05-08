package com.cerner.yamba;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class DetailsActivity extends Activity {
	private DetailsFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		fragment = (DetailsFragment) getFragmentManager().findFragmentByTag(
				DetailsFragment.class.getSimpleName());

		if (fragment == null) {
			fragment = new DetailsFragment();
			getFragmentManager()
					.beginTransaction()
					.add(android.R.id.content, fragment,
							DetailsFragment.class.getSimpleName()).commit();
		}
		Log.d("DetailsActivity", "onCreate()");
	}

	@Override
	protected void onStart() {
		super.onStart();

		fragment.setId(getIntent().getLongExtra("id", -1));
	}
}
