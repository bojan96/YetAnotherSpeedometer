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
import org.unibl.etf.yetanotherspeedometer.db.entity.RecordingMaxSpeedPoint;
import org.unibl.etf.yetanotherspeedometer.db.entity.RecordingPoint;
import org.unibl.etf.yetanotherspeedometer.location.SpeedDetailsUseCase;
import org.unibl.etf.yetanotherspeedometer.repository.LocationRepository;
import org.unibl.etf.yetanotherspeedometer.settings.SettingsStore;
import org.unibl.etf.yetanotherspeedometer.util.UnitFormatters;
import org.unibl.etf.yetanotherspeedometer.util.UnitFormattersTransformations;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class MainActivityViewModel extends ViewModel implements DefaultLifecycleObserver {

    private static final String TAG = MainActivityViewModel.class.getName();
    private static int updateCountVal = 0;

    private final MutableLiveData<String> updateCount = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRecording = new MutableLiveData<>(false);
    private final LocationRepository locationRepository;
    private final SpeedDetailsUseCase speedDetailsUseCase;
    private final Observer<Double> obs = value ->
    {
        updateCountVal = (updateCountVal + 1) % 10;
        updateCount.postValue(String.format("%d %.2f", updateCountVal, value));
    };
    private final AppDatabase appDatabase;
    private final SettingsStore settingsStore;
    private final UnitFormattersTransformations unitFormatters;

    @Inject
    public MainActivityViewModel(LocationRepository locationRepository,
                                 SpeedDetailsUseCase speedDetailsUseCase,
                                 AppDatabase appDatabase,
                                 SettingsStore settingsStore,
                                 UnitFormattersTransformations unitFormatters)
    {
        this.locationRepository = locationRepository;
        this.speedDetailsUseCase = speedDetailsUseCase;
        this.appDatabase = appDatabase;
        this.settingsStore = settingsStore;
        this.unitFormatters = unitFormatters;
    }

    public LiveData<String> getCurrentSpeed() {
        return Transformations.map(locationRepository.getCurrentSpeed(), speed -> UnitFormatters.formatCurrentSpeed(speed));
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
        return unitFormatters.formatElapsedTime(speedDetailsUseCase.getCurrentTotalTime());
    }

    public LiveData<String> getCurrentAverageSpeed()
    {
        return unitFormatters.formatSpeed(speedDetailsUseCase.getCurrentAverageSpeed());
    }

    public LiveData<String> getCurrentMaxSpeed()
    {
        return unitFormatters.formatSpeed(speedDetailsUseCase.getCurrentMaxSpeed());
    }

    public LiveData<String> getCurrentTotalDistance()
    {
        return unitFormatters.formatDistance(speedDetailsUseCase.getCurrentTotalDistance());
    }

    public LiveData<String> useImperialUnits()
    {
        return Transformations.map(settingsStore.getUseImperialUnits(), useImperialUnits -> useImperialUnits ? "mph" : "km/h");
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
        writeRecordingToDb();
    }

    private void writeRecordingToDb()
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
        appDatabase.getRecordingDao().addRecording(recording)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(id ->
            {
                var recordingPoints = new ArrayList<RecordingPoint>();
                var points = speedDetailsUseCase.getPoints();
                for(int i = 0; i < points.size(); ++i)
                {
                    var recordingPoint = new RecordingPoint();
                    recordingPoint.recordingId = id.intValue();
                    recordingPoint.latitude = points.get(i).getLatitude();
                    recordingPoint.longitude = points.get(i).getLongitude();
                    recordingPoint.orderIndex = i;
                    recordingPoints.add(recordingPoint);
                }

                appDatabase.getRecordingPointsDao()
                        .addRecordingPoints(recordingPoints)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() ->
                        {
                            if(speedDetailsUseCase.getMaxSpeedPointIndex() != -1) {
                                var maxSpeedPoint = new RecordingMaxSpeedPoint();
                                maxSpeedPoint.recordingId = id.intValue();
                                maxSpeedPoint.maxSpeedPointIndex = speedDetailsUseCase.getMaxSpeedPointIndex();
                                appDatabase
                                        .getRecordingMaxSpeedPointDao()
                                        .addRecordingMaxSpeedPoint(maxSpeedPoint)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> isRecording.setValue(false));
                            }
                            else
                                isRecording.setValue(false);
                        });
            });
    }

    private void startRecording()
    {
        speedDetailsUseCase.startCalculating();
        isRecording.setValue(true);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        locationRepository.getCurrentSpeed().observe(owner, obs);

    }
}
