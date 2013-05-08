package com.cerner.yamba;

import android.net.Uri;
import android.provider.BaseColumns;

public final class StatusContract {
	private StatusContract() {
	}

	// General
	public static final String ACTION_NEW_STATUS = "com.cerner.yamba.NEW_STATUS";
	public static final int NOTIFICATION_NEW_STATUS = 42;

	// Content Provider
	public static final String AUTHORITY = "com.cerner.yamba.provider";
	public static final String PATH = "statuses";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + PATH);
	
	public static final int STATUS_ITEM = 1;
	public static final int STATUS_DIR = 2;
	
	public static final String TYPE_ITEM = "vnd.android.cursor.item/vnd.cerner.yamba.status";
	public static final String TYPE_DIR = "vnd.android.cursor.dir/vnd.cerner.yamba.status";
	
	public final static class Columns {
		private Columns() {}
		
		public static final String ID = BaseColumns._ID;
		public static final String USER = "user";
		public static final String MESSAGE = "message";
		public static final String CREATED_AT = "createdAt";
		
		public static final String[] NAMES = {ID, USER, MESSAGE, CREATED_AT};
	}

}
