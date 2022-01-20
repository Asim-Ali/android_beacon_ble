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

public class ListAdapterBleDevices extends ArrayAdapter<BleDevice> {

    Activity activity;
    int layoutResourceID;
    ArrayList<BleDevice> devices;

    public ListAdapterBleDevices(Activity activity, int resource, ArrayList<BleDevice> objects) {
        super(activity.getApplicationContext(), resource, objects);

        this.activity = activity;
        layoutResourceID = resource;
        devices = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResourceID, parent, false);
        }

        BleDevice device = devices.get(position);
        String name = device.getName();
        String address = device.getAddress();
        int rssi = device.getRSSI();

        TextView tv_name, tv_rssi, tv_address, tv_major, tv_minor, tv_uuid, tv_proximity, tv_distance;
        tv_name = convertView.findViewById(R.id.tv_name);
        tv_rssi = convertView.findViewById(R.id.tv_rssi);
        tv_address = convertView.findViewById(R.id.tv_macaddr);
        tv_major = convertView.findViewById(R.id.tv_major);
        tv_minor = convertView.findViewById(R.id.tv_minor);
        tv_uuid = convertView.findViewById(R.id.tv_uuid);
//        tv_distance = convertView.findViewById(R.id.tv_distance);
//        tv_proximity = convertView.findViewById(R.id.tv_proximity);

        if (name != null && name.length() > 0) {
            tv_name.setText(device.getName());
//            tv_distance.setText("Distance: " + device.getDistance() + "m");
//            tv_proximity.setText("Proximity: " + device.getProximity());
        }
        else {
            tv_name.setText("No Name");
        }
        tv_major.setText("Major: " + device.getMajor());
        tv_minor.setText("Minor: " + device.getMinor());
        tv_uuid.setText("UUID: " + device.getUuids());
        tv_rssi.setText("RSSI: " + Integer.toString(rssi) + "dBm");


        if (address != null && address.length() > 0) {
            tv_address.setText(device.getAddress());
        }
        else {
            tv_address.setText("No Address");
        }

        return convertView;
    }
}



