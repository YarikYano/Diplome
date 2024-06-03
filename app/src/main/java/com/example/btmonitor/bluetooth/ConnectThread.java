package com.example.btmonitor.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.btmonitor.App;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread {
    private final BluetoothAdapter btAdapter;
    private final BluetoothDevice device;
    private BluetoothSocket bluetoothSocket;
    private ReceiveThread rThread;
    private final ReceiveThread.ReceiveThreadListener listener;

    // SPP UUID service
    public static final String UUID_DATA = "00001101-0000-1000-8000-00805F9B34FB";

    public ConnectThread(BluetoothAdapter btAdapter, BluetoothDevice device, ReceiveThread.ReceiveThreadListener listener) {
        this.btAdapter = btAdapter;
        this.device = device;
        this.listener = listener;
        try {
            bluetoothSocket =  device.createRfcommSocketToServiceRecord(UUID.fromString(UUID_DATA));
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (ActivityCompat.checkSelfPermission(App.getAppInstance(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            try {
                btAdapter.cancelDiscovery();

               // if(BluetoothDevice.DEVICE_TYPE_LE == device.getType()){
                    bluetoothSocket.connect();
               // }
                rThread = new ReceiveThread(bluetoothSocket, listener);
                rThread.start();
                listener.onConnected();
                Log.d("MyLog", "Connected");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("MyLog", "Not Connected");
                closeConnection();
            }
        }
        else {
            Log.d("MyLog", "Not permission");
        }
    }

    public void closeConnection() {
        try {
            if(bluetoothSocket !=null){
                bluetoothSocket.close();
            }
            if(rThread!=null){
                rThread.close();
            }
            btAdapter.startDiscovery();
        } catch (IOException y) {
            y.printStackTrace();
        }
    }

    public ReceiveThread getRThread() {
        return rThread;
    }
}