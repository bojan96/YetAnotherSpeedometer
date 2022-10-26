package org.unibl.etf.yetanotherspeedometer.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Timer;
import java.util.TimerTask;

public class DetailsTimer {
    private Timer timer;
    private final MutableLiveData<Long> currentTime = new MutableLiveData<>(0L);

    public void restart()
    {
        currentTime.setValue(0L);
        var timerTask = new TimerTask() {
            @Override
            public void run() {
                currentTime.postValue(currentTime.getValue() + 1);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 1000, 1000);
    }

    public void stop()
    {
        timer.cancel();
    }

    public LiveData<Long> getCurrentTime()
    {
        return currentTime;
    }
}
