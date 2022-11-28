package org.unibl.etf.yetanotherspeedometer.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recording")
public class Recording {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public double avgSpeed;
    public double maxSpeed;
    public double totalDistance;
    public long elapsedTime;
}
