package com.cerner.yamba;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusFragment extends Fragment {
	private static final String TAG = StatusFragment.class.getSimpleName();
	private static final int DANGER_ZONE = 50;
	private Button updateButton;
	private EditText statusText;
	private TextView statusCount;
	private int originalColor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup root,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_status, null, false);

		// Find the views
		updateButton = (Button) view.findViewById(R.id.statusUpdateButton);
		statusText = (EditText) view.findViewById(R.id.statusText);
		statusCount = (TextView) view.findViewById(R.id.statusCount);
		originalColor = statusCount.getTextColors().getDefaultColor();

		// Setup button listener
		updateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String status = statusText.getText().toString();
				if (postToCloudTask != null && !postToCloudTask.isCancelled()) {
					postToCloudTask.cancel(true);
				}
				postToCloudTask = new PostToCloudTask();
				postToCloudTask.execute(status);

				if (BuildConfig.DEBUG)
					Log.d(TAG, "onClicked with status: " + status);
			}

		});

		statusText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				int count = 140 - s.length();
				statusCount.setText(Integer.toString(count));

				if (count < DANGER_ZONE) {
					statusCount.setTextColor(Color.RED);
				} else {
					statusCount.setTextColor(originalColor);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

		});

		return view;
	}

	private PostToCloudTask postToCloudTask;

	@Override
	public void onStop() {
		super.onStop();
		if (postToCloudTask != null && !postToCloudTask.isCancelled()) {
			postToCloudTask.cancel(true);
		}

		Log.d(TAG, "onStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	private class PostToCloudTask extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;

		// Executes on the UI thread
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(getActivity(), "Posting",
					"Please wait...");
		}

		// Executes on a non-UI thread
		@Override
		protected String doInBackground(String... params) {

			// Post to the cloud
			ContentValues values = new ContentValues();
			values.put(StatusContract.Columns.ID, -1);
			values.put(StatusContract.Columns.USER, "");
			values.put(StatusContract.Columns.MESSAGE, params[0]);
			values.put(StatusContract.Columns.CREATED_AT,
					System.currentTimeMillis());
			Uri ret;
			try {
				ret = getActivity().getContentResolver().insert(
						StatusContract.CONTENT_URI, values);
			} catch (RuntimeException e) {
				e.printStackTrace();
				return getString(R.string.statusFailedToPost);
			}

//			if( getActivity().getContentResolver().getType(ret) != StatusContract.TYPE_ITEM ) 
//				return "Error!";
			
			return (ret == null) ? getString(R.string.statusFailedToPost)
					: getString(R.string.statusSuccessfullyPosted);
		}

		// Executes on UI thread after doInBackground()
		protected void onPostExecute(String result) {
			Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
			statusText.setText("");
			dialog.dismiss();
		};
	};
}
