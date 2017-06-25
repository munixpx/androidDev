package com.example.munix.testservices.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;



public class MyStartedService extends Service {
    public final String LOG_TAG = MyStartedService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(LOG_TAG, "onCreate, Thread name: " + Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(LOG_TAG, "onStartCommand, Thread name: " + Thread.currentThread().getName());

        new MyAsyncTask().execute(10);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "onBlind, Thread name: " + Thread.currentThread().getName());
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy, Thread name: " + Thread.currentThread().getName());
    }

    class MyAsyncTask extends AsyncTask<Integer, String, String> {
        public final String LOG_TAG = MyAsyncTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            int sleepTime = params[0];

            int counter=1;
            while(counter <= sleepTime){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress("count: " + counter);
                counter++;
            }
            return "counter stopped at " + counter + " seconds";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(MyStartedService.this, values[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);

            // destroy the service
            stopSelf();

            Intent intent = new Intent("action.service.to.activity");
            intent.putExtra("startServiceResult", str);
            sendBroadcast(intent);
        }

    }
}