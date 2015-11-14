package com.epam.hackfest.findme;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends Activity {

    private Button buttonSearch;
	private EditText editTextName;
	private EditText editTextPhone;
	private ProgressBar progressBarSearch;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.buttonSearch = (Button)this.findViewById(R.id.buttonSearch);
        this.editTextName = (EditText)this.findViewById(R.id.editTextName);
        this.editTextPhone = (EditText)this.findViewById(R.id.editTextPhoneNumber);
        this.progressBarSearch = (ProgressBar)this.findViewById(R.id.progressBarSearch);
        progressBarSearch.setVisibility(View.INVISIBLE);
        
        buttonSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = editTextName.getText().toString();
				String phone = editTextPhone.getText().toString();
				if( name == null || name.trim().length() == 0 ){
					editTextName.setError("Empty Name!");
					return;
				}
				if( phone == null || phone.trim().length() == 0 ){
					editTextPhone.setError("Empty Phone Number!");
					return;
				}
				doSearch(name, phone);
			}
		});
    }
    
	private void doSearch(String name, String phone) {
		
		AsyncTask<String, Void, Object> task = new AsyncTask<String, Void, Object>(){
			
			@Override
			protected void onPreExecute() {
				progressBarSearch.setVisibility(View.VISIBLE);
				super.onPreExecute();
			}

			@Override
			protected Object doInBackground(String... params) {
				String name = params[0];
				String phone = params[1];
				
				return Utils.search(name, phone);
			}

			@Override
			protected void onPostExecute(Object result) {
				progressBarSearch.setVisibility(View.INVISIBLE);
				super.onPostExecute(result);
				if( result instanceof String){
					String resp = (String)result;
					Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
					intent.putExtra("resp", resp);
					startActivity(intent);
				}else if( result instanceof Exception){
					Exception exp = (Exception)result;
					Toast.makeText(getApplicationContext(), "Error: "+exp.getLocalizedMessage(), Toast.LENGTH_LONG).show();
				}
			}
			
		};
		
		task.execute(name, phone);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
