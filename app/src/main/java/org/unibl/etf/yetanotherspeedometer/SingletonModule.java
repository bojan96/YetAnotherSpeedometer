package org.unibl.etf.yetanotherspeedometer;

import android.app.Application;
import android.location.LocationManager;

import org.unibl.etf.yetanotherspeedometer.location.LocationRepositoryImpl;
import org.unibl.etf.yetanotherspeedometer.repository.LocationRepository;

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
}
