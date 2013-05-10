package com.cerner.yambalib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class YambaManager {
	private static final String TAG = "YambaManager";
	private static final Intent SERVICE_INTENT = new Intent(
			StatusContract.ACTION_SERVICE);
	private static IYambaService yambaService;
	private Context context;

	public YambaManager(Context context) {
		super();
		this.context = context;

		boolean ret = context.bindService(SERVICE_INTENT, CONN,
				Context.BIND_AUTO_CREATE);
		Log.d(TAG, "YambaManager: " + ret);
	}

	private static final ServiceConnection CONN = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			yambaService = IYambaService.Stub.asInterface(service);
			Log.d(TAG, "onServiceConnected");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			yambaService = null;
			Log.d(TAG, "onServiceDisconnected");
		}
	};

	// --- Proxy Calls ---

	public void postStatus(String message, double latitude, double longitude) {
		if (yambaService == null) {
			Log.d(TAG, "postStatus without yambaService");
			return;
		}
		try {
			yambaService.postStatus(message, latitude, longitude);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
