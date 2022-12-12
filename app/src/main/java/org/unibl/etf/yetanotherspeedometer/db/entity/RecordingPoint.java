package org.unibl.etf.yetanotherspeedometer.db.entity;

import static androidx.room.ForeignKey.NO_ACTION;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "recordingPoint",
        indices = @Index("recordingId"),
        foreignKeys = @ForeignKey(
                entity = Recording.class,
                parentColumns = "id",
                childColumns = "recordingId",
                onDelete = ForeignKey.CASCADE))
public class RecordingPoint {

    @PrimaryKey(autoGenerate = true)
    public int recordingPointId;
    public int recordingId;
    public double latitude;
    public double longitude;
    public int orderIndex;
}
