package org.unibl.etf.yetanotherspeedometer.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;

import org.unibl.etf.yetanotherspeedometer.db.entity.Recording;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface RecordingDao {

    // TODO: Sort by time
    @Query("SELECT * FROM recording")
    LiveData<List<Recording>> getAll();

    @Insert
    Completable addRecording(Recording recording);
}
