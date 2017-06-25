package com.example.munix.testservices.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.munix.testservices.R;
import com.example.munix.testservices.services.MyMessengerService;


public class MyMessengerActivity extends AppCompatActivity {

    TextView txvResult;
    EditText txtFirstNum;
    EditText txtSecondNum;
    boolean isBinded = false;
    Messenger mService;


    Messenger incomingMessenger = new Messenger(new IncomingResponseHandler());

    private class IncomingResponseHandler extends Handler{
        @Override
        public void handleMessage(Message msgFromService) {
            switch (msgFromService.what){
                case 11:
                    Bundle resultData = msgFromService.getData();
                    int result = resultData.getInt("result");

                    txvResult.setText("Result " + result);

                    break;
                default:
                    super.handleMessage(msgFromService);
            }

        }
    }
    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            isBinded = true;
            mService = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBinded = false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calc);

        txvResult = (TextView) findViewById(R.id.txvResultCalc);
        txtFirstNum = (EditText) findViewById(R.id.txtFirstNum);
        txtSecondNum = (EditText) findViewById(R.id.txtSecondNum);
    }

    public void onClickCalc(View view) {

        int firstNum = Integer.valueOf(txtFirstNum.getText().toString());
        int secondNum = Integer.valueOf(txtSecondNum.getText().toString());

        Message msgToService = Message.obtain(null, 10);

        Bundle bundle = new Bundle();
        bundle.putInt("firstNum", firstNum);
        bundle.putInt("secondNum", secondNum);
        msgToService.setData(bundle);
        msgToService.replyTo = incomingMessenger;

        try {
            mService.send(msgToService);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent calcIntent = new Intent(this, MyMessengerService.class);
        bindService(calcIntent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isBinded){
            unbindService(mConnection);
        }
    }
}
