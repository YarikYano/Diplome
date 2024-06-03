package com.example.btmonitor.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReceiveThread extends Thread {
    private InputStream inputS;
    private OutputStream outputS;
    private final BluetoothSocket socket;
    private final ReceiveThreadListener listener;

    public interface ReceiveThreadListener {
        void onReadMessage(String data);
        void onError(Throwable th);

        void onConnected();
    }

    public ReceiveThread(BluetoothSocket socket,ReceiveThreadListener listener ) {
        this.socket = socket;
        this.listener = listener;
        try {
            inputS = socket.getInputStream();
            outputS = socket.getOutputStream();
        } catch (IOException ex) {
            listener.onError(ex);
        }
    }

    @Override
    public void run() {
        super.run();
        byte[] rBuffer = new byte[256];
        while (socket.isConnected()) {
            try {
                int size = inputS.read(rBuffer);
                if( size> 0) {
                    String message = new String(rBuffer, 0, size);
                    listener.onReadMessage(message);
                }
            } catch (IOException e) {
                break;
            }
        }
    }

    public void close() throws IOException {
        socket.close();
    }

    public void sendMessage(byte[] byteArray) throws IOException {
            outputS.write(byteArray);
    }
}
