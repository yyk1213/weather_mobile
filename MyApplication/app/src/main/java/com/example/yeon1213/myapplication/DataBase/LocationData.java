package com.example.yeon1213.myapplication.DataBase;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

@Entity
public class LocationData {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int mId;

    private double mLatitude;

    private double mLongitude;
    @NonNull
    private String mLocation_name;
    @NonNull
    private String mTime;
    @NonNull
    @TypeConverters(LocationTypeConverter.class)
    private List<String> mDay_of_week;//요일

    private boolean mAlarmCheck;

    public boolean getMAlarmCheck() {
        return mAlarmCheck;
    }

    public void setMAlarmCheck(boolean mAlarmCheck) {
        this.mAlarmCheck = mAlarmCheck;
    }

    @NonNull
    public int getMId() {
        return mId;
    }

    public void setMId(@NonNull int mId) {
        this.mId = mId;
    }

    public double getMLatitude() {
        return mLatitude;
    }

    public void setMLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getMLongitude() {
        return mLongitude;
    }

    public void setMLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    @NonNull
    public String getMTime() {
        return mTime;
    }

    public void setMTime(@NonNull String mTime) {
        this.mTime = mTime;
    }

    @NonNull
    public List<String> getMDay_of_week() {
        return mDay_of_week;
    }

    public void setMDay_of_week(@NonNull List<String> mDay_of_week) {
        this.mDay_of_week = mDay_of_week;
    }

    @NonNull
    public String getMLocation_name() {
        return mLocation_name;
    }

    public void setMLocation_name(@NonNull String mLocation_name) {
        this.mLocation_name = mLocation_name;
    }
}
