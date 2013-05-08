package com.cerner.yamba;

import android.app.Fragment;
import android.util.Log;

public class DetailsFragment extends Fragment {
	private long id;
	
	public void setId(long id) {
		this.id = id;
		Log.d("DetailsFragment", "Got id: "+id);
	}
}
