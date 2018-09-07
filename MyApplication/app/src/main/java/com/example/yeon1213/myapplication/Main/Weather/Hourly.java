package com.example.yeon1213.myapplication.Main.Weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hourly {

    @SerializedName("grid")
    @Expose
    private Grid grid;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("precipitation")
    @Expose
    private Precipitation precipitation;
    @SerializedName("sky")
    @Expose
    private Sky sky;
    @SerializedName("temperature")
    @Expose
    private Temperature temperature;
    @SerializedName("humidity")
    @Expose
    private String humidity;
    @SerializedName("lightning")
    @Expose
    private String lightning;
    @SerializedName("timeRelease")
    @Expose
    private String timeRelease;
    @SerializedName("sunRiseTime")
    @Expose
    private String sunRiseTime;
    @SerializedName("sunSetTime")
    @Expose
    private String sunSetTime;

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Precipitation getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Precipitation precipitation) {
        this.precipitation = precipitation;
    }

    public Sky getSky() {
        return sky;
    }

    public void setSky(Sky sky) {
        this.sky = sky;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getLightning() {
        return lightning;
    }

    public void setLightning(String lightning) {
        this.lightning = lightning;
    }

    public String getTimeRelease() {
        return timeRelease;
    }

    public void setTimeRelease(String timeRelease) {
        this.timeRelease = timeRelease;
    }

    public String getSunRiseTime() {
        return sunRiseTime;
    }

    public void setSunRiseTime(String sunRiseTime) {
        this.sunRiseTime = sunRiseTime;
    }

    public String getSunSetTime() {
        return sunSetTime;
    }

    public void setSunSetTime(String sunSetTime) {
        this.sunSetTime = sunSetTime;
    }
}
