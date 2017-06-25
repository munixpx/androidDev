package com.example.munix.testbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;


public class HeadsetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getIntExtra("state", 0);

        if (state > 0){
            Toast.makeText(context, "Headset plugged", Toast.LENGTH_SHORT).show();

            int hasMicro = intent.getIntExtra("microphone", 0);
            String headsetName = intent.getStringExtra("name");
            if (hasMicro > 0){
                Toast.makeText(context, "yes microphonee", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "no microphone", Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(context, headsetName, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Headset unplugged", Toast.LENGTH_SHORT).show();
        }

        LocalBroadcastManager

    }
}
