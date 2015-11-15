package com.epam.hackfest.findme;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.EditText;

import com.epam.hackfest.findme.domain.Request;
import com.epam.hackfest.findme.domain.ResultState;
import com.epam.hackfest.findme.domain.SearchResult;

public class Utils {
	
	private final static String HOSTNAME = "http://192.168.2.38:8080/";
	private final static String UTF_8 = "UTF-8";
	public static final String REQUEST_BROADCAST = "REQUEST_BROADCAST";
	
	private static String userName;
	private static String phoneNumber;
	
	private static Request mRequest;

	private static Object post(String strUrl, Map<String, String> params){
		try{
	        URL url = new URL(strUrl);
	        StringBuilder postData = new StringBuilder();
	        if( params != null && params.size() > 0){
		        Iterator<Entry<String, String>> ite = params.entrySet().iterator();
		        while( ite.hasNext() ){
		        	Entry<String, String> param = ite.next();
		        	postData.append(URLEncoder.encode(param.getKey(), UTF_8))
		        		.append('=')
		        		.append(URLEncoder.encode(param.getValue(), UTF_8))
		        		.append('&');
		        }
		        if( postData.length() > 0 ){
		        	postData.deleteCharAt(postData.length()-1);
		        }
	        }
	        byte[] postDataBytes = postData.toString().getBytes(UTF_8);

	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
	        conn.setDoOutput(true);
	        conn.getOutputStream().write(postDataBytes);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF_8));
	        StringBuilder builder = new StringBuilder();
	        for (String line = null; (line = reader.readLine()) != null;) {
	            builder.append(line).append("\n");
	        }
	        reader.close();
	        conn.disconnect();
	        return builder.toString();
		}catch(Exception e){
			e.printStackTrace();
			return e;
		}
	}
	
	private static Object get(String strUrl, Map<String, String> params){
		try{
	        
	        StringBuilder sb = new StringBuilder(strUrl);
	        if( params != null && params.size() > 0 ){
	        	sb.append('?');
		        Iterator<Entry<String, String>> ite = params.entrySet().iterator();
		        while( ite.hasNext() ){
		        	Entry<String, String> param = ite.next();
		        	sb.append(URLEncoder.encode(param.getKey(), UTF_8))
		        		.append('=')
		        		.append(URLEncoder.encode(param.getValue(), UTF_8))
		        		.append('&');
		        }
		        if( sb.charAt(sb.length()-1) == '&' ){
		        	sb.deleteCharAt(sb.length()-1);
		        }
	        }
	        URL url = new URL(sb.toString());

	        URLConnection conn = url.openConnection();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF_8));
	        StringBuilder builder = new StringBuilder();
	        for (String line = null; (line = reader.readLine()) != null;) {
	            builder.append(line).append("\n");
	        }
	        reader.close();
	        return builder.toString();
		}catch(Exception e){
			e.printStackTrace();
			return e;
		}
	}
	
	public static Object signIn(String name, String phone, String pwd) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("phone", phone);
		params.put("pwd", pwd);
		String url = HOSTNAME+"signin";
		return post(url, params);
	}

	public static Object search(String name, String phone) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("phone", phone);
		if( phoneNumber != null ){
			params.put("curr", phoneNumber);
		}
		String url = HOSTNAME+"search";
		return get(url, params);
	}

	public static SearchResult parseSearchResult(String jsonString) {
		SearchResult sr = new SearchResult();
		try {
			JSONObject obj = new JSONObject(jsonString);
			if( obj.has("firstName") ){
				sr.setUserName(obj.getString("firstName")+" "+obj.getString("lastName"));
			}
			if( obj.has("currentPhoneNumber") ){
				String number = obj.getJSONObject("currentPhoneNumber").getString("number");
//				for( int i=0; i<array.length(); i++ ){
//					String phoneNumber = array.getString(i);
//					sr.addPhoneNumber(phoneNumber);
//				}
				sr.addPhoneNumber(number);
			}
			if( obj.has("currentNumberPublic") ){
				sr.setPrivate(!Boolean.parseBoolean(obj.getString("currentNumberPublic")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sr;
	}
	
	public static ResultState parseResultState(String jsonString) {
		ResultState resultState = new ResultState();
		try {
			JSONObject obj = new JSONObject(jsonString);
			if( obj.has("errorCode") ){
				resultState.setErrorCode(obj.getString("errorCode"));
			}
			
			if( obj.has("errorMessage") ){
				resultState.setErrorMessage(obj.getString("errorMessage"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return resultState;
	}

	public static Object sendRequest(String destPhoneNumber) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("dest", destPhoneNumber);
		String url = HOSTNAME+"request";
		return post(url, params);
	}
	
	public static Object updatePhoneNumbers(String currNumber, String... numbers ){
		HashMap<String, String> params = new HashMap<String, String>();
		if( numbers != null && numbers.length > 0){
			StringBuilder sb = new StringBuilder();
			for( int i=0; i<numbers.length; i++){
				sb.append(numbers[i]).append(',');
			}
			sb.deleteCharAt(sb.length()-1);
			params.put("old", sb.toString());
		}
		if( currNumber != null ){
			params.put("curr", currNumber);
		}
		String url = HOSTNAME+"update";
		return post(url, params);
	}

	public static boolean isSignedIn() {
		return userName != null && phoneNumber != null;
	}
	
	public static boolean isValid(EditText editText) {
		String str = editText.getText().toString();
		if( str == null || str.trim().length() == 0){
			editText.setError("This field is required");
			editText.requestFocus();
			return false;
		}
		return true;
	}

	public static Object update(String phone, String isPrivate) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("curr", phoneNumber);
		params.put("old", phone);
		params.put("isPrivate", isPrivate);
		
		String url = HOSTNAME+"update";
		return post(url, params);
	}

	public static void setSignInParams(String name, String phone) {
		userName = name;
		phoneNumber = phone;
	}

	public static Request pull() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("phone", phoneNumber);
		
		String url = HOSTNAME+"fetch";
		mRequest = null;
		Object result = get(url, params);
		if( result instanceof String){
			//[{"description":"","requestorPhone":{"number":"999"},"requesteePhone":{"number":"123"}}]
			String jsonString = (String)result;
			try {
				JSONArray array = new JSONArray(jsonString);
				if( array.length() > 0 ){
					Request request = new Request();
					JSONObject obj = array.getJSONObject(0);
					if( obj.has("requestorPhone") ){
						request.setFromPhoneNumber(obj.getJSONObject("requestorPhone").getString("number"));
					}else if( obj.has("requestorName") ){
						request.setFromUserName(obj.getString("requestorName"));
					}
					mRequest = request;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return mRequest;
	}
	
	public static Request getRequest(){
		return mRequest;
	}

	public static Object processRequest(String method, String phone) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("phone", phoneNumber);
		params.put("dest", phone);
		
		String url = HOSTNAME + method;
		return post(url, params);
	}

}
