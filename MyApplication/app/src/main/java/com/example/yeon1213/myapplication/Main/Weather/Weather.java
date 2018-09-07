package com.example.yeon1213.myapplication.Main.Weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {

    @SerializedName("hourly")
    @Expose
    private List<Hourly> hourly = null;

    public List<Hourly> getHourly() {
        return hourly;
    }

    public void setHourly(List<Hourly> hourly) {
        this.hourly = hourly;
    }

    @SerializedName("wIndex")
    @Expose
    private WIndex wIndex;

    public WIndex getWIndex() {
        return wIndex;
    }

    public void setWIndex(WIndex wIndex) {
        this.wIndex = wIndex;
    }
}
