package org.unibl.etf.yetanotherspeedometer;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import org.unibl.etf.yetanotherspeedometer.databinding.ActivityMainBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var binding = ActivityMainBinding.inflate(getLayoutInflater());
        var viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        setContentView(binding.getRoot());
        checkPermissions();
    }

    private void checkPermissions()
    {
        var permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), requestResult ->
        {
            var isCoarseGranted = requestResult.get(ACCESS_COARSE_LOCATION);
            var isFineGranted = requestResult.get(ACCESS_FINE_LOCATION);
            if(!isCoarseGranted || !isFineGranted)
            {
                Log.d(TAG, "Precise location permission not granted");
                // TODO: Display error message to user and exit
                finishAndRemoveTask();
            }
            else
                Log.d(TAG, "Location permissions granted");
        });

        if(checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
        {
            if(shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION))
            {
                //TODO: Show why we need precies location permission
                Log.d(TAG, "TODO: Show why we need precise location permission");
            }

            permissionLauncher.launch(new String[] { ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION });
        }
    }
}