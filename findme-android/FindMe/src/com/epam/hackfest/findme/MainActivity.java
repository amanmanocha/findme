package com.epam.hackfest.findme;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
				if( !Utils.isValid(editTextName) ){
					return;
				}
				if( !Utils.isValid(editTextPhone) ){
					return;
				}
				doSearch(name, phone);
			}
		});
        
        startPullService();
        
        registerReceiver();
    }
    
	private void registerReceiver() {
		IntentFilter broadcastFilter = new IntentFilter(Utils.REQUEST_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(new ResponseReceiver(), broadcastFilter);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if( requestCode == LoginActivity.LOGIN ){
			if( resultCode == RESULT_OK ){
				startPullService();
			}
		}
	}

    private void startPullService() {
    	if( !Utils.isSignedIn() ){
	    	Intent serviceIntent = new Intent(this, PullService.class);
	        startService(serviceIntent);
    	}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
    	if( Utils.isSignedIn() ){
    		menu.getItem(0).setVisible(true);
    		menu.getItem(1).setVisible(false);
    	}else{
    		menu.getItem(0).setVisible(false);
    		menu.getItem(1).setVisible(true);
    	}
    	return super.onPrepareOptionsMenu(menu);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_update) {
        	Intent intent = new Intent(this, ProfileActivity.class);
        	startActivity(intent);
            return true;
        }else if( id == R.id.action_sigin ){
        	Intent intent = new Intent(this, LoginActivity.class);
        	startActivityForResult(intent, LoginActivity.LOGIN);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
