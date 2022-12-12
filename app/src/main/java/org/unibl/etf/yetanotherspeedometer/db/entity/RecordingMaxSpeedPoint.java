package org.unibl.etf.yetanotherspeedometer.db.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "recordingMaxSpeedPoint",
    indices = @Index("recordingId"),
    foreignKeys = @ForeignKey(
            entity = Recording.class,
            parentColumns = "id",
            childColumns = "recordingId"))
public class RecordingMaxSpeedPoint {

    @PrimaryKey
    public int recordingMaxSpeedPointId;
    public int recordingId;
    public int maxSpeedPointIndex;
}
