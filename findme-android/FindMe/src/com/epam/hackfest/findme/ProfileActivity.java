package com.epam.hackfest.findme;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ProfileActivity extends Activity {

	private EditText editTextNumber;
	private CheckBox checkBoxPrivate;
	private Button buttonUpdate;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		this.editTextNumber = (EditText)this.findViewById(R.id.editTextPhoneNumber);
		this.checkBoxPrivate = (CheckBox)this.findViewById(R.id.checkBoxPrivate);
		this.buttonUpdate = (Button)this.findViewById(R.id.buttonUpdate);
		
		this.progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
		this.progressBar.setVisibility(View.INVISIBLE);
		
		buttonUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doUpdate();
			}
		});
		buttonUpdate.setTextColor(Color.WHITE);
	}

	protected void doUpdate() {
		if( !Utils.isValid(editTextNumber) ){
			return;
		}
		if( progressBar.getVisibility() == View.VISIBLE )return;
		
		String phone = editTextNumber.getText().toString();
		boolean isPrivate = checkBoxPrivate.isChecked();
		
		AsyncTask<String, Void, Object> task = new AsyncTask<String, Void, Object>(){
			
			@Override
			protected void onPostExecute(Object result) {
				super.onPostExecute(result);
				progressBar.setVisibility(View.GONE);
				buttonUpdate.setText(R.string.update);
				if( result instanceof Exception){
					Toast.makeText(getApplicationContext(), "Error: "+((Exception)result).getLocalizedMessage(), Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
					finish();
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressBar.setVisibility(View.VISIBLE);
				buttonUpdate.setText("Updating...");
			}

			@Override
			protected Object doInBackground(String... params) {
				String phone = params[0];
				String isPrivate = params[1];
				return Utils.update(phone, isPrivate);
			}
		};
		
		task.execute(phone, String.valueOf(isPrivate));
	}

}
