package com.example.yeon1213.myapplication.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface LocationDAO extends BaseDao<LocationData> {

    @Query("SELECT * FROM LocationData")
    List<LocationData> getLocation();

    @Query("SELECT * FROM LocationData Where mId =:ID")
    LocationData getData(int ID);
//    sgdfgsdfg
}
