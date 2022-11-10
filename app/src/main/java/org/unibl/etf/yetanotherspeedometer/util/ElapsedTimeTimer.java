package org.unibl.etf.yetanotherspeedometer.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Timer;
import java.util.TimerTask;

public class ElapsedTimeTimer {
    private final Timer timer = new Timer();
    private TimerTask timerTask;
    private final MutableLiveData<Long> currentTime = new MutableLiveData<>(0L);

    public void restart()
    {
        currentTime.setValue(0L);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                currentTime.postValue(currentTime.getValue() + 1);
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }

    public void stop()
    {
        timerTask.cancel();
    }

    public LiveData<Long> getCurrentTime()
    {
        return currentTime;
    }
}
