package com.example.yeon1213.myapplication.DataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.support.v4.text.util.LinkifyCompat;

import java.util.Date;
import java.util.List;

@Entity
public class LocationData {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "mId")
    private int mId;
    @NonNull
    @ColumnInfo(name = "mLatitude")
    private double mLatitude;
    @NonNull
    @ColumnInfo(name = "mLongitude")
    private double mLongitude;

    @ColumnInfo(name = "mLocation_name")
    @NonNull
    private String mLocation_name;

    @ColumnInfo(name="mLocation_address")
    @NonNull
    private String mLocation_address;

    @NonNull
    @ColumnInfo(name = "mTime")
    private String mTime;

    @NonNull
    @ColumnInfo(name = "mDay_of_week")
    private int mDay_of_week;

    @ColumnInfo(name = "mAlarmCheck")
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
    @NonNull
    public double getMLatitude() {
        return mLatitude;
    }

    public void setMLatitude(@NonNull double mLatitude) {
        this.mLatitude = mLatitude;
    }
    @NonNull
    public double getMLongitude() {
        return mLongitude;
    }

    public void setMLongitude(@NonNull double mLongitude) {
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
    public String getMLocation_name() {
        return mLocation_name;
    }

    public void setMLocation_name(@NonNull String mLocation_name) {
        this.mLocation_name = mLocation_name;
    }

    @NonNull
    public int getMDay_of_week() {
        return mDay_of_week;
    }

    public void setMDay_of_week(@NonNull int mDay_of_week) {
        this.mDay_of_week = mDay_of_week;
    }

    @NonNull
    public String getMLocation_address() {
        return mLocation_address;
    }

    public void setMLocation_address(@NonNull String mLocation_address) {
        this.mLocation_address = mLocation_address;
    }
}
