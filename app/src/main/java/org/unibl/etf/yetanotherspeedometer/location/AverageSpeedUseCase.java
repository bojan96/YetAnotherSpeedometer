package org.unibl.etf.yetanotherspeedometer.location;

import android.location.Location;
import android.media.AudioManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.unibl.etf.yetanotherspeedometer.repository.LocationRepository;

import javax.inject.Inject;

public class AverageSpeedUseCase {

    private static final String TAG = AverageSpeedUseCase.class.getSimpleName();
    private final LocationRepository locationRepository;
    private final MutableLiveData<Double> currentAverageSpeed = new MutableLiveData<>();
    private final MutableLiveData<Double> currentTotalDistance = new MutableLiveData<>();
    private final MutableLiveData<Long> currentTotalTime = new MutableLiveData<>();
    private Location lastLocation;
    private long lastTimestamp;
    private long totalTime = 0;
    private double totalDistance = 0;

    private final Observer<Location> locationObserver = location ->
    {
        Log.d(TAG, String.format("Last timestamp = %d", lastTimestamp));
        if(lastLocation == null) {
            lastLocation = location;
            location.distanceTo(location);
            lastTimestamp = System.nanoTime();
            return;
        }

        double distance = location.distanceTo(lastLocation);
        if(distance < 1)
            return;

        totalDistance += distance;
        lastLocation = location;
        var currentTimestamp = System.nanoTime();
        totalTime += currentTimestamp - lastTimestamp;
        lastTimestamp = currentTimestamp;

        var averageSpeed = totalDistance * 1e9 / totalTime;
        Log.d(TAG, String.format("Total distance = %f, total time = %f, average speed = %f",
                totalDistance, totalTime / 1e9, averageSpeed));
        currentAverageSpeed.postValue(averageSpeed);
        currentTotalDistance.postValue(totalDistance);
        currentTotalTime.postValue(totalTime);
    };

    @Inject
    public AverageSpeedUseCase(LocationRepository locationRepository)
    {
        this.locationRepository = locationRepository;
    }

    public LiveData<Double> getCurrentAverageSpeed()
    {
        return currentAverageSpeed;
    }

    public LiveData<Double> getCurrentTotalDistance() {
        return currentTotalDistance;
    }

    public LiveData<Long> getCurrentTotalTime() {
        return currentTotalTime;
    }

    public void startCalcuating()
    {
        locationRepository.getCurrentLocation().observeForever(locationObserver);
    }

    public void stopCalculating()
    {
        locationRepository.getCurrentLocation().removeObserver(locationObserver);
    }

}
