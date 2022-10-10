package org.unibl.etf.yetanotherspeedometer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<String> str = new MutableLiveData<>("test");

    public LiveData<String> getStr() {
        return str;
    }

    public void onTextChange(String s)
    {
        str.setValue(s);
    }
}
