package com.example.munix.testservices.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class OperationsService extends Service {

    MyLocalBinder iBinder = new MyLocalBinder();

    public class MyLocalBinder extends Binder{
        public OperationsService getService(){
            return OperationsService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public int operationAdd(int a, int b){
        return a + b;
    }

    public int operationSub(int a, int b){
        return a - b;
    }

    public int operationMult(int a, int b){
        return a * b;
    }

    public int operationDiv(int a, int b){
        return a / b;
    }
}
