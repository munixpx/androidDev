package com.example.munix.testbroadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {


    private BroadcastReceiver headsetReceiver = new HeadsetReceiver();
//    private BroadcastReceiver screenOnReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(context, "Screen On Motherfucker", Toast.LENGTH_SHORT).show();
//
//            Log.i("ScreenOn", "aasdasdasd");
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//    public void onClickSendBroadcast(View view) {
//
//        Intent broadcastIntent = new Intent("action.broadcast.tests");
//
//        Bundle extras = new Bundle();
//        extras.putString("name", "Davide");
//        extras.putInt("age", 27);
//
//        sendOrderedBroadcast(broadcastIntent, null, new MyFourthReciever(), null,
//                Activity.RESULT_OK, "boobies", extras);
//    }

    @Override
    protected void onResume() {
        super.onResume();

//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//        registerReceiver(screenOnReceiver, intentFilter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(headsetReceiver, intentFilter);
    }

    public void onClickReceiveBroadcast(View view) {


    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(headsetReceiver);
//        unregisterReceiver(screenOnReceiver);
    }

    public static class MyThirdBroadcast extends BroadcastReceiver{
        private String LOGTAG = MyThirdBroadcast.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(LOGTAG, "On Third broadcast!");
            Toast.makeText(context, "on 3rd Broadcast", Toast.LENGTH_SHORT).show();
            if (isOrderedBroadcast()) {
                int initCode = getResultCode();
                String initData = getResultData();
                Bundle initExtra = getResultExtras(true);

                Log.i(LOGTAG, "code: " + initCode + " Data: " + initData + " name: " + initExtra.getString("name"));

                initExtra.putString("name", "not Davide");
                setResult(initCode, initData, initExtra);
            }
        }
    }
}
