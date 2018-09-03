package com.example.yeon1213.myapplication.Main.WeatherData;

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

    @SerializedName("dust")
    @Expose
    private List<Dust> dust = null;

    public List<Dust> getDust() {
        return dust;
    }

    public void setDust(List<Dust> dust) {
        this.dust = dust;
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
