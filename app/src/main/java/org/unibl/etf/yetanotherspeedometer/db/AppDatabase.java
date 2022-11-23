package org.unibl.etf.yetanotherspeedometer.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.unibl.etf.yetanotherspeedometer.db.dao.RecordingDao;
import org.unibl.etf.yetanotherspeedometer.db.entity.Recording;

@Database(entities = Recording.class, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecordingDao getRecordingDao();
}
