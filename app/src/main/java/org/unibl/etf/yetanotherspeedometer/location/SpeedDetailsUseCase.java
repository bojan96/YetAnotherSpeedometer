package org.unibl.etf.yetanotherspeedometer.location;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.unibl.etf.yetanotherspeedometer.repository.LocationRepository;
import org.unibl.etf.yetanotherspeedometer.util.ElapsedTimeTimer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SpeedDetailsUseCase {

    public static class PointInfo
    {
        private double latitude;
        private double longitude;

        public PointInfo(Location location) {
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    private static final String TAG = SpeedDetailsUseCase.class.getSimpleName();
    private final LocationRepository locationRepository;
    private final MutableLiveData<Double> currentAverageSpeed = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> currentTotalDistance = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> currentMaxSpeed = new MutableLiveData<>(0.0);
    private ElapsedTimeTimer timer;
    private Location lastLocation;
    private long lastTimestamp;
    private long totalTime = 0;
    private double totalDistance = 0;
    private boolean isRecording = false;
    private List<PointInfo> points = new ArrayList<>();
    private int maxSpeedPointIndex = -1;
    private long notMovingCounts = 0;

    private final Observer<Location> locationObserver = location ->
    {
        Log.d(TAG, String.format("Last timestamp = %d", lastTimestamp));
        if(lastLocation == null) {
            lastLocation = location;
            lastTimestamp = System.nanoTime();
            currentMaxSpeed.postValue((double)location.getSpeed());
            maxSpeedPointIndex = 0;
            points.add(new PointInfo(location));
            return;
        }

        double distance = location.distanceTo(lastLocation);
        if(compareDoubles(location.getSpeed(), 0, 0.1)) {
            lastTimestamp = System.nanoTime();
            Log.d(TAG, String.format("Not moving: %d (%f, %f) (%f, %f)\n", ++notMovingCounts,
                    location.getLatitude(), location.getLongitude(), lastLocation.getLatitude(), lastLocation.getLongitude()));
            return;
        }

        totalDistance += distance;
        lastLocation = location;
        var currentTimestamp = System.nanoTime();
        totalTime += currentTimestamp - lastTimestamp;
        lastTimestamp = currentTimestamp;

        var averageSpeed = totalDistance * 1e9 / totalTime;
        Log.d(TAG, String.format("Total distance = %f, total time = %f, average speed = %f",
                totalDistance, totalTime / 1e9, averageSpeed));

        if(location.getSpeed() > currentMaxSpeed.getValue())
        {
            currentMaxSpeed.postValue((double) location.getSpeed());
            maxSpeedPointIndex = points.size();
        }
        currentAverageSpeed.postValue(averageSpeed);
        currentTotalDistance.postValue(totalDistance);
        points.add(new PointInfo(location));
    };

    private static boolean compareDoubles(double lhs, double rhs, double epsilon)
    {
        return Math.abs(lhs - rhs) < epsilon;
    }

    @Inject
    public SpeedDetailsUseCase(LocationRepository locationRepository, ElapsedTimeTimer timer)
    {
        this.locationRepository = locationRepository;
        this.timer = timer;
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

    public List<PointInfo> getPoints()
    {
        return points;
    }

    public void startCalculating()
    {
        resetState();
        timer.restart();
        locationRepository.getCurrentLocation().observeForever(locationObserver);
        isRecording = true;
    }

    public void stopCalculating()
    {
        timer.stop();
        locationRepository.getCurrentLocation().removeObserver(locationObserver);
        isRecording = false;
    }

    private void resetState()
    {
        currentMaxSpeed.setValue(0.0);
        currentAverageSpeed.setValue(0.0);
        currentTotalDistance.setValue(0.0);
        totalDistance = 0;
        totalTime = 0;
        lastLocation = null;
        lastTimestamp = 0;
        points = new ArrayList<>();
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    public int getMaxSpeedPointIndex() {
        return maxSpeedPointIndex;
    }

}
