package com.epam.hackfest.findme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class ResponseReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		String details = intent.getExtras().getString("request");
		Intent notificationIntent = new Intent(context, ProcessRequestActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		notificationIntent.putExtra("request", details);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
				.setContentIntent(pendingIntent)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("New Request")
				.setContentText(details);
		
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());
	}
}
