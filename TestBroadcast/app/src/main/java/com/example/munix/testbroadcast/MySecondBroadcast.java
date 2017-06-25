package com.example.munix.testbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class MySecondBroadcast extends BroadcastReceiver {
    private String LOGTAG = MySecondBroadcast.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOGTAG, "On Second broadcast!");

        Toast.makeText(context, "on 2nd Broadcast", Toast.LENGTH_SHORT).show();
    }
}
