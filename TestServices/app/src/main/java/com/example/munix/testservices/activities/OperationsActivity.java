package com.example.munix.testservices.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.munix.testservices.services.OperationsService;
import com.example.munix.testservices.R;

public class OperationsActivity extends AppCompatActivity {

    boolean isBinded = false;
    private EditText txtOperandA, txtOperandB;
    private TextView txvResult;
    private OperationsService opertionsService;
    int operandA, operandB;

    public ServiceConnection operationConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            OperationsService.MyLocalBinder operationBinder = (OperationsService.MyLocalBinder) iBinder;
            opertionsService = operationBinder.getService();
            isBinded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBinded = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_operations);

        txtOperandA = (EditText) findViewById(R.id.txtOperandA);
        txtOperandB = (EditText) findViewById(R.id.txtOperandB);
        txvResult = (TextView) findViewById(R.id.txvResult);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent operationsIntent = new Intent(this, OperationsService.class);
        bindService(operationsIntent, operationConnection, BIND_AUTO_CREATE);
    }

    public void onClickOperation(View view) {
        operandA = Integer.valueOf(txtOperandA.getText().toString());
        operandB = Integer.valueOf(txtOperandB.getText().toString());

        int result = 0;
        switch (view.getId()){
            case R.id.btnAdd:
                Toast.makeText(this, "Adding", Toast.LENGTH_SHORT).show();
                result = opertionsService.operationAdd(operandA, operandB);
                break;
            case R.id.btnSub:
                Toast.makeText(this, "Subtracting", Toast.LENGTH_SHORT).show();
                result = opertionsService.operationSub(operandA, operandB);
                break;
            case R.id.btnMult:
                Toast.makeText(this, "Multiplying", Toast.LENGTH_SHORT).show();
                result = opertionsService.operationMult(operandA, operandB);
                break;
            case R.id.btnDiv:
                Toast.makeText(this, "Dividing", Toast.LENGTH_SHORT).show();
                result = opertionsService.operationDiv(operandA, operandB);
                break;
        }

        txvResult.setText("Result: " + result);
    }
}
