package com.example.munix.bluemonster;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_scan, new ScanFragment())
                    .commit();
        }
    }

    public static class ScanFragment extends Fragment {
        private final String LOG_TAG = CommandFragment.class.getSimpleName();

        final int REQUEST_COARSE_LOCATION = 112;

        private ArrayAdapter<String> mScanAdapter;
        final ArrayList<BluetoothDevice> scannedDevices = new ArrayList<>();
        private ConnectThread mConnectThread;

        // Create a BroadcastReceiver for ACTION_FOUND
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    scannedDevices.add(device);
                    updateDevicesView(scannedDevices);
                }
                else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    Toast.makeText(getActivity(), "Scanning...", Toast.LENGTH_SHORT).show();
                    scannedDevices.clear();
                    updateDevicesView(scannedDevices);
                }
                else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    Toast.makeText(getActivity(), "Scan terminated", Toast.LENGTH_SHORT).show();
                }
            }
        };

        public ScanFragment() {
        }

        protected void checkLocationPermission() {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_COARSE_LOCATION);
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               String permissions[], int[] grantResults) {
            switch (requestCode) {
                case REQUEST_COARSE_LOCATION: {
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.v(LOG_TAG, "GRAAAAANTED!!");
                    } else {
                        Log.v(LOG_TAG, "WOOOPS NOT GRAAAAANTED!!");
                    }
                    break;
                }
            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);

            // Marshmallow requires location permissions
            checkLocationPermission();

            // Register the BroadcastReceiver
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            getActivity().registerReceiver(mReceiver, filter);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.scan, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_scan) {
                getBluetoothDevices(true);

                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_scan, container, false);

            String [] pairedDevicesArray = {};
            List<String> pairedList = new ArrayList<>(Arrays.asList(pairedDevicesArray));

            mScanAdapter = new ArrayAdapter<>(
                    getActivity(),
                    R.layout.list_item_scan,
                    R.id.list_item_scan_textview,
                    pairedList);
            ListView pairedView = (ListView) rootView.findViewById(R.id.listview_scan);
            pairedView.setAdapter(mScanAdapter);

            getBluetoothDevices(false);
            updateDevicesView(scannedDevices);

            pairedView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    String connectingString = String.format("connecting to \"%s\"",
                            mScanAdapter.getItem(pos));
                    Toast.makeText(getActivity(), connectingString, Toast.LENGTH_SHORT).show();
                }
            });
            return rootView;
        }

        public Boolean connectClient(BluetoothDevice device){
            mConnectThread = new ConnectThread(device);
            mConnectThread.start();

            return true;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            MainActivity.mBluetoothAdapter.cancelDiscovery();
            getActivity().unregisterReceiver(mReceiver);
        }

        private void getBluetoothDevices(Boolean scan){
            if (scan) {
                if (MainActivity.mBluetoothAdapter.isDiscovering()) {
                    MainActivity.mBluetoothAdapter.cancelDiscovery();
                }
                MainActivity.mBluetoothAdapter.startDiscovery();
            }
            else {
                Set<BluetoothDevice> pairedDevices = MainActivity.mBluetoothAdapter.getBondedDevices();
                scannedDevices.clear();
                for (BluetoothDevice device : pairedDevices){
                    scannedDevices.add(device);
                }
            }
        }

        public Boolean updateDevicesView(ArrayList<BluetoothDevice> devices){
            mScanAdapter.clear();
            for (BluetoothDevice device : devices) {
                Log.v(LOG_TAG, "ADDING: " + device.getName());
                Log.v(LOG_TAG, "UIDS: ");
                if (device.getUuids() != null) {
                    for (ParcelUuid x : device.getUuids()) {
                        Log.v(LOG_TAG, "UUUID: " + x.toString());
                    }
                }
                mScanAdapter.add(device.getName() + "\n" + device.getAddress());
            }
            return true;
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                Log.v("ConnectThread : ", "CONST: " + device.getName());
                // MY_UUID is the app's UUID string, also used by the server code
                if (device.getUuids() != null) {
                    for (ParcelUuid MY_UUID : device.getUuids()) {
                        tmp = device.createRfcommSocketToServiceRecord(MY_UUID.getUuid());
                    }
                }
            } catch (IOException e) {}
            mmSocket = tmp;
        }

        public void run() {

            Log.v("ConnectThread : ", "RUNNING!!! ");
            MainActivity.mBluetoothAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            Toast.makeText(getApplicationContext(), "Brindisi", Toast.LENGTH_SHORT).show();
            //manageConnectedSocket(mmSocket);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}
