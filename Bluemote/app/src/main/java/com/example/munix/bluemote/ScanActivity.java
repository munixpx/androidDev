package com.example.munix.bluemote;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ScanActivity extends Activity {
    public String LOG_TAG = ScanActivity.class.getSimpleName();

    public static final String SCAN_RESULT = "scanResult";
    public static final String ACTION_SCAN = "action.custom.scanResultBroadcast";

    private ListView lvDevices;
    private ArrayAdapter<String> adDevices;
    private List<String> deviceNames = new ArrayList<>();
    private List<String> deviceAddresses = new ArrayList<>();

    private Boolean proceedDiscovery = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_devices);

        lvDevices = findViewById(R.id.devicesList);
        lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedDevice = adapterView.getItemAtPosition(i).toString();

                Intent intent = new Intent(ACTION_SCAN);
                intent.putExtra(SCAN_RESULT, selectedDevice);
                sendBroadcast(intent);
            }
        });

        // register for discovery
        IntentFilter deviceFound = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(scanReciever, deviceFound);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // get paired devices
        Set<BluetoothDevice> pairedDevices = MainActivity.bluetoothAdapter.getBondedDevices();

        Integer numPairedDevices = pairedDevices.size();
        if(numPairedDevices > 0){
            Log.d(LOG_TAG, "Num paired: "+numPairedDevices);

            deviceNames.clear();
            deviceAddresses.clear();
            for (BluetoothDevice device : pairedDevices){
                deviceNames.add(device.getName());
                deviceAddresses.add(device.getAddress());
            }

            adDevices = new ArrayAdapter<String>(this, R.layout.list_item_device,
                    R.id.deviceItem, getDevicesNames());
            lvDevices.setAdapter(adDevices);
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(scanReciever);
        super.onDestroy();
    }

    private String [] getDevicesNames(){
        String [] tmp = new String[deviceNames.size()];
        deviceNames.toArray(tmp);
        return tmp;
    }

    private String [] getDevicesAddresses(){
        String [] tmp = new String[deviceAddresses.size()];
        deviceAddresses.toArray(tmp);
        return tmp;
    }

    private void clearDevice(){
        adDevices = new ArrayAdapter<String>(this, R.layout.list_item_device,
                R.id.deviceItem, new String[]{});
        lvDevices.setAdapter(adDevices);
    }

    private void setDeviceNames(){
        adDevices = new ArrayAdapter<String>(this, R.layout.list_item_device,
                R.id.deviceItem, getDevicesNames());
        lvDevices.setAdapter(adDevices);
    }

    public void onClickScan(View view) {
        Toast.makeText(this, "scanning", Toast.LENGTH_SHORT).show();

        clearDevice();

        Log.d(LOG_TAG, "Scanning");
        deviceNames.clear();
        deviceAddresses.clear();
        MainActivity.bluetoothAdapter.startDiscovery();
    }

    private final BroadcastReceiver scanReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if ( device != null ) {
                    if ( device.getName() != null && device.getName() != null) {
                        Log.d(LOG_TAG, "Found device: " + device.getName());
                        deviceNames.add(device.getName());
                        deviceAddresses.add(device.getAddress());

                        setDeviceNames();
                    }
                }
            }
        }
    };
}
