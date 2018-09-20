package com.example.yeon1213.myapplication.DataBase;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity
public class LocationData {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int mId;
    @NonNull
    private double mLatitude;
    @NonNull
    private double mLongitude;

    private Date mDate;
    @NonNull
    private long mTime;

    private String mDay_of_week;//요일

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
    public long getMTime() {
        return mTime;
    }

    public void setMTime(@NonNull long mTime) {
        this.mTime = mTime;
    }

    public Date getMDate() {
        return mDate;
    }

    public void setMDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getMDay_of_week() {
        return mDay_of_week;
    }

    public void setMDay_of_week(String mDay_of_week) {
        this.mDay_of_week = mDay_of_week;
    }
}
