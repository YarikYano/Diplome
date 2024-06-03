package com.example.btmonitor.dialogs;

import android.bluetooth.BluetoothDevice;

public interface ItemClickListener {
    void onDeviceSelected(BluetoothDevice device);
}
