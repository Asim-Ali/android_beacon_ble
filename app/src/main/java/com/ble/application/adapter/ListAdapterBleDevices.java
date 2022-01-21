package com.ble.application.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ble.application.R;
import com.ble.application.model.BleDevice;

import java.util.ArrayList;

/**
 * @author Asim Ali Khan
 * @version 1.0
 */

public class ListAdapterBleDevices extends ArrayAdapter<BleDevice> {

    Activity activity;
    int layoutResourceID;
    ArrayList<BleDevice> bleDevices;

    public ListAdapterBleDevices(Activity activity, int resource, ArrayList<BleDevice> objects) {
        super(activity.getApplicationContext(), resource, objects);

        this.activity = activity;
        layoutResourceID = resource;
        bleDevices = objects;
    }

//    Setting fetched values for model on UI
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResourceID, parent, false);
        }

        TextView name, rssi, address, major, minor, uuid;
        name = convertView.findViewById(R.id.tv_name);
        rssi = convertView.findViewById(R.id.tv_rssi);
        address = convertView.findViewById(R.id.tv_macaddr);
        major = convertView.findViewById(R.id.tv_major);
        minor = convertView.findViewById(R.id.tv_minor);
        uuid = convertView.findViewById(R.id.tv_uuid);

        BleDevice device = bleDevices.get(position);
        String deviceName = device.getName();
        String deviceAddress = device.getAddress();
        int deviceRssi = device.getRSSI();

        if (deviceName != null && deviceName.length() > 0) {
            name.setText(device.getName());
        }
        else {
            name.setText("No Name");
        }
        major.setText("Major: " + device.getMajor());
        minor.setText("Minor: " + device.getMinor());
        uuid.setText("UUID: " + device.getUuid());
        rssi.setText("RSSI: " + Integer.toString(deviceRssi) + "dBm");

        if (deviceAddress != null && deviceAddress.length() > 0) {
            address.setText(device.getAddress());
        }
        else {
            address.setText("No Address");
        }

        return convertView;
    }
}



