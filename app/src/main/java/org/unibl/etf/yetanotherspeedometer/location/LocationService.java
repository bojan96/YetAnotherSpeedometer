package org.unibl.etf.yetanotherspeedometer.location;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import androidx.core.location.LocationListenerCompat;

import org.unibl.etf.yetanotherspeedometer.R;
import org.unibl.etf.yetanotherspeedometer.repository.LocationRepository;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LocationService extends Service {

    private static final int NOTIFICATION_ID = 1;

    @Inject
    public LocationManager locationManager;
    @Inject
    public LocationRepository locationRepository;

    public final LocationListenerCompat locationListener = location -> {
        Log.d(LocationService.class.getName(), String.format("Location update: (%f, %f) Speed = %f",
                location.getLatitude(), location.getLongitude(), location.getSpeed()));
        locationRepository.addLocation(location);
    };

    @Inject
    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, new Notification.Builder(this, getString(R.string.location_notification_channel_id))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.location_notification_title))
                .setContentText(getString(R.string.location_notification_text))
                .build());
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locationListener);
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        locationManager.removeUpdates(locationListener);
    }
}