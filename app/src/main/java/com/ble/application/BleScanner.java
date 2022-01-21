package com.ble.application;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;

import java.lang.*;

import com.ble.application.utils.Utils;

/**
 * @author Asim Ali Khan
 * @version 1.0
 */


public class BleScanner {
    private MainActivity ma;

    private BluetoothAdapter bluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private int signalStrength;

    public String uuid;
    public int major_temp;
    public int minor_temp;
    public String major;
    public String minor;

    public static final char[] hexArray = "0123456789ABCDEF".toCharArray();

//    Constructor for initializing
    public BleScanner(MainActivity mainActivity, long scanPeriod, int signalStrength) {
        ma = mainActivity;

        mHandler = new Handler();
        this.signalStrength = signalStrength;

        final BluetoothManager bluetoothManager =
                (BluetoothManager) ma.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    public boolean isScanning() {
        return mScanning;
    }

//    Check whether the bluetooth of the device is on or not
//    if yes start scanning, else stop
    public void start() {
        if (!Utils.checkBluetooth(bluetoothAdapter)) {
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

//    Start scanning
    private void scanLeDevice(final boolean enable) {
        if (enable && !mScanning) {
            mScanning = true;
            bluetoothAdapter.startLeScan(mLeScanCallback);
            // If you want to scan for only specific types of peripherals,
            // you can instead call startLeScan(UUID[], BluetoothAdapter.LeScanCallback),
            // providing an array of UUID objects that specify the GATT services your app supports.

//            bluetoothAdapter.startLeScan(uuids, mLeScanCallback);
        }
        else {
            mScanning = false;
            bluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

//    Response of scanning devices and calculating major, minor and uuid of the device
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {

                    final int new_rssi = rssi;
                    if (rssi > signalStrength) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                int startByte = 2;
                                boolean patternFound = false;
                                while (startByte <= 5) {
                                    if (    ((int) scanRecord[startByte + 2] & 0xff) == 0x02 &&
                                            ((int) scanRecord[startByte + 3] & 0xff) == 0x15) {
                                        patternFound = true;
                                        break;
                                    }
                                    startByte++;
                                }

                                if (patternFound) {
                                    byte[] uuidBytes = new byte[16];
                                    System.arraycopy(scanRecord, startByte+4, uuidBytes, 0, 16);
                                    String hexString = bytesToHex(uuidBytes);

//                                    UUID parsing
                                    uuid =  hexString.substring(0,8) + "-" +
                                            hexString.substring(8,12) + "-" +
                                            hexString.substring(12,16) + "-" +
                                            hexString.substring(16,20) + "-" +
                                            hexString.substring(20,32);

//                                    Parsing major and minor values
                                    major_temp = (scanRecord[startByte+20] & 0xff) * 0x100 + (scanRecord[startByte+21] & 0xff);
                                    major = Integer.toString(major_temp);
                                    minor_temp = (scanRecord[startByte+22] & 0xff) * 0x100 + (scanRecord[startByte+23] & 0xff);
                                    minor = Integer.toString(minor_temp);

                                }
                                if(major != null && minor != null && uuid != null){
                                    ma.addDevice(device, new_rssi, major, minor, uuid);
                                }
//                                else {
//                                    ma.addDevice(device, new_rssi, "N/A","N/A","N/A");
//                                }
                            }
                        });
                    }
                }
    };

//    Conversion function
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
