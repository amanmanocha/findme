package com.epam.hackfest.findme;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements OnClickListener {

	public static final int LOGIN = 0;
	
	private EditText editTextName;
	private EditText editTextPhone;
	private EditText editTextPassword;
	private Button buttonSignIn;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		this.editTextName = (EditText)this.findViewById(R.id.editTextName);
		this.editTextPhone = (EditText)this.findViewById(R.id.editTextPhoneNumber);
		this.editTextPassword = (EditText)this.findViewById(R.id.editTextPassword);
		this.progressBar = (ProgressBar)this.findViewById(R.id.progressBarSignIn);
		this.buttonSignIn = (Button)this.findViewById(R.id.buttonSignIn);
		
		buttonSignIn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		boolean ok = true;
		if( v == buttonSignIn ){
			ok = ok && checkEmpty(editTextName);
			ok = ok && checkEmpty(editTextPhone);
			ok = ok && checkEmpty(editTextPassword);
		}
		if( ok ){
			signIn();
		}
	}

	private void signIn() {
		String name = editTextName.getText().toString();
		String phone = editTextPhone.getText().toString();
		String pwd = editTextPassword.getText().toString();
		
		AsyncTask<String, Void, Object> task = new AsyncTask<String, Void, Object>(){
			
			@Override
			protected void onPostExecute(Object result) {
				super.onPostExecute(result);
				progressBar.setVisibility(View.INVISIBLE);
				if( result instanceof String ){
					
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			protected Object doInBackground(String... params) {
				String name = params[0];
				String phone = params[1];
				String pwd = params[2];
				return Utils.signIn(name, phone, pwd);
			}
		};
		
		task.execute(name, phone, pwd);
	}

	private boolean checkEmpty(EditText editText) {
		String str = editText.getText().toString();
		if( str == null || str.trim().length() == 0){
			editText.setError("This field is required");
			editText.requestFocus();
			return false;
		}
		return true;
	}

}
