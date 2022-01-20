package com.ble.application;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.lang.*;

import com.ble.application.utils.Utils;

public class BleScanner {
    private MainActivity ma;

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

    private long scanPeriod;
    private int signalStrength;

    public BleScanner(MainActivity mainActivity, long scanPeriod, int signalStrength) {
        ma = mainActivity;

        mHandler = new Handler();

        this.scanPeriod = scanPeriod;
        this.signalStrength = signalStrength;

        final BluetoothManager bluetoothManager =
                (BluetoothManager) ma.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    public boolean isScanning() {
        return mScanning;
    }

    public void start() {
        if (!Utils.checkBluetooth(mBluetoothAdapter)) {
            Utils.requestUserBluetooth(ma);
            ma.stopScan();
        }
        else {
            scanLeDevice(true);
        }
    }

    public void stop() {
        scanLeDevice(false);
    }

    // If you want to scan for only specific types of peripherals,
    // you can instead call startLeScan(UUID[], BluetoothAdapter.LeScanCallback),
    // providing an array of UUID objects that specify the GATT services your app supports.
    private void scanLeDevice(final boolean enable) {
        if (enable && !mScanning) {
//            Utils.toast(ma.getApplicationContext(), "Starting BLE scan...");

            // Stops scanning after a pre-defined scan period.

//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
////                    Utils.toast(ma.getApplicationContext(), "Stopping BLE scan...");
//
//                    mScanning = false;
//                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
//
////                    ma.startScan();
//                    ma.stopScan();
//                }
//            }, scanPeriod);
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
//            mBluetoothAdapter.startLeScan(uuids, mLeScanCallback);
        }
        else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

//    private Runnable stopScan = new Runnable() {
//        @Override
//        public void run() {
//            mBluetoothAdapter.stopLeScan(mLeScanCallback);
//            scanLeDevice(true);
//        }
//    };
//}

    public String uuid;
    public int major_temp;
    public int minor_temp;
    public String major;
    public String minor;
    public float distance;
    public String proximity;

    public static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {

                    final int new_rssi = rssi;
//                    if (rssi > signalStrength) {
//                        new android.os.Handler(Looper.getMainLooper()).postDelayed(
//                                new Runnable() {
//                                    public void run() {
////                                        float b = (-59 - (rssi))/(20);
////                                        distance = (float) Math.pow(10, b);
////
////                                        if(distance >= 3){
////                                            proximity = "Far";
////                                        }else{
////                                            proximity = "Near";
////                                        }
//                                        int startByte = 2;
//                                        boolean patternFound = false;
//                                        while (startByte <= 5) {
//                                            if (    ((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
//                                                    ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { //Identifies correct data length
//                                                patternFound = true;
//                                                break;
//                                            }
//                                            startByte++;
//                                        }
//
//                                        if (patternFound) {
//                                            byte[] uuidBytes = new byte[16];
//                                            System.arraycopy(scanRecord, startByte+4, uuidBytes, 0, 16);
//                                            String hexString = bytesToHex(uuidBytes);
//
//                                            //Here is your UUID
//                                            uuid =  hexString.substring(0,8) + "-" +
//                                                    hexString.substring(8,12) + "-" +
//                                                    hexString.substring(12,16) + "-" +
//                                                    hexString.substring(16,20) + "-" +
//                                                    hexString.substring(20,32);
//
//                                            major_temp = (scanRecord[startByte+20] & 0xff) * 0x100 + (scanRecord[startByte+21] & 0xff);
//                                            major = Integer.toString(major_temp);
//                                            minor_temp = (scanRecord[startByte+22] & 0xff) * 0x100 + (scanRecord[startByte+23] & 0xff);
//                                            minor = Integer.toString(minor_temp);
//
//                                        }
//                                         }
//                                },
//                                3000);
//
//                        if(major != null && minor != null && uuid != null){
////                                    ma.addDevice(device, new_rssi, major, minor, uuid, distance, proximity);
//
//                            ma.addDevice(device, new_rssi, major, minor, uuid);
//                        }
//                        else {
////                                    ma.addDevice(device, new_rssi, "N/A","N/A","N/A", 0, "N/A");
//                            ma.addDevice(device, new_rssi, "N/A","N/A","N/A");
//                        }
//                    }

                    if (rssi > signalStrength) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
//                                int N = 2;
                                float b = (-59 - (rssi))/(20);
                                distance = (float) Math.pow(10, b);

                                if(distance >= 3){
                                    proximity = "Far";
                                }else{
                                    proximity = "Near";
                                }
                                int startByte = 2;
                                boolean patternFound = false;
                                while (startByte <= 5) {
                                    if (    ((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                                            ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { //Identifies correct data length
                                        patternFound = true;
                                        break;
                                    }
                                    startByte++;
                                }

                                if (patternFound) {
                                    byte[] uuidBytes = new byte[16];
                                    System.arraycopy(scanRecord, startByte+4, uuidBytes, 0, 16);
                                    String hexString = bytesToHex(uuidBytes);

                                    //Here is your UUID
                                    uuid =  hexString.substring(0,8) + "-" +
                                            hexString.substring(8,12) + "-" +
                                            hexString.substring(12,16) + "-" +
                                            hexString.substring(16,20) + "-" +
                                            hexString.substring(20,32);

                                    major_temp = (scanRecord[startByte+20] & 0xff) * 0x100 + (scanRecord[startByte+21] & 0xff);
                                    major = Integer.toString(major_temp);
                                    minor_temp = (scanRecord[startByte+22] & 0xff) * 0x100 + (scanRecord[startByte+23] & 0xff);
                                    minor = Integer.toString(minor_temp);

                                }
                                if(major != null && minor != null && uuid != null){
//                                    ma.addDevice(device, new_rssi, major, minor, uuid, distance, proximity);
                                    ma.addDevice(device, new_rssi, major, minor, uuid);
                                }
//                                else {
////                                    ma.addDevice(device, new_rssi, "N/A","N/A","N/A", 0, "N/A");
//                                    ma.addDevice(device, new_rssi, "N/A","N/A","N/A");
//                                }

                            }
                        });
                    }
                }
            };


}
