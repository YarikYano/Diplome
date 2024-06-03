package com.example.btmonitor.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class BtConnection {
    private final BluetoothAdapter btAdapter;
    private ConnectThread connectThread;

    ReceiveThread.ReceiveThreadListener listener;

    public BtConnection() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void setListener(ReceiveThread.ReceiveThreadListener listener) {
        this.listener = listener;
    }

    public void connect(String mac) {
        if (!btAdapter.isEnabled() || mac.isEmpty()) return;
        BluetoothDevice device = btAdapter.getRemoteDevice(mac);
        if (device == null) return;

        connectThread = new ConnectThread(btAdapter, device, listener);
        connectThread.start();
    }

    public void sendMessage(String message) {
        try {
            connectThread.getRThread().sendMessage(message.getBytes());
        } catch (Exception ex) {
            listener.onError(ex);
        }
    }
}