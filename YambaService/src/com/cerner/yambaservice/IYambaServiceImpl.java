package com.cerner.yambaservice;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.RemoteException;
import android.preference.PreferenceManager;

import com.cerner.yambalib.IYambaService;
import com.cerner.yambalib.YambaStatus;
import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClient.Status;
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
	public boolean postStatus(String status, double latitude, double longitude)
			throws RemoteException {
		try {
			getCloud().postStatus(status, latitude, longitude);
			return true;
		} catch (YambaClientException e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public List<YambaStatus> getTimeline(int max) throws RemoteException {
		try {
			List<Status> timeline = getCloud().getTimeline(max);
			List<YambaStatus> ret = new ArrayList<YambaStatus>(max);
			for(Status status: timeline) {
				ret.add( new YambaStatus(status) );
			}
			return ret;
		} catch (YambaClientException e) {
			e.printStackTrace();
			return null;
		}
	}

}
