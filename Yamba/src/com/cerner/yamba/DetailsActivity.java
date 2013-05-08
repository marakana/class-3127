package com.cerner.yamba;

import android.app.Activity;
import android.os.Bundle;

public class DetailsActivity extends Activity {
	private DetailsFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			fragment = new DetailsFragment();
			getFragmentManager()
					.beginTransaction()
					.add(android.R.id.content, fragment,
							DetailsFragment.class.getSimpleName()).commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		fragment.setId(getIntent().getLongExtra("id", -1));
	}
}
