package com.example.yeon1213.myapplication.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface LocationDAO {
    @Insert
    public void insert(LocationData... locationData);
    @Update
    public void update(LocationData... locationData);

    @Query("SELECT * FROM location")
    public List<LocationData> getLocation();
}
