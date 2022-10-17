package org.unibl.etf.yetanotherspeedometer.repository;

import android.location.Location;

import androidx.lifecycle.LiveData;

public interface LocationRepository {

    LiveData<Double> getCurrentSpeed();
    void addLocation(Location location);
}
