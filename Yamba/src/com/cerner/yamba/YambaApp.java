package com.cerner.yamba;

import com.cerner.yambalib.StatusContract;

import android.app.Application;

public class YambaApp extends Application {
	public long latestTimestamp = 0;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		getContentResolver().getType(StatusContract.CONTENT_URI);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
