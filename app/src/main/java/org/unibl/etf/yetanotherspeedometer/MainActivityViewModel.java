package org.unibl.etf.yetanotherspeedometer;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

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
    private final Observer<Double> obs = value ->
    {
        updateCountVal = (updateCountVal + 1) % 10;
        updateCount.postValue(String.format("%d %.2f", updateCountVal, value));
        currentSpeed.postValue(value * 3.6);
    };

    @Inject
    public MainActivityViewModel(LocationRepository locationRepository)
    {
        this.locationRepository = locationRepository;
    }

    public MutableLiveData<Double> getCurrentSpeed() {
        return currentSpeed;
    }

    public MutableLiveData<String> getUpdateCount() {
        return updateCount;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        locationRepository.getCurrentSpeed().observeForever(obs);
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        locationRepository.getCurrentSpeed().removeObserver(obs);
    }
}
