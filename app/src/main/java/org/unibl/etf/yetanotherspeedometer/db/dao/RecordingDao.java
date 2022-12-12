package org.unibl.etf.yetanotherspeedometer.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import org.unibl.etf.yetanotherspeedometer.db.entity.Recording;
import org.unibl.etf.yetanotherspeedometer.db.entity.RecordingMaxSpeedPoint;
import org.unibl.etf.yetanotherspeedometer.db.entity.RecordingPoint;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface RecordingDao {

    // TODO: Sort by time
    @Query("SELECT * FROM recording")
    LiveData<List<Recording>> getAll();

    @Insert
    Single<Long> addRecording(Recording recording);

    @Delete
    Completable deleteRecording(Recording recording);

    @Query("SELECT * FROM " +
            "recording JOIN recordingPoint ON recording.id = recordingPoint.recordingId " +
            "WHERE recording.id = :recordingId " +
            "ORDER BY recordingPoint.orderIndex")
    LiveData<Map<Recording, List<RecordingPoint>>> getRecordingWithPoints(int recordingId);

    @Query("SELECT * FROM " +
            "recording JOIN recordingMaxSpeedPoint ON recording.id = recordingMaxSpeedPoint.recordingId " +
            "WHERE recording.id = :recordingId")
    LiveData<Map<Recording, List<RecordingMaxSpeedPoint>>> getRecordingWithMaxSpeedPoint(int recordingId);
}
