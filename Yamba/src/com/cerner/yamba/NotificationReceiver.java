package com.cerner.yamba;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
	private static final String TAG = NotificationReceiver.class
			.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceived");

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new NotificationCompat.Builder(context)
				.setContentTitle("New Status")
				.setContentText("There is at least one new status.")
				.setSmallIcon(android.R.drawable.ic_dialog_alert).build();

		notificationManager.notify(StatusContract.NOTIFICATION_NEW_STATUS,
				notification);
	}

}
