package org.unibl.etf.yetanotherspeedometer.notification;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;

import org.unibl.etf.yetanotherspeedometer.R;
import org.unibl.etf.yetanotherspeedometer.location.SpeedDetailsUseCase;
import org.unibl.etf.yetanotherspeedometer.repository.LocationRepository;
import org.unibl.etf.yetanotherspeedometer.util.UnitFormatters;

import java.util.Timer;
import java.util.TimerTask;

public class SpeedDetailsNotifier {

    private final Timer timer;
    private final LocationRepository locationtRepository;
    private final SpeedDetailsUseCase speedDetailsUseCase;
    private final Application application;
    private final NotificationManager notificationManager;
    private final UnitFormatters unitFormatters;
    private TimerTask timerTask;

    public SpeedDetailsNotifier(LocationRepository locationRepository, SpeedDetailsUseCase speedDetailsUseCase, Application app, Timer timer, NotificationManager notificationManager, UnitFormatters unitFormatters)
    {
        this.timer = timer;
        this.locationtRepository = locationRepository;
        this.speedDetailsUseCase = speedDetailsUseCase;
        this.application = app;
        this.notificationManager = notificationManager;
        this.unitFormatters = unitFormatters;
    }

    public void startNotifying()
    {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                var currentSpeed = locationtRepository.getCurrentSpeed().getValue();
                var maxSpeed = speedDetailsUseCase.getCurrentMaxSpeed().getValue();
                var avgSpeed = speedDetailsUseCase.getCurrentAverageSpeed().getValue();
                var elapsedTime = speedDetailsUseCase.getCurrentTotalTime().getValue();
                var totalDistance = speedDetailsUseCase.getCurrentTotalDistance().getValue();

                var notification = speedDetailsUseCase.isRecording() ?
                        createRecordingNotification(currentSpeed, totalDistance, elapsedTime) :
                        createNotification(currentSpeed);

                notificationManager.notify(application.getResources().getInteger(R.integer.speed_details_notification_id), notification);
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
    }

    public void stopNotifying()
    {
        timerTask.cancel();
    }

    private Notification createNotification(double currentSpeed)
    {
        return new Notification.Builder(application, application.getString(R.string.location_notification_channel_id))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(application.getString(R.string.location_notification_title))
                .setContentText(unitFormatters.formatSpeed(currentSpeed))
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .build();
    }

    private Notification createRecordingNotification(double currentSpeed, double totalDistance, long elapsedTime)
    {
        return new Notification.Builder(application, application.getString(R.string.location_notification_channel_id))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(application.getString(R.string.location_notification_title))
                .setContentText(String.format("%s, %s, %s",
                        unitFormatters.formatSpeed(currentSpeed),
                        unitFormatters.formatDistance(totalDistance),
                        UnitFormatters.formatElapsedTime(elapsedTime)))
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .build();
    }

}
