package com.example.yeon1213.myapplication.Main.WeatherData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Common {

    @SerializedName("alertYn")
    @Expose
    private String alertYn;
    @SerializedName("stormYn")
    @Expose
    private String stormYn;

    public String getAlertYn() {
        return alertYn;
    }

    public void setAlertYn(String alertYn) {
        this.alertYn = alertYn;
    }

    public String getStormYn() {
        return stormYn;
    }

    public void setStormYn(String stormYn) {
        this.stormYn = stormYn;
    }
}
