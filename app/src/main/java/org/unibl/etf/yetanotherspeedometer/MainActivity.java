package org.unibl.etf.yetanotherspeedometer;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import org.unibl.etf.yetanotherspeedometer.databinding.ActivityMainBinding;
import org.unibl.etf.yetanotherspeedometer.location.LocationService;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @Inject
    public LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var binding = ActivityMainBinding.inflate(getLayoutInflater());
        var viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        getLifecycle().addObserver(viewModel);
        setContentView(binding.getRoot());

        checkPermissions();
        createNotificationChannels();

        startForegroundService(new Intent(this, LocationService.class));
    }

    private void createNotificationChannels()
    {
        var channel = new NotificationChannel(getString(R.string.location_notification_channel_id),
                getString(R.string.location_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT);

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
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