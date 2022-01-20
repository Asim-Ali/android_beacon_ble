package com.ble.application.model;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;

public class BleDevice {

    private BluetoothDevice bluetoothDevice;
    private int rssi;
    private String uuids;
    private String major;
    private String minor;
    private float distance;
    private String proximity;

    public BleDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getAddress() {
        return bluetoothDevice.getAddress();
    }

    public String getName() {
        return bluetoothDevice.getName();
    }

    public void setRSSI(int rssi) {
        this.rssi = rssi;
    }

    public int getRSSI() {
        return rssi;
    }

    public String getUuids() {
        return uuids;
    }

    public void setUuids(String uuids) {
        this.uuids = uuids;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getProximity() {
        return proximity;
    }

    public void setProximity(String proximity) {
        this.proximity = proximity;
    }
}
