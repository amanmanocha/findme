package com.epam.hackfest.findme;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProcessRequestActivity extends Activity {

	private Button buttonAccept;
	private Button buttonIgnore;
	private TextView textViewDetail;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.process_request_title);
		setContentView(R.layout.activity_process_request);
		
		this.textViewDetail = (TextView)this.findViewById(R.id.textViewRequest);
		this.buttonAccept = (Button)this.findViewById(R.id.buttonAccept);
		this.buttonIgnore = (Button)this.findViewById(R.id.buttonIgnore);
		
		this.progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
		this.progressBar.setVisibility(View.INVISIBLE);
		
		textViewDetail.setText(Utils.getRequest().toString());
		
		buttonAccept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				processRequest("accept");
			}

		});
		
		buttonIgnore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				processRequest("ignore");
			}
		});
	}
	
	private void processRequest(final String method) {
		if( progressBar.getVisibility() == View.VISIBLE )return;
		
		AsyncTask<String, Void, Object> task = new AsyncTask<String, Void, Object>(){
			
			@Override
			protected void onPostExecute(Object result) {
				super.onPostExecute(result);
				progressBar.setVisibility(View.GONE);
				if( result instanceof String ){
					String msg = "Request Accepted!";
					if( "ignore".equals(method) ){
						msg = "Request Ignored!";
					}
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
					finish();
				}else if( result instanceof Exception){
					Toast.makeText(getApplicationContext(), "Error: "+((Exception)result).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			protected Object doInBackground(String... params) {
				String method = params[0];
				String phone = params[1];
				return Utils.processRequest(method, phone);
			}
		};
		
		task.execute(method, Utils.getRequest().toString());
	}
	
}
