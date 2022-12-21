package org.unibl.etf.yetanotherspeedometer.util;

import android.widget.Switch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import org.unibl.etf.yetanotherspeedometer.settings.SettingsStore;

public class UnitFormattersTransformations {

    private final SettingsStore settingsStore;
    private final SwitchLiveData<String> _speedLiveData;
    private final SwitchLiveData<String> _distanceLiveData;
    private final SwitchLiveData<String> _currentSpeedLiveData;

    public UnitFormattersTransformations(SettingsStore settingsStore) {
        this.settingsStore = settingsStore;
        _speedLiveData = new SwitchLiveData<>(settingsStore.getUseImperialUnits());
        _distanceLiveData = new SwitchLiveData<>(settingsStore.getUseImperialUnits());
        _currentSpeedLiveData = new SwitchLiveData<>(settingsStore.getUseImperialUnits());
    }

    public static LiveData<String> formatSpeedToKmPerHour(LiveData<Double> speed) {
        return Transformations.map(speed, s -> UnitFormatters.formatSpeedKmPerHour(s));
    }

    public static LiveData<String> formatDistanceMeters(LiveData<Double> distance) {
        return Transformations.map(distance, d -> UnitFormatters.formatDistanceMeters(d));
    }

    public LiveData<String> formatElapsedTime(LiveData<Long> elapsedTime) {
        return Transformations.map(elapsedTime, t -> UnitFormatters.formatElapsedTime(t));
    }

    public LiveData<String> formatSpeed(LiveData<Double> speed) {
        return _speedLiveData
                .ifFalse(Transformations.map(speed, s -> UnitFormatters.formatSpeedKmPerHour(s)))
                .ifTrue(Transformations.map(speed, s -> UnitFormatters.formatSpeedMilesPerHour(s)));
    }

    public LiveData<String> formatDistance(LiveData<Double> distance) {
        return _distanceLiveData
                .ifFalse(Transformations.map(distance, d -> UnitFormatters.formatDistanceMeters(d)))
                .ifTrue(Transformations.map(distance, d -> UnitFormatters.formatDistanceMiles(d)));
    }

    public LiveData<String> formatCurrentSpeed(LiveData<Double> speed)
    {
        return _currentSpeedLiveData
                .ifFalse(Transformations.map(speed, s -> UnitFormatters.formatCurrentSpeedKmPerHour(s)))
                .ifTrue(Transformations.map(speed, s -> UnitFormatters.formatCurrentSpeedMilesPerHour(s)));
    }
}
