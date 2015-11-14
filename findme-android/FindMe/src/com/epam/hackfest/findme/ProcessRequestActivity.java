package com.epam.hackfest.findme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProcessRequestActivity extends Activity {

	private Button buttonAccept;
	private Button buttonIgnore;
	private TextView textViewDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.process_request_title);
		setContentView(R.layout.activity_process_request);
		
		this.textViewDetail = (TextView)this.findViewById(R.id.textViewRequest);
		this.buttonAccept = (Button)this.findViewById(R.id.buttonAccept);
		this.buttonIgnore = (Button)this.findViewById(R.id.buttonIgnore);
		
		textViewDetail.setText(Utils.getRequest().toString());
		
		buttonAccept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Request Accepted!", Toast.LENGTH_LONG).show();
				finish();
			}
		});
		
		buttonIgnore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
}
