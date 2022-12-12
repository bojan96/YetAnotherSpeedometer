package org.unibl.etf.yetanotherspeedometer.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.unibl.etf.yetanotherspeedometer.db.entity.RecordingPoint;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface RecordingPointDao {

    @Insert
    Completable addRecordingPoints(List<RecordingPoint> recordingPoint);

}
