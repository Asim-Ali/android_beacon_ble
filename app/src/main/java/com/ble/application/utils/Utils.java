package com.ble.application.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Gravity;
import android.widget.Toast;

import com.ble.application.MainActivity;
import com.ble.application.service.ServiceBleGatt;


/**
 * @author Asim Ali Khan
 * @version 1.0
 */


public class Utils {

    public static boolean checkBluetooth(BluetoothAdapter bluetoothAdapter) {

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return false;
        }
        else {
            return true;
        }
    }

    public static void requestUserBluetooth(Activity activity) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, MainActivity.REQUEST_ENABLE_BT);
    }

    public static IntentFilter makeGattUpdateIntentFilter() {

        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(ServiceBleGatt.ACTION_GATT_CONNECTED);
        intentFilter.addAction(ServiceBleGatt.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(ServiceBleGatt.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(ServiceBleGatt.ACTION_DATA_AVAILABLE);

        return intentFilter;
    }

    public static String hexToString(byte[] data) {
        final StringBuilder sb = new StringBuilder(data.length);

        for(byte byteChar : data) {
            sb.append(String.format("%02X ", byteChar));
        }

        return sb.toString();
    }

    public static int hasWriteProperty(int property) {
        return property & BluetoothGattCharacteristic.PROPERTY_WRITE;
    }

    public static int hasReadProperty(int property) {
        return property & BluetoothGattCharacteristic.PROPERTY_READ;
    }

    public static int hasNotifyProperty(int property) {
        return property & BluetoothGattCharacteristic.PROPERTY_NOTIFY;
    }

    public static void toast(Context context, String string) {

        Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        toast.show();
    }
}
