package com.example.yeon1213.myapplication.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

@Database(entities={LocationData.class},version=1)
@TypeConverters({LocationTypeConverter.class})
public abstract class LocationDatabase extends RoomDatabase{

    private static LocationDatabase INSTANCES;
    //private static final String[] DAY_DB={"sun.db","mon.db", "tue.db","wed.db", "thur.db","fri.db","sat.db"};

    public abstract LocationDAO getLocationDAO();

    public static LocationDatabase getDataBase(final Context context) {

        if (INSTANCES == null) {
            Log.d("instance null", "널이야");
            INSTANCES = Room.databaseBuilder(context.getApplicationContext(), LocationDatabase.class, "location.db")
                    .allowMainThreadQueries()
                    .build();
        }
            return INSTANCES;
    }
}
