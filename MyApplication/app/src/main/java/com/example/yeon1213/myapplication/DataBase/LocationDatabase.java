package com.example.yeon1213.myapplication.DataBase;

import android.app.Service;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities={LocationData.class},version=1)
@TypeConverters({DateTypeConverter.class})
public abstract class LocationDatabase extends RoomDatabase{
    private static LocationDatabase INSTANCE;

    public abstract LocationDAO getLocationDAO();

    public static LocationDatabase getDataBase(Service service){
        if(INSTANCE==null) return null;
        else return INSTANCE;
    }
}
