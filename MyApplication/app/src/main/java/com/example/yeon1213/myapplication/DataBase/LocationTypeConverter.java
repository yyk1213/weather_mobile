package com.example.yeon1213.myapplication.DataBase;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class LocationTypeConverter {

    Gson gson=new Gson();

    @TypeConverter
    public String convertListToString(List<String> list) {

        return gson.toJson(list);
    }

    @TypeConverter
    public List<String> convertStringToList(String str){
        Type listType=new TypeToken<List<String>>() {}.getType();

        return gson.fromJson(str,listType);
    }
}
