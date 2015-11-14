package com.epam.hackfest.findme;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class ProfileActivity extends Activity {

	private EditText editTextNumber;
	private CheckBox checkBoxPrivate;
	private Button buttonUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		this.editTextNumber = (EditText)this.findViewById(R.id.editTextPhoneNumber);
		this.checkBoxPrivate = (CheckBox)this.findViewById(R.id.checkBoxPrivate);
		this.buttonUpdate = (Button)this.findViewById(R.id.buttonUpdate);
		
		buttonUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doUpdate();
			}
		});
	}

	protected void doUpdate() {
		if( !Utils.isValid(editTextNumber) ){
			return;
		}
		
		String phone = editTextNumber.getText().toString();
		boolean isPrivate = checkBoxPrivate.isChecked();
		
		AsyncTask<String, Void, Object> task = new AsyncTask<String, Void, Object>(){
			
			@Override
			protected void onPostExecute(Object result) {
				super.onPostExecute(result);
				buttonUpdate.setText(R.string.update);
				if( result instanceof String ){
					
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				buttonUpdate.setText("Pending...");
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
