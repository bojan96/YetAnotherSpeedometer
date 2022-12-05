package org.unibl.etf.yetanotherspeedometer.settings;

import android.app.Application;
import android.content.SharedPreferences;

import org.unibl.etf.yetanotherspeedometer.R;

public class SettingsStore {


    private final SharedPreferences sharedPreferences;
    private final Application application;

    public SettingsStore(SharedPreferences sharedPreferences, Application application)
    {
        this.sharedPreferences = sharedPreferences;
        this.application = application;
    }

    public boolean useImperialUnits()
    {
        return sharedPreferences.getBoolean(application.getString(R.string.settings_key_use_imperial_units), false);
    }
}
