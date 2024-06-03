package com.example.btmonitor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.btmonitor.SharedViewModel;
import com.example.btmonitor.databinding.FragmentPasswordBinding;
import com.example.btmonitor.utills.PrefUtils;

import java.util.Objects;

public class PasswordFragment extends Fragment {
    private SharedViewModel viewModel;
    private FragmentPasswordBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPasswordBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // method call to initialize the views
        initViews();
        // method call to initialize the listeners
        initListeners();
    }

    private void initListeners() {
        binding.savePassword.setOnClickListener(v-> {
            String password = Objects.requireNonNull(binding.editTextPassword.getText()).toString();
            if(!password.isEmpty() && password.length() >= 10){
                PrefUtils.setPassword(password);
                Toast.makeText(requireContext(), "Password saved!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(requireContext(), "Enter password, min 10 charters", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initViews() {
        binding.editTextPassword.setText(PrefUtils.getPassword());
    }
}
