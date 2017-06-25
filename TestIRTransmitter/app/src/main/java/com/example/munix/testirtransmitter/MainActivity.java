package com.example.munix.testirtransmitter;

import android.app.Activity;
import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    ConsumerIrManager irManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        irManager = (ConsumerIrManager) this.getSystemService(Context.CONSUMER_IR_SERVICE);
    }

    public void onClickSendIRMessage(View view) {
        boolean hasIR = irManager.hasIrEmitter();
        Toast.makeText(this, "has IR: "+hasIR, Toast.LENGTH_SHORT).show();
    }
}
