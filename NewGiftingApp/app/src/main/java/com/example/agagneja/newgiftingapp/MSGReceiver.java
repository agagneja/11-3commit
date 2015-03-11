package com.example.agagneja.newgiftingapp;

/**
 * Created by agagneja on 3/5/2015.
 */
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class MSGReceiver  extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("msg", extras.getString("msg"));
        msgrcv.putExtra("fromn", extras.getString("fromn"));
        msgrcv.putExtra("name",extras.getString("name"));
        msgrcv.putExtra("gift_id",extras.getString("gift_id"));
        Log.i("Received", extras.getString("gift_id"));
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
        ComponentName comp = new ComponentName(context.getPackageName(),MSGService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}