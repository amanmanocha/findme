package com.epam.hackfest.findme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epam.hackfest.findme.domain.SearchResult;

public class SearchResultActivity extends Activity implements OnClickListener {

	private ListView listView;
	private Button buttonCall;
	private Button buttonAdd;
	private TextView textViewResult;
	private Button buttonRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String resp = getIntent().getExtras().getString("resp");
		SearchResult sr = Utils.parseSearchResult(resp);
		if( sr.hasPhoneNumbers() && !sr.isPrivate() ){
			createWithResults(sr);
		}else{
			createWithoutResults(sr);
		}
		
	}
	
	private void createWithoutResults(SearchResult sr) {
		setContentView(R.layout.activity_search_no_result);
		
		this.textViewResult = (TextView)this.findViewById(R.id.textViewResult);
		this.buttonRequest = (Button)this.findViewById(R.id.buttonRequest);
		
		if( sr.isPrivate() ){
			buttonRequest.setVisibility(View.VISIBLE);
			buttonRequest.setOnClickListener(this);
		}else{
			buttonRequest.setVisibility(View.INVISIBLE);
		}
	}

	private void createWithResults(SearchResult sr){
		
		setContentView(R.layout.activity_search_result);
		
		listView = (ListView)this.findViewById(R.id.listViewResult);
		listView.setAdapter(new ResultAdapter(this.getApplicationContext()));
		
		this.buttonCall = (Button)this.findViewById(R.id.buttonCall);
		this.buttonAdd = (Button)this.findViewById(R.id.buttonAdd);
		
		buttonCall.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		if( v == buttonCall ){
			callNumber();
		}else if( v == buttonAdd ){
			addNumberToContactList();
		}else if( v == buttonRequest ){
			requestDetails();
		}
	}

	private void requestDetails() {
		if( Utils.isSignedIn() ){
			sendRequest();
		}else{
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent, LoginActivity.LOGIN);
		}
	}
	
	private void sendRequest() {
		AsyncTask<Void, Void, Object> task = new AsyncTask<Void, Void, Object>(){
			
			@Override
			protected void onPostExecute(Object result) {
				super.onPostExecute(result);
				if( result instanceof Exception){
					buttonRequest.setTag(R.string.request);
					Toast.makeText(getApplicationContext(), "Error: "+((Exception)result).getLocalizedMessage(), Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
					finish();
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				buttonRequest.setText(R.string.sendingRequest);
			}

			@Override
			protected Object doInBackground(Void... params) {
				return Utils.sendRequest();
			}
		};
		task.execute();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if( requestCode == LoginActivity.LOGIN ){
			if( resultCode == RESULT_OK ){
				sendRequest();
			}
		}
	}

	private void addNumberToContactList() {
		
	}

	private void callNumber() {
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_result, menu);
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
	
	private class ResultAdapter extends BaseAdapter{

		private Context context;

		public ResultAdapter(Context applicationContext) {
			this.context = applicationContext;
		}

		@Override
		public int getCount() {
			return 1;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView = new TextView(context);
			textView.setText("Cool");
			return textView;
		}
		
	}

}
