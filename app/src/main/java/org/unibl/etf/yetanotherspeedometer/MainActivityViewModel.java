package org.unibl.etf.yetanotherspeedometer;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import org.unibl.etf.yetanotherspeedometer.location.SpeedDetailsUseCase;
import org.unibl.etf.yetanotherspeedometer.repository.LocationRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainActivityViewModel extends ViewModel implements DefaultLifecycleObserver {

    private static final String TAG = MainActivityViewModel.class.getName();
    private static int updateCountVal = 0;

    private final MutableLiveData<Double> currentSpeed = new MutableLiveData<>(null);
    private final MutableLiveData<String> updateCount = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRecording = new MutableLiveData<>(false);
    private final LocationRepository locationRepository;
    private final SpeedDetailsUseCase speedDetailsUseCase;
    private final Observer<Double> obs = value ->
    {
        updateCountVal = (updateCountVal + 1) % 10;
        updateCount.postValue(String.format("%d %.2f", updateCountVal, value));
        currentSpeed.postValue(value * 3.6);
    };

    @Inject
    public MainActivityViewModel(LocationRepository locationRepository, SpeedDetailsUseCase speedDetailsUseCase)
    {
        this.locationRepository = locationRepository;
        this.speedDetailsUseCase = speedDetailsUseCase;
    }

    public MutableLiveData<Double> getCurrentSpeed() {
        return currentSpeed;
    }

    public MutableLiveData<String> getUpdateCount() {
        return updateCount;
    }

    public LiveData<Double> getAverageSpeed()
    {
        return Transformations.map(speedDetailsUseCase.getCurrentAverageSpeed(), averageSpeed -> averageSpeed * 3.6);
    }

    public LiveData<Boolean> getIsRecording() {
        return isRecording;
    }

    public LiveData<Long> getRecordingDuration() {
        return speedDetailsUseCase.getCurrentTotalTime();
    }

    public LiveData<Double> getCurrentAverageSpeed()
    {
        return Transformations.map(speedDetailsUseCase.getCurrentAverageSpeed(), speed -> speed * 3.6);
    }

    public LiveData<Double> getCurrentMaxSpeed()
    {
        return Transformations.map(speedDetailsUseCase.getCurrentMaxSpeed(), speed -> speed * 3.6);
    }

    public LiveData<Double> getCurrentTotalDistance()
    {
        return speedDetailsUseCase.getCurrentTotalDistance();
    }

    public void toggleRecording()
    {
        if(isRecording.getValue())
            stopRecording();
        else
            startRecording();
    }

    private void stopRecording()
    {
        speedDetailsUseCase.stopCalculating();
        isRecording.setValue(false);
    }

    private void startRecording()
    {
        speedDetailsUseCase.startCalcuating();
        isRecording.setValue(true);
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
