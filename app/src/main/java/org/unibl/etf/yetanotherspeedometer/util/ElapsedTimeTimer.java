package org.unibl.etf.yetanotherspeedometer.util;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Timer;
import java.util.TimerTask;

public class ElapsedTimeTimer {
    private Timer timer;
    private TimerTask timerTask;
    private long currentTimeVal = 0;
    private final MutableLiveData<Long> currentTime = new MutableLiveData<>(0L);

    public ElapsedTimeTimer(Timer timer)
    {
        this.timer = timer;
    }

    public void restart()
    {
        currentTime.setValue(0L);
        currentTimeVal = 0;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                ++currentTimeVal;
//                Log.d(ElapsedTimeTimer.class.getName(), String.format("Timer: %d\n", currentTimeVal));
                currentTime.postValue(currentTimeVal);
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
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
