package com.example.yeon1213.myapplication.Main.WeatherData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WctIndex {
    @SerializedName("grid")
    @Expose
    private Grid grid;
    @SerializedName("current")
    @Expose
    private Current current;
    @SerializedName("forecast")
    @Expose
    private Forecast forecast;

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }
}
