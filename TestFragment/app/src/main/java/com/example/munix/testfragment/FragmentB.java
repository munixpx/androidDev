package com.example.munix.testfragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentB extends Fragment {
    public String LOG_TAG = FragmentB.class.getSimpleName();
    private TextView txtResult;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_b, container, false);

        txtResult = (TextView) view.findViewById(R.id.txtResult);
        if ( savedInstanceState != null){
            txtResult.setText(savedInstanceState.getString("text"));
        }

        return view;
    }

    public void getSum(int firstNumber, int secondNumber) {
        txtResult.setText("Result: " + (firstNumber + secondNumber));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("text", txtResult.getText().toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(LOG_TAG, "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(LOG_TAG, "onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy");
    }
}
