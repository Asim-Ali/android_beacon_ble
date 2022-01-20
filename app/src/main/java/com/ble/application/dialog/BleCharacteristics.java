package com.ble.application.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.ble.application.R;
import com.ble.application.service.ServiceBleGatt;

public class BleCharacteristics extends DialogFragment implements DialogInterface.OnClickListener {

    private String title;
    private ServiceBleGatt service;
    private BluetoothGattCharacteristic characteristic;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_ble_characteristic, null))
                .setNegativeButton("Cancel", this).setPositiveButton("Send", this);
        builder.setTitle(title);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        // Find a way to check which button as pressed cancel or ok
//            Utils.toast(activity.getApplicationContext(), "Button " + Integer.toString(which) + " Pressed");

        EditText edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.et_submit);

        switch (which) {
            case -2:
                // cancel button pressed
                break;
            case -1:
                // okay button pressed
                if (service != null) {
                    characteristic.setValue(edit.getText().toString());
                    service.writeCharacteristic(characteristic);
                }
                break;
            default:
                break;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setService(ServiceBleGatt service) {
        this.service = service;
    }

    public void setCharacteristic(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
    }
}