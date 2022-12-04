package org.unibl.etf.yetanotherspeedometer.util;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class UnitFormatters {
    private UnitFormatters()
    {

    }

    public static String formatSpeedKmPerHour(double speed)
    {
        return String.format("%d km/h", Math.round(speed * 3.6));
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

    public static String formatCurrentSpeedKmPerHour(Double speed)
    {
        return String.format("%03d", speed != null ? Math.round(speed) : 0);
    }
}
