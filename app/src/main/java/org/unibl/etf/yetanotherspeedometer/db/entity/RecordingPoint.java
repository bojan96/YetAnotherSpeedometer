package org.unibl.etf.yetanotherspeedometer.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recordingPoint")
public class RecordingPoint {

    @PrimaryKey(autoGenerate = true)
    public int recordingPointId;
    public int recordingId;
    public double latitude;
    public double longitude;
    public int orderIndex;
    public boolean maxSpeedPoint;
}
