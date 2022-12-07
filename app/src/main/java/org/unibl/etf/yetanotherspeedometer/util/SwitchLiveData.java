package org.unibl.etf.yetanotherspeedometer.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class SwitchLiveData<T> extends MutableLiveData<T> {

    private LiveData<Boolean> booleanSwitch = null;
    private LiveData<T> ifTrueLiveData;
    private LiveData<T> ifFalseLiveData;
    private final Observer<T> trueSetterObserver = val ->
    {
        if(booleanSwitch.getValue())
            setValue(val);
    };

    private final Observer<T> falseSetterObserver = val ->
    {
        if(!booleanSwitch.getValue())
            setValue(val);
    };

    public SwitchLiveData(LiveData<Boolean> booleanSwitch)
    {
        this.booleanSwitch = booleanSwitch;
        booleanSwitch.observeForever(val ->
        {
            if(val)
            {
                if(ifTrueLiveData != null)
                    setValue(ifTrueLiveData.getValue());
            }
            else
            {
                if(ifFalseLiveData != null)
                    setValue(ifFalseLiveData.getValue());
            }
        });
    }

    public SwitchLiveData<T> ifTrue(LiveData<T> liveData)
    {
        if(ifTrueLiveData != null)
            ifTrueLiveData.removeObserver(trueSetterObserver);
        ifTrueLiveData = liveData;
        ifTrueLiveData.observeForever(trueSetterObserver);
        return this;
    }

    public SwitchLiveData<T> ifFalse(LiveData<T> liveData)
    {
        if(ifFalseLiveData != null)
            ifFalseLiveData.removeObserver(falseSetterObserver);
        ifFalseLiveData = liveData;
        ifFalseLiveData.observeForever(falseSetterObserver);
        return this;
    }

}
