package com.example.yeon1213.myapplication.DataBase;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import java.lang.annotation.Inherited;

public interface BaseDao<T> {

    @Insert
    public void insert(T t);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    public void update(T t);

    @Delete
    public void delete(T t);
}
