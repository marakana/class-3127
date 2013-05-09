package com.cerner.yambadata;

import java.util.List;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.cerner.yambalib.StatusContract;
import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClient.Status;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class StatusProvider extends ContentProvider {
	private static final String TAG = StatusProvider.class.getSimpleName();
	private SharedPreferences prefs;
	private String username, password;
	private YambaClient cloud;

	private static final UriMatcher MATCHER = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		MATCHER.addURI(StatusContract.AUTHORITY, StatusContract.PATH,
				StatusContract.STATUS_DIR);
		MATCHER.addURI(StatusContract.AUTHORITY, StatusContract.PATH + "/#",
				StatusContract.STATUS_ITEM);
	}

	@Override
	public boolean onCreate() {
		prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		username = prefs.getString(PrefsFragment.USERNAME, "");
		password = prefs.getString(PrefsFragment.PASSWORD, "");

		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			return false;
		} else {
			cloud = new YambaClient(username, password);
			return true;
		}

	}

	@Override
	public String getType(Uri uri) {
		switch (MATCHER.match(uri)) {
		case StatusContract.STATUS_DIR:
			return StatusContract.TYPE_DIR;
		case StatusContract.STATUS_ITEM:
			return StatusContract.TYPE_ITEM;
		default:
			throw new IllegalArgumentException("Invalid uri: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		if (MATCHER.match(uri) != StatusContract.STATUS_DIR) {
			throw new IllegalArgumentException("Invalid uri: " + uri);
		}

		try {
			String status = values.getAsString(StatusContract.Columns.MESSAGE);
			Double latitude = values
					.getAsDouble(StatusContract.Columns.LATITUDE);
			Double longitude = values
					.getAsDouble(StatusContract.Columns.LONGITUDE);
			if (latitude != null && longitude != null) {
				cloud.postStatus(status, latitude, longitude);
				Log.d(TAG, String.format("Posted %s (%f, %f)", status,
						latitude, longitude));
			} else {
				cloud.postStatus(status);
				Log.d(TAG, String.format("Posted %s", status));
			}

			// Notify content resolver that the data for this uri has changed
			getContext().getContentResolver().notifyChange(uri, null);

			return uri; // should really be returning uri with id of new record
		} catch (YambaClientException e) {
			Log.e(TAG, "Failed to post", e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	// content://com.cerner.yamba.provider/statuses/47
	// SELECT id, user, message, createdAt FROM statuses WHERE id = 47 ORDER BY
	// createdAt
	private static final int MAX_STATUSES = 20;

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		long id = -1;

		if (cloud == null)
			return null;

		switch (MATCHER.match(uri)) {
		case StatusContract.STATUS_DIR:
			break;
		case StatusContract.STATUS_ITEM:
			id = ContentUris.parseId(uri);
			break;
		default:
			throw new IllegalArgumentException("Invalid uri: " + uri);
		}

		try {
			List<Status> timeline = cloud.getTimeline((id == -1) ? MAX_STATUSES
					: 1000);
			MatrixCursor cursor = new MatrixCursor(
					StatusContract.Columns.NAMES, MAX_STATUSES);
			for (Status status : timeline) {
				if (id == -1) {
					cursor.newRow().add(status.getId()).add(status.getUser())
							.add(status.getMessage())
							.add(status.getCreatedAt().getTime());
				} else if (id == status.getId()) {
					cursor.newRow().add(status.getId()).add(status.getUser())
							.add(status.getMessage())
							.add(status.getCreatedAt().getTime());
					return cursor;
				}
			}

			// Register the cursor to watch changes to this uri
			cursor.setNotificationUri(getContext().getContentResolver(), uri);

			Log.d(TAG, "query returning records: " + cursor.getCount());
			return cursor;
		} catch (YambaClientException e) {
			Log.e(TAG, "Failed to fetch the timeline", e);
			e.printStackTrace();
			return null;
		}
	}

}
