package com.epam.hackfest.findme;

import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.Data;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.epam.hackfest.findme.domain.SearchResult;

public class SearchResultActivity extends Activity implements OnClickListener {

	private ListView listView;
	private Button buttonCall;
	private Button buttonAdd;
	private TextView textViewResult;
	private Button buttonRequest;
	private SearchResult searchResult;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String resp = getIntent().getExtras().getString("resp");
		searchResult = Utils.parseSearchResult(resp);
		if( searchResult.hasPhoneNumbers() && !searchResult.isPrivate() ){
			createWithResults();
		}else{
			createWithoutResults();
		}
		
	}
	
	private void createWithoutResults() {
		setContentView(R.layout.activity_search_no_result);
		
		this.textViewResult = (TextView)this.findViewById(R.id.textViewResult);
		this.buttonRequest = (Button)this.findViewById(R.id.buttonRequest);
		
		this.progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
		this.progressBar.setVisibility(View.INVISIBLE);
		
		if( searchResult.isPrivate() ){
			buttonRequest.setVisibility(View.VISIBLE);
			buttonRequest.setOnClickListener(this);
			textViewResult.setText("The phone number is private, you can send a request to the owner ask for access.");
		}else{
			textViewResult.setText("No result was found!");
			buttonRequest.setVisibility(View.INVISIBLE);
		}
		buttonRequest.setTextColor(Color.WHITE);
	}

	private void createWithResults(){
		
		setContentView(R.layout.activity_search_result);
		
		listView = (ListView)this.findViewById(R.id.listViewResult);
		listView.setAdapter(new ResultAdapter(this.getApplicationContext(), searchResult));
		
		this.textViewResult = (TextView)this.findViewById(R.id.textViewResult);
		this.buttonCall = (Button)this.findViewById(R.id.buttonCall);
		this.buttonAdd = (Button)this.findViewById(R.id.buttonAdd);
		textViewResult.setText("Current Number for "+searchResult.getUserName());
		
		buttonCall.setOnClickListener(this);
		buttonAdd.setOnClickListener(this);
		buttonCall.setTextColor(Color.WHITE);
		buttonAdd.setTextColor(Color.WHITE);
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
		if( progressBar.getVisibility() == View.VISIBLE )return;
		
		AsyncTask<String, Void, Object> task = new AsyncTask<String, Void, Object>(){
			
			@Override
			protected void onPostExecute(Object result) {
				super.onPostExecute(result);
				progressBar.setVisibility(View.GONE);
				buttonRequest.setText(R.string.request);
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
				buttonRequest.setText(R.string.sendingRequest);
			}

			@Override
			protected Object doInBackground(String... params) {
				return Utils.sendRequest(params[0]);
			}
		};
		task.execute(searchResult.getPhoneNumbers().get(0));
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
		if( !searchResult.hasPhoneNumbers() )return;
		String userName = searchResult.getUserName();
		String phone = searchResult.getPhoneNumbers().get(0);

		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		ContentResolver resolver = this.getContentResolver();
		ContentValues values = new ContentValues();
		long contactid = ContentUris.parseId(resolver.insert(uri, values));

		uri = Uri.parse("content://com.android.contacts/data");

		values.put("raw_contact_id", contactid);
		values.put(Data.MIMETYPE, "vnd.android.cursor.item/name");
		values.put("data1", userName);
		resolver.insert(uri, values);
		values.clear();

		values.put("raw_contact_id", contactid);
		values.put(Data.MIMETYPE, "vnd.android.cursor.item/phone_v2");
		values.put("data1", phone);
		resolver.insert(uri, values);
		values.clear();
		
		Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
		finish();
	}

	private void callNumber() {
		if( !searchResult.hasPhoneNumbers() )return;
		String phoneNumber = searchResult.getPhoneNumbers().get(0);
		
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
		startActivity(intent);
	}

	private class ResultAdapter extends BaseAdapter{

		private Context context;
		private List<String> phoneNumbers;

		public ResultAdapter(Context applicationContext, SearchResult searchResult) {
			this.context = applicationContext;
			this.phoneNumbers = searchResult.getPhoneNumbers();
		}

		@Override
		public int getCount() {
			return phoneNumbers.size();
		}

		@Override
		public Object getItem(int position) {
			return phoneNumbers.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.item_result, null);
			TextView textView = (TextView)view.findViewById(R.id.textViewItem);
			textView.setText(phoneNumbers.get(position));
			view.setBackgroundColor(Color.BLUE);
//			view.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					v.setBackgroundColor(Color.BLUE);
//				}
//			});
			return view;
		}
		
	}

}
