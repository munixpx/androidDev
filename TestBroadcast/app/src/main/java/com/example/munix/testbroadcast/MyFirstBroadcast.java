package com.example.munix.testbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class MyFirstBroadcast extends BroadcastReceiver {
    private String LOGTAG = MyFirstBroadcast.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOGTAG, "On First Receiver");

        Toast.makeText(context, "on 1st Broadcast", Toast.LENGTH_SHORT).show();
    }
}
