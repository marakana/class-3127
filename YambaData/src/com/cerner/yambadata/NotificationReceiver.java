package com.cerner.yambadata;

import com.cerner.yamba.MainActivity;
import com.cerner.yambalib.StatusContract;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
	private static final String TAG = NotificationReceiver.class
			.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceived");

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent operation = PendingIntent.getActivity(context, 0,
				new Intent(context, MainActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT),
				PendingIntent.FLAG_UPDATE_CURRENT);

		@SuppressWarnings("deprecation")
		Notification notification = new Notification.Builder(context)
				.setContentTitle("New Status")
				.setContentText("There is at least one new status.")
				.setSmallIcon(android.R.drawable.ic_dialog_alert)
				.setAutoCancel(true).setContentIntent(operation)
				.getNotification();

		notificationManager.notify(StatusContract.NOTIFICATION_NEW_STATUS,
				notification);
	}

}
