package org.unibl.etf.yetanotherspeedometer;

import android.app.Application;
import android.location.LocationManager;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class SingletonModule {

    @Provides
    public LocationManager getLocationManager(Application app)
    {
        return app.getSystemService(LocationManager.class);
    }
}
