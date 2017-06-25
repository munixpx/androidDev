package com.example.munix.testservices.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;


public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int sleepTime = intent.getIntExtra("sleepTime", 1);

        int counter=1;
        while(counter <= sleepTime){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter++;
        }

        ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");
        Bundle returnBundle = new Bundle();
        returnBundle.putString("resultIntentService", "Counter stop at " + counter);
        resultReceiver.send(20, returnBundle);

    }
}
