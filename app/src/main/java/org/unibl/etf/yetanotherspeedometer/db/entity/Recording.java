package org.unibl.etf.yetanotherspeedometer.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recording")
public class Recording {
    @PrimaryKey
    public int id;
    public double avgSpeed;
    public double maxSpeed;
    public long totalDistance;
    public long elapsedTime;
}
