package com.cerner.yamba;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsFragment extends Fragment {
	private static final String TAG = DetailsFragment.class.getSimpleName();
	private long id;
	TextView textUser, textMessage, textCreatedAt;
	ViewGroup container;
	AsyncTask<Long, Void, Boolean> loadDataTask;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup root,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.status_row, null, false);

		textUser = (TextView) view.findViewById(R.id.status_row_username);
		textMessage = (TextView) view.findViewById(R.id.status_row_message);
		textCreatedAt = (TextView) view.findViewById(R.id.status_row_createdAt);
		clearUI();

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateUI();
	}

	private void clearUI() {
		textUser.setText("");
		textMessage.setText("");
		textCreatedAt.setText("");
	}

	public void updateUI() {
		Log.d(TAG, "updateUI with id:" + id);
		if (id != -1) {
			new LoadDataTask().execute(id);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (loadDataTask != null) {
			loadDataTask.cancel(true);
			loadDataTask = null;
		}
	}

	public void setId(long id) {
		this.id = id;
		Log.d(TAG, "setId got id: " + id);
	}

	private final class LoadDataTask extends AsyncTask<Long, Void, Boolean> {
		Cursor cursor = null;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(getActivity(), "Loading...",
					"Please wait...");
			dialog.setCancelable(true);
		};

		@Override
		protected Boolean doInBackground(Long... params) {
			Uri uri = ContentUris.withAppendedId(StatusContract.CONTENT_URI,
					params[0]);
			cursor = getActivity().getContentResolver().query(uri, null, null,
					null, null);
			return (cursor != null && cursor.getCount() > 0) ? true : false;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			dialog.dismiss();

			if (success && cursor.moveToFirst()) {

				// Get the data from the cursor
				String user = cursor.getString(cursor
						.getColumnIndex(StatusContract.Columns.USER));
				String message = cursor.getString(cursor
						.getColumnIndex(StatusContract.Columns.MESSAGE));
				long createdAt = cursor.getLong(cursor
						.getColumnIndex(StatusContract.Columns.CREATED_AT));

				// Update the UI
				textUser.setText(user);
				textMessage.setText(message);
				textCreatedAt.setText(DateUtils
						.getRelativeTimeSpanString(createdAt));
			} else {
				Toast.makeText(getActivity(), "No data", Toast.LENGTH_LONG)
						.show();
			}
		};

	};
}
