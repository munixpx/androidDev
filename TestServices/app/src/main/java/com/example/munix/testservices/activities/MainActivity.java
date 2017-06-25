package com.example.munix.testservices.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.munix.testservices.services.MyIntentService;
import com.example.munix.testservices.services.MyStartedService;
import com.example.munix.testservices.R;

public class MainActivity extends AppCompatActivity {

    private TextView txvResultIntent, txvResultBroadcastIntent;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvResultIntent = (TextView) findViewById(R.id.txvResultIntent);
        txvResultBroadcastIntent = (TextView) findViewById(R.id.txvResultBroadcastIntent);
    }

    public void onStartServiceClick(View view) {
        Intent myService = new Intent(MainActivity.this, MyStartedService.class);
        startService(myService);
    }

    public void onStopServiceClick(View view) {
        Intent myService = new Intent(MainActivity.this, MyStartedService.class);
        stopService(myService);
    }


    public void onStartIntentServiceClick(View view) {

        ResultReceiver resultReceiver = new MyResultReceiver(null);

        Intent intent = new Intent(MainActivity.this, MyIntentService.class);
        intent.putExtra("sleepTime", 3);
        intent.putExtra("receiver", resultReceiver);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("action.service.to.activity");
        registerReceiver(myServiceBroadcastReciever, myIntentFilter);
    }

    private BroadcastReceiver myServiceBroadcastReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String counterStr = intent.getStringExtra("startServiceResult");
            txvResultBroadcastIntent.setText(counterStr);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(myServiceBroadcastReciever);
    }

    public void onClickCalculator(View view) {
        Intent openCalcIntent = new Intent(this, OperationsActivity.class);
        startActivity(openCalcIntent);
    }

    public void onClickAnotherCalculator(View view) {

        Intent calcIntent = new Intent(this, MyMessengerActivity.class);
        startActivity(calcIntent);
    }

    private class MyResultReceiver extends ResultReceiver{

        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            if(resultCode == 20 && resultData != null){
                final String resultCount = resultData.getString("resultIntentService");

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        txvResultIntent.setText(resultCount);
                    }
                });
            }
        }
    }
}
