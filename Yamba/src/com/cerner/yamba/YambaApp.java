package com.cerner.yamba;

import android.app.Application;

public class YambaApp extends Application {
	public long latestTimestamp = 0;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
