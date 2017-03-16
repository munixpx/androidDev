package com.example.munix.testfragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FragmentA extends Fragment {

    public String LOG_TAG = FragmentA.class.getSimpleName();
    private EditText etFirstNumber;
    private EditText etSecondNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_a, container, false);

        etFirstNumber = (EditText) view.findViewById(R.id.etFirstNumber);
        etSecondNumber = (EditText) view.findViewById(R.id.etSecondNumber);
        Button btnSendToActivity = (Button) view.findViewById(R.id.btnSendToActivity);
        Log.v(LOG_TAG, "Button first: " + etFirstNumber.getText().toString());
        Log.v(LOG_TAG, "Button second: " + etSecondNumber.getText().toString());

        btnSendToActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyListener myListener = (MyListener) getActivity();

                int firstNumber = Integer.valueOf(etFirstNumber.getText().toString());
                int secondNumber = Integer.valueOf(etSecondNumber.getText().toString());
                Log.v(LOG_TAG, "first: " + String.valueOf(firstNumber));
                Log.v(LOG_TAG, "second: " + String.valueOf(secondNumber));

                myListener.retrieveNumbers(firstNumber, secondNumber);


            }
        });

        return view;
    }
}
