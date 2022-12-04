package org.unibl.etf.yetanotherspeedometer.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

public class UnitFormattersTransformations {

    private UnitFormattersTransformations()
    {

    }

    public static LiveData<String> formatSpeedToKmPerHour(LiveData<Double> speed)
    {
        return Transformations.map(speed, s -> UnitFormatters.formatSpeedKmPerHour(s));
    }

    public static LiveData<String> formatDistanceMeters(LiveData<Double> distance)
    {
        return Transformations.map(distance, d -> UnitFormatters.formatDistanceMeters(d));
    }

    public static LiveData<String> formatElapsedTime(LiveData<Long> elapsedTime)
    {
        return Transformations.map(elapsedTime, t -> UnitFormatters.formatElapsedTime(t));
    }
}
