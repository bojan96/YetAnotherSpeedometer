package org.unibl.etf.yetanotherspeedometer.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "recording")
public class Recording {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public double avgSpeed;
    public double maxSpeed;
    public double totalDistance;
    public long elapsedTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recording recording = (Recording) o;
        return id == recording.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
