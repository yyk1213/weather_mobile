package com.example.yeon1213.myapplication.Main.Weather;

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

    @SerializedName("thIndex")
    @Expose
    private List<ThIndex> thIndex = null;

    public List<ThIndex> getThIndex() {
        return thIndex;
    }

    public void setThIndex(List<ThIndex> thIndex) {
        this.thIndex = thIndex;
    }

    @SerializedName("carWash")
    @Expose
    private List<CarWash> carWash = null;

    public List<CarWash> getCarWash() {
        return carWash;
    }

    public void setCarWash(List<CarWash> carWash) {
        this.carWash = carWash;
    }

    @SerializedName("uvindex")
    @Expose
    private List<UvIndex> uvIndex = null;
    @SerializedName("timeRelease")
    @Expose
    private String timeRelease;

    public List<UvIndex> getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(List<UvIndex> uvIndex) {
        this.uvIndex = uvIndex;
    }

    public String getTimeRelease() {
        return timeRelease;
    }

    public void setTimeRelease(String timeRelease) {
        this.timeRelease = timeRelease;
    }

    @SerializedName("laundry")
    @Expose
    private List<Laundry> laundry=null;

    public List<Laundry> getLaundry() {
        return laundry;
    }

    public void setLaundry(List<Laundry> laundry) {
        this.laundry = laundry;
    }
}
