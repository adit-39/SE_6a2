package com.example.admin.parentapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static String TAG = "PARENT-APP";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        Log.i(TAG,"Inside intent Service");
        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                Log.i(TAG, "Completed work");
             // sendNotification("Received: " + extras.toString());
                Log.i(TAG, "Received: " + extras.toString());
                Log.i(TAG, "Launching Emergency Activity");

                //change availability status
//                String url;
//                LocationUpdates.mode=2;
//                if(LocationUpdates.mCurrentLocation!=null)
//                    url="http://104.199.137.20/api/ambulance/9591011896/"+LocationUpdates.mCurrentLocation.getLatitude()+"/"+LocationUpdates.mCurrentLocation.getLongitude()+"/"+LocationUpdates.mode;
//                else
//                    url="http://104.199.137.20/api/ambulance/9591011896/0.0/0.0/"+LocationUpdates.mode;
//                try {
//                    String content;
//                    HttpClient httpclient = new DefaultHttpClient();
//                    HttpResponse response = httpclient.execute(new HttpGet(url));
//                    String responseStr = EntityUtils.toString(response.getEntity());
//                    Log.i(TAG,responseStr);
//                    if(responseStr.equals("true"))
//                        Log.i(TAG,"Notification successful: "+LocationUpdates.mode);
//                } catch (Exception e) {
//                    Log.i(TAG, "Network exception", e);
//                }
//                sendNotification("Received: " + extras.toString());
//
               Intent i = new Intent(this,Emergency.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtras(extras);
               startActivity(i);

            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.i(TAG,"Attempting to send notification");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("EMERGENCY!!!")
                        .setSmallIcon(R.drawable.common_signin_btn_icon_dark)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        Log.i(TAG,"Finished building notification");
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}