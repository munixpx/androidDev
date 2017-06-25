package com.example.munix.testservices.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

public class MyMessengerService extends Service {

    private class IncomingHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:
                    Bundle bundle = msg.getData();
                    int firstNum = bundle.getInt("secondNum", 0);
                    int secondNum = bundle.getInt("firstNum", 0);
                    int result = operationAdd(firstNum, secondNum);

                    Message msgToActivity = Message.obtain(null, 11);
                    Bundle resultBunder = new Bundle();
                    resultBunder.putInt("result", result);
                    msgToActivity.setData(resultBunder);

                    try {
                        msg.replyTo.send(msgToActivity);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }

    Messenger myMessenger = new Messenger(new IncomingHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myMessenger.getBinder();
    }

    public int operationAdd(int a, int b) {
        return a + b;

    }
}