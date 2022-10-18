package org.unibl.etf.yetanotherspeedometer.repository;

import android.location.Location;

import androidx.lifecycle.LiveData;

public interface LocationRepository {

    LiveData<Double> getCurrentSpeed();
    LiveData<Location> getCurrentLocation();
    void addLocation(Location location);
}
