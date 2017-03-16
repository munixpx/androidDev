package com.example.munix.testfragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


public class MainActivity extends AppCompatActivity implements MyListener{
    public String LOG_TAG = MainActivity.class.getSimpleName();
    private FragmentManager manager;
    private int firstNumber=0, secondNumber=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getFragmentManager();

        if ( manager.findFragmentByTag("fragA")==null){
            FragmentA fragA = new FragmentA();

            FragmentTransaction fragATrans = manager.beginTransaction();
            fragATrans.add(R.id.containerA, fragA, "fragA");
            fragATrans.commit();
        }

        if ( manager.findFragmentByTag("fragB")==null) {
            addFragmentB();
        }
        Log.v(LOG_TAG, "onCreate!");

//        Button btnCalc = (Button) findViewById(R.id.btnSendToFragment);
//        btnCalc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }
    public FragmentB addFragmentB(){
        FragmentB fragB = new FragmentB();
        FragmentTransaction fragBTrans = manager.beginTransaction();
        fragBTrans.add(R.id.containerB, fragB, "fragB");
        fragBTrans.addToBackStack("AddFragB");
        fragBTrans.commit();

        return fragB;
    }

    @Override
    public void retrieveNumbers(int x, int y) {
        firstNumber=x;
        secondNumber=y;
    }

    public void sendDataToFragment(View view) {
        if (manager.findFragmentByTag("fragB") == null) {
            addFragmentB();
            manager.executePendingTransactions();
        }
        FragmentB fragB = (FragmentB) manager.findFragmentByTag("fragB");
        Log.v(LOG_TAG, "getting sum of: " + String.valueOf(firstNumber) + " " + String.valueOf(secondNumber));
        fragB.getSum(firstNumber, secondNumber);

    }

    @Override
    public void onBackPressed() {
        if(manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();
        }else{
            super.onBackPressed();
        }
    }

}
