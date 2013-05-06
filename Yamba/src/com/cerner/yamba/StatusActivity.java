package com.cerner.yamba;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class StatusActivity extends Activity {
	private static final String TAG = StatusActivity.class.getSimpleName();
	private Button updateButton;
	private EditText statusText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);

		// Find the views
		updateButton = (Button) findViewById(R.id.statusUpdateButton);
		statusText = (EditText) findViewById(R.id.statusText);

		// Setup button listener
		updateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String status = statusText.getText().toString();
				if(postToCloudTask!=null && !postToCloudTask.isCancelled()) {
					postToCloudTask.cancel(true);
				}
				postToCloudTask = new PostToCloudTask();
				postToCloudTask.execute(status);
				
				if (BuildConfig.DEBUG)
					Log.d(TAG, "onClicked with status: " + status);
			}

		});
	}

	PostToCloudTask postToCloudTask;
	@Override
	protected void onStop() {
		super.onStop();
		if(postToCloudTask!=null && !postToCloudTask.isCancelled()) {
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
		};
	};

}
