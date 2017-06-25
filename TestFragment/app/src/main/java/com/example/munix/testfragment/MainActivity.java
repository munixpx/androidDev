package com.example.munix.testfragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class MainActivity extends AppCompatActivity implements ColorListener{
    public String LOG_TAG = MainActivity.class.getSimpleName();
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getFragmentManager();

        if ( manager.findFragmentByTag("fragB")==null) {
            addFragmentB();
        }
        
        if ( manager.findFragmentByTag("fragA")==null){
            FragmentA fragA = new FragmentA();

            FragmentTransaction fragATrans = manager.beginTransaction();
            fragATrans.add(R.id.containerA, fragA, "fragA");
            fragATrans.commit();
        }


        Log.v(LOG_TAG, "onCreate!");

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
    public void updateColors(int r, int g, int b) {
        FragmentB fragB = (FragmentB) manager.findFragmentByTag("fragB");
        if (fragB == null){
            return;
        }

        fragB.updateBackground(r, g, b);
    }

//    public void sendDataToFragment(View view) {
//        if (manager.findFragmentByTag("fragB") == null) {
//            addFragmentB();
//            manager.executePendingTransactions();
//        }
//        FragmentB fragB = (FragmentB) manager.findFragmentByTag("fragB");
//        Log.v(LOG_TAG, "getting sum of: " + String.valueOf(firstNumber) + " " + String.valueOf(secondNumber));
//        fragB.getSum(firstNumber, secondNumber);
//
//    }

}
