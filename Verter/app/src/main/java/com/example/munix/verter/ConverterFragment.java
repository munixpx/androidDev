package com.example.munix.verter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;


public class ConverterFragment extends Fragment {


    private RelativeLayout mainLayout;

    public ConverterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_converter, container, false);

        View rootView = inflater.inflate(R.layout.fragment_converter, container, false);
        rootView.setBackgroundColor(getResources().getColor(R.color.colorBackground));

        final EditText inputText = (EditText) rootView.findViewById(R.id.inputValue);
        final EditText outputText = (EditText) rootView.findViewById(R.id.outputValue);

        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String conversionText="";
                if (inputText.getText().toString().length() > 0){
                    float conversionResult = Float.parseFloat(inputText.getText().toString()) * 0.72f;
                    conversionText = String.valueOf(conversionResult);
                }
                outputText.setText(conversionText);
            }
        });

        return rootView;
    }



}
