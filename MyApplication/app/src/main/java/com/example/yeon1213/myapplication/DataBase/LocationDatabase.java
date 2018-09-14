package com.example.yeon1213.myapplication.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

@Database(entities={LocationData.class},version=1)
@TypeConverters({DateTypeConverter.class})
public abstract class LocationDatabase extends RoomDatabase{
    private static LocationDatabase[] INSTANCES=new LocationDatabase[8];
    private static final String[] DAY_DB={"sun.db","mon.db", "tue.db","wed.db", "thur.db","fri.db","sat.db"};

    public abstract LocationDAO getLocationDAO();

    public static LocationDatabase getDataBase(final Context context,int order) {

        if (INSTANCES[order] == null) {
            if (order == 0) {
                Log.d("instance null", "널이야");
                INSTANCES[order] = Room.databaseBuilder(context.getApplicationContext(), LocationDatabase.class, "location.db")
                        .allowMainThreadQueries()
                        .build();
            }else if(order==8){
                INSTANCES[order] = Room.databaseBuilder(context.getApplicationContext(), LocationDatabase.class, "final.db")
                        .allowMainThreadQueries()
                        .build();
            }
            else {
                INSTANCES[order] = Room.databaseBuilder(context.getApplicationContext(), LocationDatabase.class, DAY_DB[order - 1])
                        .allowMainThreadQueries()
                        .build();
            }
            return INSTANCES[order];
        }
        else {
            return INSTANCES[order];
        }
    }
}
