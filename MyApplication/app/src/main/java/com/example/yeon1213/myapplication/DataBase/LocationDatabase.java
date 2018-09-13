package com.example.yeon1213.myapplication.DataBase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

@Database(entities={LocationData.class},version=1)
@TypeConverters({DateTypeConverter.class})
public abstract class LocationDatabase extends RoomDatabase{
    private static LocationDatabase INSTANCE;

    public abstract LocationDAO getLocationDAO();

    public static LocationDatabase getDataBase(final Context context){
        if(INSTANCE==null) {
            Log.d("instance null","널이야");
//            synchronized (LocationDatabase.class){
//                if(INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),LocationDatabase.class,"location.db")
                            .allowMainThreadQueries()
                            .build();
                    Log.d("빌드가 왜 안되지","됐나 안됐나");
//                }
//            }
        }
            return INSTANCE;
    }
}
