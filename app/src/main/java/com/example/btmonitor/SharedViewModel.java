package com.example.btmonitor;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.btmonitor.bluetooth.BtConnection;
import com.example.btmonitor.bluetooth.ReceiveThread;
import com.example.btmonitor.utills.PrefUtils;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class SharedViewModel extends ViewModel implements ReceiveThread.ReceiveThreadListener {
    private final MutableLiveData<Boolean> isSafeLocked = new MutableLiveData<>(false);
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> selectedDeviceLiveData = new MutableLiveData<>();
    private String selectedDevice;
    private final BtConnection btConnection;
    private boolean state;
    private boolean deviceConnected;

    private final MutableLiveData<Pair<String, Boolean>> deviceState = new MutableLiveData<>(new Pair<>("", false));

    public SharedViewModel() {
        btConnection = new BtConnection();
        btConnection.setListener(this);
    }

    public void sendMessage(String password, boolean isSafeUnlocked) {
        String data = String.format("%s%s", password, (isSafeUnlocked ? "ON" : "OFF"));
        CRC32 crc32 = new CRC32();
        crc32.reset();
        crc32.update(data.getBytes(StandardCharsets.UTF_8));
        btConnection.sendMessage(String.format("%s%s", data, crc32.getValue()));
    }

    public void connect() {
       /* BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = btAdapter.getRemoteDevice(selectedDevice);
        try {
            BluetoothSocket btSocket = BtUtlis.createBluetoothSocket(device);

            btSocket.connect();

             thread = new ConnectedThread(btSocket, new ConnectedThread.ConnectedThreadListener() {
                @Override
                public void onDataRead(String data) {
                    Log.d("TAG", "data recived " + data);
                }

                @Override
                public void onDataSent() {
                    Log.d("TAG", "data sended");
                }

                @Override
                public void onDataSentError(Exception exception) {
                    exception.printStackTrace();
                }
            });
            thread.start();
        } catch (IOException e) {

        }*/


        btConnection.connect(PrefUtils.getMac());

    }

    public void setDevice(String device) {
        selectedDevice = device;
        selectedDeviceLiveData.setValue(device);
    }

    public void toggle() {
        toggle(!state);
    }

    public void toggle(boolean value) {
        if (selectedDevice != null && deviceConnected) {
            state = value;
            isSafeLocked.setValue(value);
            sendMessage(PrefUtils.getPassword(), value);
        }
    }

    public void lock() {
        toggle(true);
    }

    public void unlock() {
        toggle(false);
    }

    public LiveData<String> getMessage() {
        return messageLiveData;
    }

    public LiveData<String> getDevice() {
        return selectedDeviceLiveData;
    }

    public LiveData<Boolean> getIsSafeLocked() {
        return isSafeLocked;
    }


    public LiveData<Pair<String, Boolean>> getDeviceState() {
        return deviceState;
    }

    @Override
    public void onReadMessage(String data) {
        messageLiveData.postValue(data);
        state = (Integer.parseInt(data) == 0);
        isSafeLocked.setValue(state);
    }

    @Override
    public void onError(Throwable th) {
        th.printStackTrace();
    }

    @Override
    public void onConnected() {
        deviceConnected = true;
        deviceState.setValue(new Pair<>(selectedDevice, true));
    }
}
