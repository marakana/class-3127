package com.cerner.yambadata;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cerner.yambalib.StatusContract;
import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClient.Status;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class RefreshService extends IntentService {
	private static final String TAG = RefreshService.class.getSimpleName();
	private long latestTimestamp = 0;

	public RefreshService() {
		super(TAG);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		latestTimestamp = PreferenceManager.getDefaultSharedPreferences(this)
				.getLong("latestTimestamp", 0);

		Log.d(TAG, "onCreated with latestTimestamp: " + latestTimestamp);
	}

	// Executes on a worker thread
	@Override
	public void onHandleIntent(Intent intent) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String username = prefs.getString("username", "");
		String password = prefs.getString("password", "");

		// Do we have username & password?
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			Toast.makeText(this, "Please set your username and password first",
					Toast.LENGTH_LONG).show();
			return;
		}

		Status lastestStatus = null;
		YambaClient cloud = new YambaClient(username, password);
		try {
			List<Status> timeline = cloud.getTimeline(20);
			for (Status status : timeline) {
				Log.d(TAG,
						String.format("%s: %s", status.getUser(),
								status.getMessage()));

				// Update latestTimestamp
				if (status.getCreatedAt().getTime() > latestTimestamp) {
					latestTimestamp = status.getCreatedAt().getTime();
					lastestStatus = status;
				}
			}

			Log.d(TAG, "ran: " + this.toString());
		} catch (YambaClientException e) {
			Log.e(TAG, "Failed to fetch timeline", e);
			e.printStackTrace();
		}

		if (lastestStatus != null) {
			sendBroadcast(new Intent(StatusContract.ACTION_NEW_STATUS));
			Log.d(TAG, "sent broadcast for lastest status created at: "
					+ lastestStatus.getCreatedAt());
		}

		Log.d(TAG, "onHandleIntent");
		return;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Save lastestTimestamp
		PreferenceManager.getDefaultSharedPreferences(this).edit()
				.putLong("latestTimestamp", latestTimestamp).apply();

		Log.d(TAG, "onDestroyed with latestTimestamp: " + latestTimestamp);
	}

}
