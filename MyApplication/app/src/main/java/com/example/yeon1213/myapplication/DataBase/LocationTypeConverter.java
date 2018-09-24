package com.example.yeon1213.myapplication.DataBase;

import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

public class LocationTypeConverter {

    @TypeConverter
    public static String convertListToString(List<String> list) {

        Gson gson=new Gson();
        String json=gson.toJson(list);

        return json;
    }
}
