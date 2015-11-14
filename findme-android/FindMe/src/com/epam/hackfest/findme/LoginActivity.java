package com.epam.hackfest.findme;

import com.epam.hackfest.findme.domain.ResultState;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
		
		progressBar.setVisibility(View.INVISIBLE);
		buttonSignIn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if( v == buttonSignIn ){
			if( !Utils.isValid(editTextName) )return;
			if( !Utils.isValid(editTextPhone) )return;
			if( !Utils.isValid(editTextPassword) )return;
			
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
					ResultState rs = Utils.parseResultState(result.toString());
					if( "0".equals(rs.getErrorCode()) ){
						Intent returnIntent = new Intent();
						String name = editTextName.getText().toString();
						String phone = editTextPhone.getText().toString();
						Utils.setSignInParams(name, phone);
						setResult(Activity.RESULT_OK,returnIntent);
						finish();
					}else{
						Toast.makeText(getApplicationContext(), "Signin Failed: "+rs.getErrorCode(), Toast.LENGTH_LONG).show();
					}
				}else if( result instanceof Exception ){
					Toast.makeText(getApplicationContext(), "Signin Failed: "+((Exception)result).getLocalizedMessage(), Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getApplicationContext(), "Signin Failed", Toast.LENGTH_LONG).show();
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

}
