package com.epam.hackfest.findme;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.epam.hackfest.findme.domain.Request;

public class PullService extends IntentService {
	
	private Thread workThread;
	
	public PullService(){
		super("pull service");
	}
	
	@Override
    protected void onHandleIntent(Intent workIntent) {
//        String dataString = workIntent.getDataString();
		workThread = new Thread(){
			public void run(){
				while(!Thread.interrupted()){
					Request request = Utils.pull();
					if( request != null ){
						sendBroadcast(request);
					}
					try {
						sleep(10000);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
		};
		workThread.start();
	}
	
	private void sendBroadcast(Request request){
		Intent localIntent = new Intent(Utils.REQUEST_BROADCAST)
			.putExtra("request", request.toString());
	    LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

	@Override
	public void onDestroy() {
		workThread.interrupt();
		super.onDestroy();
	}
	
	
}
