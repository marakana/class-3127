package com.cerner.yamba;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

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
        updateButton.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				String status = statusText.getText().toString();
				
				if(BuildConfig.DEBUG) Log.d(TAG, "onClicked with status: "+status);
			}
        	
        });
    }

}
