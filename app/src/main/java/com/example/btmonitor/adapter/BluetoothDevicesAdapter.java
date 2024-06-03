package com.example.btmonitor.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.example.btmonitor.R;
import com.example.btmonitor.utills.PrefUtils;

import java.util.ArrayList;
import java.util.List;

public class BluetoothDevicesAdapter extends BaseAdapter {

    private final Context context;
    private final List<BluetoothDevice> list = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onBluetoothDeviceSelected(BluetoothDevice item);
        }

    public BluetoothDevicesAdapter( Context context) {
        this.context = context;
    }

    public void add(BluetoothDevice device) {
        list.add(device);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.bluetooth_device, parent, false);
        }

        BluetoothDevice bluetoothHeadsetDevice = getItem(position);

        TextView tvName = view.findViewById(R.id.tv_bluetooth_device_name);
        tvName.setText(bluetoothHeadsetDevice.getName());

        Switch swSelection = view.findViewById(R.id.switch_bluetooth_device_selection);
        swSelection.setOnCheckedChangeListener((buttonView, isChecked) -> {
        //    listener.onBluetoothDeviceSelected(bluetoothHeadsetDevice);
        });
        swSelection.setChecked(PrefUtils.getMac().contains(bluetoothHeadsetDevice.getAddress()));
        view.setOnClickListener(v-> {
            listener.onBluetoothDeviceSelected(bluetoothHeadsetDevice);
        });
        return view;
    }
}
