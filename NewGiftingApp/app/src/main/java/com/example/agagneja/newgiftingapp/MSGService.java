package com.example.agagneja.newgiftingapp;

/**
 * Created by agagneja on 3/5/2015.
 */
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
public class MSGService extends IntentService {
    SharedPreferences prefs;
    NotificationCompat.Builder notification;
    NotificationManager manager;
    public MSGService() {
        super("MSGService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        prefs = getSharedPreferences("Gift", 0);
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.e("L2C","Error");
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.e("L2C","Error");
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                //if(!prefs.getString("CURRENT_ACTIVE","").equals(extras.getString("fromu"))) {

                Log.i("TAG", "Completed work @ " + SystemClock.elapsedRealtime());
                //sendNotification(extras.getString("msg"));
               sendNotification(extras.getString("msg"), extras.getString("name"),extras.getString("fromn"),extras.getString("gift_id"));
                //}
                Log.i("TAG", "Received: " + extras.getString("gift_id"));
            }
        }
        MSGReceiver.completeWakefulIntent(intent);
    }
    private void sendNotification(String msg,String name,String fromn,String gift_id) {
    //private void sendNotification(String msg)
    //{
        Bundle args = new Bundle();
       args.putString("fromn", fromn);
        args.putString("msg", msg);
        args.putString("name",name);
       args.putString("gift_id",gift_id);
        Intent chat = new Intent(this, Preview.class);
        chat.putExtra("INFO", args);
        notification = new NotificationCompat.Builder(this);
       // notification.setContentTitle(name);
        notification.setContentTitle(fromn);
        notification.setContentText(msg);
        notification.setTicker("New Message !");
        notification.setSmallIcon(R.drawable.ic_launcher);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1000,
                chat, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.setContentIntent(contentIntent);
        notification.setAutoCancel(true);
        manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }
}
