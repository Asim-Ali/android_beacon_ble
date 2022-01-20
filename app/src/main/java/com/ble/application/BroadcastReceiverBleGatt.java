package com.ble.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ble.application.service.BleServices;
import com.ble.application.service.ServiceBleGatt;
import com.ble.application.utils.Utils;

public class BroadcastReceiverBleGatt extends BroadcastReceiver {

    private boolean mConnected = false;

    private BleServices activity;

    public BroadcastReceiverBleGatt(BleServices activity) {
        this.activity = activity;
    }

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device. This can be a
    // result of read or notification operations.

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (ServiceBleGatt.ACTION_GATT_CONNECTED.equals(action)) {
            mConnected = true;
        }
        else if (ServiceBleGatt.ACTION_GATT_DISCONNECTED.equals(action)) {
            mConnected = false;
            Utils.toast(activity.getApplicationContext(), "Disconnected From Device");
            activity.finish();
        }
        else if (ServiceBleGatt.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
            activity.updateServices();
        }
        else if (ServiceBleGatt.ACTION_DATA_AVAILABLE.equals(action)) {

//            String uuid = intent.getStringExtra(Service_BTLE_GATT.EXTRA_UUID);
//            String data = intent.getStringExtra(Service_BTLE_GATT.EXTRA_DATA);

            activity.updateCharacteristic();
        }

        return;
    }
}

