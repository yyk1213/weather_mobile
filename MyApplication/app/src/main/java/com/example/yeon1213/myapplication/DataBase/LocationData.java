package com.example.yeon1213.myapplication.DataBase;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;
@Entity(tableName = "location")
public class LocationData {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int mId;
    @NonNull
    private double mLatitude;
    @NonNull
    private double mLongitude;
    @NonNull
    private Date mDate;
    @NonNull
    private long mTime;
    @NonNull
    private String mDay_of_week;//요일

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
    public long getMTime() {
        return mTime;
    }

    public void setMTime(@NonNull long mTime) {
        this.mTime = mTime;
    }

    @NonNull
    public Date getMDate() {
        return mDate;
    }

    public void setMDate(@NonNull Date mDate) {
        this.mDate = mDate;
    }

    @NonNull
    public String getMDay_of_week() {
        return mDay_of_week;
    }

    public void setMDay_of_week(@NonNull String mDay_of_week) {
        this.mDay_of_week = mDay_of_week;
    }
}
