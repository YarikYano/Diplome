package com.example.btmonitor;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.btmonitor.databinding.ActivityMainBinding;
import com.example.btmonitor.dialogs.BluetoothDevicesSelectionFragment;
import com.example.btmonitor.utills.PrefUtils;

public class MainActivity extends AppCompatActivity {
    private final int ENABLE_REQUEST = 15;
    private static final String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };
    private BluetoothAdapter btAdapter;
  //  private BtConnection btConnection;

    private ActivityMainBinding binding;

    private MenuItem btMenuItem;
    private SharedViewModel viewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate and get instance of binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // set content view to binding's root
        setContentView(binding.getRoot());

        initViewModel();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.action_home, R.id.action_settings, R.id.action_timer)
                .build();
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

        init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        btMenuItem = menu.findItem(R.id.id_bt_button);
        setBtIcon(btMenuItem);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.id_bt_button) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {

                if (!btAdapter.isEnabled()) {
                    enableBt();
                } else {
                    btAdapter.disable();
                    item.setIcon(R.drawable.ic_bt_enable);

                }
            }
        } else if (item.getItemId() == R.id.id_menu) {
            if (btAdapter.isEnabled()) {
                //  Intent i = new Intent(MainActivity.this, BtListActivity.class);
                // startActivity(i);
                openBluetoothDevicePicker();
            } else {
                Toast.makeText(this, "Turn on Bluetooth", Toast.LENGTH_SHORT).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ENABLE_REQUEST) {
            if (resultCode == RESULT_OK) {
                setBtIcon(btMenuItem);

            }

        }
    }

    private void setBtIcon(MenuItem menuItem) {
        if (btAdapter.isEnabled()) {
            menuItem.setIcon(R.drawable.ic_bt_disable);

        } else {
            menuItem.setIcon(R.drawable.ic_bt_enable);
        }

    }

    private void init() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        //btConnection = new BtConnection();

        observeViewModelData();

        checkPermissions();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
    }

    private void enableBt() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(i, ENABLE_REQUEST);
        } else {
            Toast.makeText(this, "Turn on Bluetooth", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btAdapter = null;

        viewModel = null;
        binding = null;
        btMenuItem = null;
    }

    void observeViewModelData() {
        /*viewModel.getDevice().observe(this, (data -> {
            if (data != null && !TextUtils.isEmpty(data)) {
                //btConnection.connect(data);
            }
        }));
        viewModel.getMessage().observe(this, (data -> {
            if (data != null && !TextUtils.isEmpty(data)) {
              //  btConnection.sendMessage(data);
            }
        }));*/
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_LOCATION,
                    1
            );
        }
    }

    private void openBluetoothDevicePicker() {
        BluetoothDevicesSelectionFragment bluetoothDevicesSelectionFragment = new BluetoothDevicesSelectionFragment();
        bluetoothDevicesSelectionFragment.setOnItemClickListener(
                device -> {

                    PrefUtils.setMac(device.getAddress());
                    viewModel.setDevice(device.getAddress());
                    bluetoothDevicesSelectionFragment.dismiss();
                });

        bluetoothDevicesSelectionFragment.show(getSupportFragmentManager(), BluetoothDevicesSelectionFragment.TAG);
    }
}