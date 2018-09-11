package com.example.yeon1213.myapplication.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities={LocationData.class},version=1)
@TypeConverters({DateTypeConverter.class})
public abstract class LocationDatabase extends RoomDatabase{
    public abstract LocationDAO getLocationDAO();
}
