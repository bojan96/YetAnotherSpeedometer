package org.unibl.etf.yetanotherspeedometer;

import android.app.Application;
import android.location.LocationManager;

import org.unibl.etf.yetanotherspeedometer.location.SpeedDetailsUseCase;
import org.unibl.etf.yetanotherspeedometer.location.LocationRepositoryImpl;
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

}
