package org.unibl.etf.yetanotherspeedometer.settings;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.unibl.etf.yetanotherspeedometer.R;

public class SettingsStore {


    private final SharedPreferences sharedPreferences;
    private Application application;
    private final MutableLiveData<Boolean> useImperialUnits = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> keepScreenOn = new MutableLiveData<>(false);

    private final SharedPreferences.OnSharedPreferenceChangeListener onPreferenceChangeListener = 
            (sharedPreferences, key) -> 
            {
                if(key.equals(application.getString(R.string.settings_key_use_imperial_units)))
                    useImperialUnits.postValue(readUseImperialUnits());
                else if(key.equals(application.getString(R.string.settings_key_keep_screen_on)))
                    keepScreenOn.postValue(readKeepScreenOn());
            };

    public SettingsStore(SharedPreferences sharedPreferences, Application application)
    {
        this.sharedPreferences = sharedPreferences;
        this.application = application;
        useImperialUnits.setValue(readUseImperialUnits());
        keepScreenOn.setValue(readKeepScreenOn());
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(onPreferenceChangeListener);
    }

    private boolean readUseImperialUnits()
    {
        return sharedPreferences.getBoolean(application.getString(R.string.settings_key_use_imperial_units), false);
    }

    private boolean readKeepScreenOn()
    {
        return sharedPreferences.getBoolean(application.getString(R.string.settings_key_keep_screen_on), false);
    }

    public LiveData<Boolean> getUseImperialUnits()
    {
        return useImperialUnits;
    }

    public LiveData<Boolean> getKeepScreenOn()
    {
        return keepScreenOn;
    }
}
