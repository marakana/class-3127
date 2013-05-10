package com.cerner.yambaservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.RemoteException;
import android.preference.PreferenceManager;

import com.cerner.yambalib.IYambaService;
import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class IYambaServiceImpl extends IYambaService.Stub {
	private Context context;
	private YambaClient cloud;

	public IYambaServiceImpl(Context context) {
		super();
		this.context = context;

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		prefs.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {

			@Override
			public void onSharedPreferenceChanged(SharedPreferences prefs,
					String key) {
				if ("username".equals(key) || "password".equals(key)) {
					cloud = null;
				}
			}
		});
	}

	private YambaClient getCloud() {
		if (cloud == null) {
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(context);
			String username = prefs.getString("username", "");
			String password = prefs.getString("password", "");

			cloud = new YambaClient(username, password);
		}
		return cloud;
	}

	@Override
	public void postStatus(String status, double latitude, double longitude)
			throws RemoteException {
		try {
			getCloud().postStatus(status, latitude, longitude);
		} catch (YambaClientException e) {
			e.printStackTrace();
		}

	}

}
