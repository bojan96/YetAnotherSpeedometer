package org.unibl.etf.yetanotherspeedometer.location;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.unibl.etf.yetanotherspeedometer.repository.LocationRepository;
import org.unibl.etf.yetanotherspeedometer.util.DetailsTimer;

import javax.inject.Inject;

public class SpeedDetailsUseCase {

    private static final String TAG = SpeedDetailsUseCase.class.getSimpleName();
    private final LocationRepository locationRepository;
    private final MutableLiveData<Double> currentAverageSpeed = new MutableLiveData<>();
    private final MutableLiveData<Double> currentTotalDistance = new MutableLiveData<>();
    private final MutableLiveData<Double> currentMaxSpeed = new MutableLiveData<>(0.0);
    private final MutableLiveData<Long> currentTotalTime = new MutableLiveData<>();
    private final DetailsTimer timer = new DetailsTimer();
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
        if(distance < 0.5)
            return;

        totalDistance += distance;
        lastLocation = location;
        var currentTimestamp = System.nanoTime();
        totalTime += currentTimestamp - lastTimestamp;
        lastTimestamp = currentTimestamp;

        var averageSpeed = totalDistance * 1e9 / totalTime;
        Log.d(TAG, String.format("Total distance = %f, total time = %f, average speed = %f",
                totalDistance, totalTime / 1e9, averageSpeed));

        if(location.getSpeed() > currentMaxSpeed.getValue())
            currentMaxSpeed.postValue((double) location.getSpeed());
        currentAverageSpeed.postValue(averageSpeed);
        currentTotalDistance.postValue(totalDistance);
    };

    @Inject
    public SpeedDetailsUseCase(LocationRepository locationRepository)
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
        return timer.getCurrentTime();
    }

    public LiveData<Double> getCurrentMaxSpeed() {
        return currentMaxSpeed;
    }

    public void startCalcuating()
    {
        timer.restart();
        locationRepository.getCurrentLocation().observeForever(locationObserver);
    }

    public void stopCalculating()
    {
        timer.stop();
        locationRepository.getCurrentLocation().removeObserver(locationObserver);
    }

}
