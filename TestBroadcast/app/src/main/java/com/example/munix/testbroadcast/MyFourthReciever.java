package com.example.munix.testbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class MyFourthReciever extends BroadcastReceiver {

    private String LOGTAG = MyFourthReciever.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(LOGTAG, "on fourth!");
        if(isOrderedBroadcast()) {
            Bundle initExtra = getResultExtras(true);
            Log.i(LOGTAG, initExtra.getString("name"));
        }
        }
}
