package com.ble.application;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;

import com.ble.application.adapter.ListAdapterBleDevices;
import com.ble.application.model.BleDevice;
import com.ble.application.service.BleServices;
import com.ble.application.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, AdapterView.OnItemClickListener{

    private final static String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_ENABLE_BT = 1;
    public static final int BTLE_SERVICES = 2;

    private HashMap<String, BleDevice> mBTDevicesHashMap;
    private ArrayList<BleDevice> mBTDevicesArrayList;
    private ListAdapterBleDevices adapter;
    private ListView listView;

    private Button btn_Scan;

    private BroadcastReceiverBleState mBTStateUpdateReceiver;
    private BleScanner mBTLeScanner;
    private BluetoothAdapter bluetoothAdapter;
    private Handler mHandler;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// check whether the device is BLE compatible or not?
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Utils.toast(getApplicationContext(), "BLE not supported");
            finish();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so that it can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }

            if (this.checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Bluetooth");
                builder.setMessage("Turn on the Bluetooth please");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        bluetoothAdapter.enable();
//                        requestPermissions(new String[]{Manifest.permission.BLUETOOTH},
//                                PERMISSION_REQUEST_BLUETOOTH);

                    }
                });
                builder.show();
            }
        }

        mHandler = new Handler();
        // define the signal strength and also the time-span the signal are updated till
        mBTStateUpdateReceiver = new BroadcastReceiverBleState(getApplicationContext());
        mBTLeScanner = new BleScanner(this, 3000, -100);

        mBTDevicesHashMap = new HashMap<>();
        mBTDevicesArrayList = new ArrayList<>();

        // listing down all the possible BLE connections
        adapter = new ListAdapterBleDevices(this, R.layout.list_ble_device, mBTDevicesArrayList);

        listView = new ListView(this);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(this);

        btn_Scan = (Button) findViewById(R.id.btn_scan);
        ((ScrollView) findViewById(R.id.scrollView)).addView(listView);
        findViewById(R.id.btn_scan).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_scan:
//                Utils.toast(getApplicationContext(), "Scan Button Pressed");
                if (!mBTLeScanner.isScanning()) {
                    startScan();
                }
                else {
                    stopScan();
                }
                break;
            default:
                break;
        }
    }

    // start scanning if Bluetooth is on
    public void startScan(){
        btn_Scan.setText("Scanning...");

        mBTDevicesArrayList.clear();
        mBTDevicesHashMap.clear();
        mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                        mBTLeScanner.start();
                }
            }, 3000);
//        try {
//            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//            Method getUuidsMethod = BluetoothAdapter.class.getDeclaredMethod("getUuids", null);
//            ParcelUuid[] uuids = (ParcelUuid[]) getUuidsMethod.invoke(adapter, null);
//
//            if(uuids != null) {
//                for (ParcelUuid uuid : uuids) {
//                    Log.d(TAG, "UUID: " + uuid.getUuid().toString());
//                }
//            }else{
//                Log.d(TAG, "Uuids not found, be sure to enable Bluetooth!");
//            }
//
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
    }


    // add scanned devices
//    public void addDevice(BluetoothDevice device, int rssi, String major, String minor, String uuid, float distance, String proximity) {
    public void addDevice(BluetoothDevice device, int rssi, String major, String minor, String uuid) {
            String mac = "DD:34:02:05:62:A0";
        String address = device.getAddress();
        if (!mBTDevicesHashMap.containsKey(address)) {
//            if(address.equals(mac)){
//          if(device.getName() != null && address.equals(mac)){
             if(device.getName() != null){
                BleDevice btleDevice = new BleDevice(device);
                btleDevice.setRSSI(rssi);
                btleDevice.setUuids(uuid);
                btleDevice.setMajor(major);
                btleDevice.setMinor(minor);
//                btleDevice.setDistance(distance);
//                btleDevice.setProximity(proximity);

                mBTDevicesHashMap.put(address, btleDevice);
                mBTDevicesArrayList.add(btleDevice);
            }
        }
        else {
            mBTDevicesHashMap.get(address).setRSSI(rssi);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }

        }
    }

    // stop scanning after the defined time interval mentioned above
    public void stopScan() {
        btn_Scan.setText("Scan Again");
        mBTLeScanner.stop();
    }


    // if clicked on any device entry in the listview
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Context context = view.getContext();
        stopScan();

        String name = mBTDevicesArrayList.get(position).getName();
        String address = mBTDevicesArrayList.get(position).getAddress();

        Intent intent = new Intent(this, BleServices.class);
        intent.putExtra(BleServices.EXTRA_NAME, name);
        intent.putExtra(BleServices.EXTRA_ADDRESS, address);
        startActivityForResult(intent, BTLE_SERVICES);
    }

// check whether bluetooth is on or not
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        // Check which request we're responding to
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_ENABLE_BT) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
////                Utils.toast(getApplicationContext(), "Thank you for turning on Bluetooth");
//            } else if (resultCode == RESULT_CANCELED) {
//                Utils.toast(getApplicationContext(), "Please turn on Bluetooth");
//            }
//        } else if (requestCode == BTLE_SERVICES) {
//            // Do something
//        }
//    }

    //functions related to BLE Scan
    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mBTStateUpdateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

    }

    @Override
    protected void onResume() {
        super.onResume();
//        registerReceiver(mBTStateUpdateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(mBTStateUpdateReceiver);
        stopScan();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBTStateUpdateReceiver);
        stopScan();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}