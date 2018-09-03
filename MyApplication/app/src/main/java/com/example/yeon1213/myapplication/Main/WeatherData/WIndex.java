package com.example.yeon1213.myapplication.Main.WeatherData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WIndex {

    @SerializedName("wctIndex")
    @Expose
    private List<WctIndex> wctIndex = null;

    public List<WctIndex> getWctIndex() {
        return wctIndex;
    }

    public void setWctIndex(List<WctIndex> wctIndex) {
        this.wctIndex = wctIndex;
    }

    @SerializedName("heatIndex")
    @Expose
    private List<HeatIndex> heatIndex = null;

    public List<HeatIndex> getHeatIndex() {
        return heatIndex;
    }

    public void setHeatIndex(List<HeatIndex> heatIndex) {
        this.heatIndex = heatIndex;
    }
}
