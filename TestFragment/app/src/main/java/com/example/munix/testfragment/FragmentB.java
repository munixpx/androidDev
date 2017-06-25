package com.example.munix.testfragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FragmentB extends Fragment {
    public String LOG_TAG = FragmentB.class.getSimpleName();

    private LinearLayout palletteLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_b, container, false);

        palletteLayout = (LinearLayout) view.findViewById(R.id.palletteLayout);
        return view;
    }

    public void updateBackground(int rValue, int gValue, int bValue) {
        palletteLayout.setBackgroundColor(Color.rgb(rValue, gValue, bValue));
    }
}
