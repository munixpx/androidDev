package com.example.munix.bluemote;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;



public class MainActivity extends Activity {

    public static BluetoothAdapter bluetoothAdapter;

    private BluetoothStateReciever bluetoothStateReciever;
    private ScanReciever scanReciever;
    private static final Integer BLUETOOTH_ENABLE = 11;
    private static final Integer REQUEST_COARSE_LOCATION = 12;
    private boolean proceedDiscovery=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkLocationPermission();

        // get Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null){
            Toast.makeText(this, "Bluetooth not supoported on this device!", Toast.LENGTH_SHORT).show();
            return;
        }

        // make sure Bluetooth is enabled
        if (!bluetoothAdapter.isEnabled()){
            Intent bluetoothRequest = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(bluetoothRequest, BLUETOOTH_ENABLE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BLUETOOTH_ENABLE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "Bluetooth needs to be enabled for the application" +
                        "to work propertly", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        scanReciever = new ScanReciever();
        IntentFilter scanFilter = new IntentFilter(ScanActivity.ACTION_SCAN);
        registerReceiver(scanReciever, scanFilter);

        bluetoothStateReciever = new BluetoothStateReciever();
        IntentFilter bluetoothStateFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothStateReciever, bluetoothStateFilter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.scan){
            Toast.makeText(this, "scanning for devices", Toast.LENGTH_SHORT).show();

            Intent scanIntent = new Intent(this, ScanActivity.class);
            startActivity(scanIntent);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(scanReciever);
        unregisterReceiver(bluetoothStateReciever);
        super.onDestroy();
    }

    public void onClickOnButton(View view) {
        Toast.makeText(this, "Light on!", Toast.LENGTH_SHORT).show();

    }

    public void onClickOffButton(View view) {
        Toast.makeText(this, "Light off!", Toast.LENGTH_SHORT).show();
    }


    private class ScanReciever extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String selectedDevice = intent.getStringExtra(ScanActivity.SCAN_RESULT);

            Toast.makeText(context, "Selected: " + selectedDevice, Toast.LENGTH_SHORT).show();
        }
    }

    private class BluetoothStateReciever extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent bluetoothChangedIntent) {
            Bundle bundle = bluetoothChangedIntent.getExtras();
            Integer bluetoothState = (Integer) bundle.get(BluetoothAdapter.EXTRA_STATE);
            if (bluetoothState == null){
                return;
            }

            if (bluetoothState == BluetoothAdapter.STATE_OFF){
                Toast.makeText(context, "Bluetooth must be on for application to work", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void checkLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_COARSE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                proceedDiscovery = true;
            } else {
                proceedDiscovery = false;
            }
        }
    }
}
