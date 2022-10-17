package org.unibl.etf.yetanotherspeedometer.location;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.unibl.etf.yetanotherspeedometer.repository.LocationRepository;

import java.util.Random;

import javax.inject.Inject;

public class LocationRepositoryImpl implements LocationRepository {

    private MutableLiveData<Double> currentSpeed = new MutableLiveData<>(0.0);
    private Location loc;
    private final int id = new Random().nextInt();

    @Inject
    public LocationRepositoryImpl()
    {

    }

    @Override
    public LiveData<Double> getCurrentSpeed() {
        return currentSpeed;
    }

    public void addLocation(Location location)
    {
        currentSpeed.setValue((double)location.getSpeed());
    }
}
