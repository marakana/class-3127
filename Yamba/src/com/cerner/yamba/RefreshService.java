package com.cerner.yamba;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClient.Status;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class RefreshService extends IntentService {
	private static final String TAG = RefreshService.class.getSimpleName();

	public RefreshService() {
		super(TAG);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreated");
	}

	// Executes on a worker thread
	@Override
	public void onHandleIntent(Intent intent) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String username = prefs.getString(PrefsFragment.USERNAME, "");
		String password = prefs.getString(PrefsFragment.PASSWORD, "");

		// Do we have username & password?
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			Toast.makeText(this, "Please set your username and password first",
					Toast.LENGTH_LONG).show();
			return;
		}

		YambaClient cloud = new YambaClient(username, password);
		try {
			List<Status> timeline = cloud.getTimeline(20);
			for(Status status: timeline) {
				Log.d(TAG, String.format("%s: %s", status.getUser(), status.getMessage()) );
			}
			
		} catch (YambaClientException e) {
			Log.e(TAG, "Failed to fetch timeline", e);
			e.printStackTrace();
		}
		

		Log.d(TAG, "onHandleIntent");
		return;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroyed");
	}

}
