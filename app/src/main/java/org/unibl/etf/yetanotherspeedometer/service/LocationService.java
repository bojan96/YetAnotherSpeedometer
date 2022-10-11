package org.unibl.etf.yetanotherspeedometer.service;

import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LocationService extends Service {

    @Inject
    public LocationManager locationManager;

    @Inject
    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}