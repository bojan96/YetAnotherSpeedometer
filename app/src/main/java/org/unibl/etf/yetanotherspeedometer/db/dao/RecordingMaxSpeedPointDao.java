package org.unibl.etf.yetanotherspeedometer.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import org.unibl.etf.yetanotherspeedometer.db.entity.RecordingMaxSpeedPoint;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface RecordingMaxSpeedPointDao {

    @Insert
    Completable addRecordingMaxSpeedPoint(RecordingMaxSpeedPoint recordingMaxSpeedPoint);
}
