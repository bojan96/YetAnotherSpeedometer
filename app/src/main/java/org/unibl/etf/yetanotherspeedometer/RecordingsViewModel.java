package org.unibl.etf.yetanotherspeedometer;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.unibl.etf.yetanotherspeedometer.db.AppDatabase;
import org.unibl.etf.yetanotherspeedometer.db.entity.Recording;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RecordingsViewModel extends ViewModel implements DefaultLifecycleObserver {

    private final AppDatabase appDatabase;
    private MutableLiveData<Boolean> hasRecordings = new MutableLiveData<>(false);

    @Inject
    public RecordingsViewModel(AppDatabase appDatabase)
    {
        this.appDatabase = appDatabase;
    }

    public LiveData<List<Recording>> getRecordings()
    {
        return appDatabase.getRecordingDao().getAll();
    }

    public LiveData<Boolean> getHasRecordings()
    {
        return hasRecordings;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        appDatabase.getRecordingDao().getAll().observe(owner, recordings ->
        {
            hasRecordings.setValue(recordings.size() > 0);
        });
    }
}
