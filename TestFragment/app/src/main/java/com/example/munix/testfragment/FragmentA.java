package com.example.munix.testfragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import static android.graphics.Color.rgb;

public class FragmentA extends Fragment {

    public String LOG_TAG = FragmentA.class.getSimpleName();

    private TextView colorText;
    private int rValue, bValue, gValue;
    private ToggleButton colorToggle;
    private int defaultTextColor, defaultToggleColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_a, container, false);

        colorText = (TextView) view.findViewById(R.id.txtColour);
        colorToggle = (ToggleButton) view.findViewById(R.id.toggleColour);

        defaultTextColor = colorText.getCurrentTextColor();
        defaultToggleColor = colorToggle.getCurrentTextColor();

        final SeekBar RedBar = (SeekBar) view.findViewById(R.id.barRed);
        final SeekBar GreenBar = (SeekBar) view.findViewById(R.id.barGreen);
        final SeekBar BlueBar = (SeekBar) view.findViewById(R.id.barBlue);

        RedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                rValue = RedBar.getProgress();
                colorText.setText(String.valueOf(rValue));
                updateColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        GreenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                gValue = GreenBar.getProgress();
                updateColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        BlueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                bValue = BlueBar.getProgress();
                updateColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        colorToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(colorToggle.isChecked()){
                    int resultantColor = Color.rgb(rValue, gValue, bValue);
                    colorText.setTextColor(resultantColor);
                    colorToggle.setHighlightColor(resultantColor);
                }else {
                    colorText.setTextColor(defaultTextColor);
                    colorToggle.setTextColor(defaultToggleColor);
                    colorToggle.setHighlightColor(defaultToggleColor);
                }
            }
        });

        rValue = RedBar.getProgress();
        gValue = GreenBar.getProgress();
        bValue = BlueBar.getProgress();
        updateColor();

        return view;
    }

    public void updateColor(){
        ColorListener colorListener = (ColorListener) getActivity();
        colorListener.updateColors(rValue, gValue, bValue);

        if(colorToggle.isChecked()){
            int resultantColor = rgb(rValue, gValue, bValue);
            colorText.setTextColor(resultantColor);
            colorToggle.setTextColor(resultantColor);
            colorToggle.setHighlightColor(resultantColor);
        }
    }
}
