package org.unibl.etf.yetanotherspeedometer.util;

import org.unibl.etf.yetanotherspeedometer.settings.SettingsStore;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class UnitFormatters {

    private static final double KM_PER_HOUR = 3.6;
    private static final double MI_PER_HOUR = 2.2369362921;

    private final SettingsStore settingsStore;

    public UnitFormatters(SettingsStore settingsStore)
    {
        this.settingsStore = settingsStore;
    }

    public static String formatSpeedKmPerHour(double speed)
    {
        return String.format("%d km/h", Math.round(speed * KM_PER_HOUR));
    }

    public static String formatSpeedMilesPerHour(double speed)
    {
        return String.format("%d mph", Math.round(speed * MI_PER_HOUR));
    }

    public static String formatDistanceMiles(double distance)
    {
        if(distance > 1609.344)
            return String.format("%.2f mi", distance * 0.00062137119);

        return String.format("%d yd", Math.round(distance * 1.0936133));
    }

    public static String formatDistanceMeters(double distance)
    {
        if(distance > 1000)
            return String.format("%.2f km", distance / 1000);

        return String.format("%d m", Math.round(distance));
    }

    public static String formatElapsedTime(long elapsedTime)
    {
        long hours = elapsedTime / 3600;
        long minutes = (elapsedTime % 3600) / 60;
        long seconds = (elapsedTime % 3600) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String formatCurrentSpeedKmPerHour(double speed)
    {
        return String.format("%03d", Math.round(speed * KM_PER_HOUR));
    }

    public static String formatCurrentSpeedMilesPerHour(double speed)
    {
        return String.format("%03d", Math.round(speed * MI_PER_HOUR));
    }

    private boolean useImperialUnits()
    {
        return settingsStore.getUseImperialUnits().getValue();
    }

    public String formatSpeed(double speed)
    {
        return useImperialUnits() ?
                formatSpeedMilesPerHour(speed) : formatSpeedKmPerHour(speed);
    }

    public String formatDistance(double distance)
    {
        return useImperialUnits() ?
                formatDistanceMiles(distance) : formatDistanceMeters(distance);
    }
}
