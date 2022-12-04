package org.unibl.etf.yetanotherspeedometer;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import org.unibl.etf.yetanotherspeedometer.db.AppDatabase;
import org.unibl.etf.yetanotherspeedometer.db.entity.Recording;
import org.unibl.etf.yetanotherspeedometer.location.SpeedDetailsUseCase;
import org.unibl.etf.yetanotherspeedometer.repository.LocationRepository;
import org.unibl.etf.yetanotherspeedometer.util.UnitFormatters;
import org.unibl.etf.yetanotherspeedometer.util.UnitFormattersTransformations;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class MainActivityViewModel extends ViewModel implements DefaultLifecycleObserver {

    private static final String TAG = MainActivityViewModel.class.getName();
    private static int updateCountVal = 0;

    private final MutableLiveData<String> currentSpeed = new MutableLiveData<>();
    private final MutableLiveData<String> updateCount = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRecording = new MutableLiveData<>(false);
    private final LocationRepository locationRepository;
    private final SpeedDetailsUseCase speedDetailsUseCase;
    private final Observer<Double> obs = value ->
    {
        updateCountVal = (updateCountVal + 1) % 10;
        updateCount.postValue(String.format("%d %.2f", updateCountVal, value));
        currentSpeed.postValue(UnitFormatters.formatCurrentSpeedKmPerHour(value));
    };
    private final AppDatabase appDatabase;

    @Inject
    public MainActivityViewModel(LocationRepository locationRepository, SpeedDetailsUseCase speedDetailsUseCase, AppDatabase appDatabase)
    {
        this.locationRepository = locationRepository;
        this.speedDetailsUseCase = speedDetailsUseCase;
        this.appDatabase = appDatabase;
    }

    public LiveData<String> getCurrentSpeed() {
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

    public LiveData<String> getRecordingDuration()
    {
        return UnitFormattersTransformations.formatElapsedTime(speedDetailsUseCase.getCurrentTotalTime());
    }

    public LiveData<String> getCurrentAverageSpeed()
    {
        return UnitFormattersTransformations.formatSpeedToKmPerHour(speedDetailsUseCase.getCurrentAverageSpeed());
    }

    public LiveData<String> getCurrentMaxSpeed()
    {
        return UnitFormattersTransformations.formatSpeedToKmPerHour(speedDetailsUseCase.getCurrentMaxSpeed());
    }

    public LiveData<String> getCurrentTotalDistance()
    {
        return UnitFormattersTransformations.formatDistanceMeters(speedDetailsUseCase.getCurrentTotalDistance());
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
        writeRecordingToDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> isRecording.setValue(false));
    }

    private Completable writeRecordingToDb()
    {
        var avgSpeed = speedDetailsUseCase.getCurrentAverageSpeed().getValue();
        var maxSpeed = speedDetailsUseCase.getCurrentMaxSpeed().getValue();
        var distance = speedDetailsUseCase.getCurrentTotalDistance().getValue();
        var elapsedTime = speedDetailsUseCase.getCurrentTotalTime().getValue();
        var recording = new Recording();
        recording.avgSpeed = avgSpeed;
        recording.elapsedTime = elapsedTime;
        recording.maxSpeed = maxSpeed;
        recording.totalDistance = distance;
        return appDatabase.getRecordingDao().addRecording(recording);
    }

    private void startRecording()
    {
        speedDetailsUseCase.startCalcuating();
        isRecording.setValue(true);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        locationRepository.getCurrentSpeed().observe(owner, obs);

    }
}
