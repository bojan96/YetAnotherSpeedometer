package org.unibl.etf.yetanotherspeedometer;

import android.app.Application;
import android.app.NotificationManager;
import android.location.LocationManager;

import androidx.room.Room;

import org.unibl.etf.yetanotherspeedometer.db.AppDatabase;
import org.unibl.etf.yetanotherspeedometer.location.SpeedDetailsUseCase;
import org.unibl.etf.yetanotherspeedometer.location.LocationRepositoryImpl;
import org.unibl.etf.yetanotherspeedometer.notification.SpeedDetailsNotifier;
import org.unibl.etf.yetanotherspeedometer.repository.LocationRepository;
import org.unibl.etf.yetanotherspeedometer.util.ElapsedTimeTimer;

import java.util.Timer;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class SingletonModule {

    @Provides
    public static LocationManager getLocationManager(Application app)
    {
        return app.getSystemService(LocationManager.class);
    }

    @Binds
    @Singleton
    public abstract LocationRepository getLocationRepository(LocationRepositoryImpl locationRepository);

    @Provides
    @Singleton
    public static Timer getTimer()
    {
        return new Timer();
    }

    @Provides
    @Singleton
    public static ElapsedTimeTimer getElapsedTimeTimer(Timer timer) {
        return new ElapsedTimeTimer(timer);
    }

    @Provides
    @Singleton
    public static SpeedDetailsUseCase getAverageSpeedUseCase(LocationRepository locationRepo, ElapsedTimeTimer timer)
    {
        return new SpeedDetailsUseCase(locationRepo, timer);
    }

    @Provides
    @Singleton
    public static NotificationManager getNotificationManager(Application app)
    {
        return app.getSystemService(NotificationManager.class);
    }

    @Provides
    @Singleton
    public static SpeedDetailsNotifier getSpeedDetailsNotifier(LocationRepository locationRepository,
                                                               SpeedDetailsUseCase speedDetailsUseCase,
                                                               NotificationManager notificationManager,
                                                               Application app,
                                                               Timer timer)
    {
        return new SpeedDetailsNotifier(locationRepository, speedDetailsUseCase, app, timer, notificationManager);
    }


    @Provides
    @Singleton
    public static AppDatabase getAppDatabase(Application app)
    {
        return Room.databaseBuilder(app, AppDatabase.class, "appDatabase").build();
    }
}
