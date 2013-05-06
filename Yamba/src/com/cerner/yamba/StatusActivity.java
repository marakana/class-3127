package com.cerner.yamba;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class StatusActivity extends Activity {
	private static final String TAG = StatusActivity.class.getSimpleName();
	private static final int DANGER_ZONE = 50;
	private Button updateButton;
	private EditText statusText;
	private TextView statusCount;
	private int originalColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);

		// Find the views
		updateButton = (Button) findViewById(R.id.statusUpdateButton);
		statusText = (EditText) findViewById(R.id.statusText);
		statusCount = (TextView) findViewById(R.id.statusCount);
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
		
				if(count<DANGER_ZONE) {
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
	}

	PostToCloudTask postToCloudTask;

	@Override
	protected void onStop() {
		super.onStop();
		if (postToCloudTask != null && !postToCloudTask.isCancelled()) {
			postToCloudTask.cancel(true);
		}

		Log.d(TAG, "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	private class PostToCloudTask extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		
		// Executes on the UI thread
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(StatusActivity.this, "Posting", "Please wait...");
		}
		
		// Executes on a non-UI thread
		@Override
		protected String doInBackground(String... params) {

			// Post to the cloud
			YambaClient cloud = new YambaClient("student", "password");
			try {
				cloud.postStatus(params[0]);
				return getString(R.string.statusSuccessfullyPosted);
			} catch (YambaClientException e) {
				Log.e(TAG, "Failed to post", e);
				e.printStackTrace();
				return getString(R.string.statusFailedToPost);
			}
		}

		// Executes on UI thread after doInBackground()
		protected void onPostExecute(String result) {
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG)
					.show();
			statusText.setText("");
			dialog.dismiss();
		};
	};

}
