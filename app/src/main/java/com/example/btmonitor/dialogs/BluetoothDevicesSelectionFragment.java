package com.example.btmonitor.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.btmonitor.R;
import com.example.btmonitor.adapter.BluetoothDevicesAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BluetoothDevicesSelectionFragment extends BottomSheetDialogFragment implements BluetoothDevicesAdapter.OnItemClickListener {

    public static final String TAG = "BluetoothDevicesSelectionFragment";

    private TextView tvNoDeviceFound;

    private ListView listView;
    private BluetoothDevicesAdapter adapter;

    private OnItemClickListener listener;

    @Override
    public void onBluetoothDeviceSelected(BluetoothDevice item) {
        if(listener!=null) {
            listener.onBluetoothDeviceSelected(item);
        }
    }

    public interface OnItemClickListener {
        void onBluetoothDeviceSelected(BluetoothDevice item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bluetooth_devices_fragment, null);
        dialog.setContentView(contentView);

        tvNoDeviceFound = contentView.findViewById(R.id.bluetooth_device_fragment_tv_no_device_found);

        listView = contentView.findViewById(R.id.bluetooth_device_fragment_lv_devices);
        adapter = new BluetoothDevicesAdapter(getContext());
        adapter.setOnItemClickListener(this);
        listView.setAdapter(adapter);

        listBluetoothDevices(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        requireContext().registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null && device.getName() != null) {
                    onBluetoothDeviceFound(device);
                }
            }
        }
    };

    public void listBluetoothDevices(Context context) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            } else {
                bluetoothAdapter.startDiscovery();
            }
            for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
                if (device != null && device.getName() != null) {
                    onBluetoothDeviceFound(device);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        requireContext().unregisterReceiver(receiver);
    }

    private void onBluetoothDeviceFound(BluetoothDevice bluetoothHeadsetDevice) {
        adapter.add(bluetoothHeadsetDevice);
        adapter.notifyDataSetChanged();
        tvNoDeviceFound.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
    }

}