package com.example.btmonitor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.btmonitor.R;
import com.example.btmonitor.SharedViewModel;
import com.example.btmonitor.databinding.FragmentMainBinding;
import com.example.btmonitor.utills.PrefUtils;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    private SharedViewModel viewModel;
    private boolean isRunning;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.stateButton.setOnClickListener(v -> viewModel.toggle());
        if(!PrefUtils.getMac().isEmpty()){
            viewModel.setDevice(PrefUtils.getMac());
        }
        binding.device.setText(R.string.device_not_connected);
        observeData();
    }

    private void observeData() {
        viewModel.getIsSafeLocked().observe(getViewLifecycleOwner(), this::setStateButton);
        viewModel.getDeviceState().observe(getViewLifecycleOwner(), state-> {
            if(!state.first.isEmpty() && state.second){
                binding.device.setText(getString(R.string.connented_to, state.first));
            }
            else{
                binding.device.setText(R.string.device_not_connected);
            }
        });
        viewModel.getDevice().observe(getViewLifecycleOwner(), data-> {
            if(!data.isEmpty()){
                viewModel.connect();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // viewModel = null;
    }

    public void setStateButton(boolean on) {
        if (on) {
            binding.stateButton.setText(getResources().getString(R.string.state_on));
        } else {
            binding.stateButton.setText(getResources().getString(R.string.state_off));
        }
    }
}
