package org.unibl.etf.yetanotherspeedometer;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import org.unibl.etf.yetanotherspeedometer.location.AverageSpeedUseCase;
import org.unibl.etf.yetanotherspeedometer.repository.LocationRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainActivityViewModel extends ViewModel implements DefaultLifecycleObserver {

    private static final String TAG = MainActivityViewModel.class.getName();
    private static int updateCountVal = 0;

    private final MutableLiveData<Double> currentSpeed = new MutableLiveData<>(null);
    private final MutableLiveData<String> updateCount = new MutableLiveData<>();
    private final LocationRepository locationRepository;
    private final AverageSpeedUseCase averageSpeedUseCase;
    private final Observer<Double> obs = value ->
    {
        updateCountVal = (updateCountVal + 1) % 10;
        updateCount.postValue(String.format("%d %.2f", updateCountVal, value));
        currentSpeed.postValue(value * 3.6);
    };

    @Inject
    public MainActivityViewModel(LocationRepository locationRepository, AverageSpeedUseCase averageSpeedUseCase)
    {
        this.locationRepository = locationRepository;
        this.averageSpeedUseCase = averageSpeedUseCase;
    }

    public MutableLiveData<Double> getCurrentSpeed() {
        return currentSpeed;
    }

    public MutableLiveData<String> getUpdateCount() {
        return updateCount;
    }

    public LiveData<Double> getAverageSpeed()
    {
        return Transformations.map(averageSpeedUseCase.getCurrentAverageSpeed(), averageSpeed -> averageSpeed * 3.6);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        locationRepository.getCurrentSpeed().observeForever(obs);
        averageSpeedUseCase.startCalcuating();
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        averageSpeedUseCase.stopCalculating();
        locationRepository.getCurrentSpeed().removeObserver(obs);
    }
}
