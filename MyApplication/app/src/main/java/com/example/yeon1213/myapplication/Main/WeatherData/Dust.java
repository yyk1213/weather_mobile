package com.example.yeon1213.myapplication.Main.WeatherData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dust {
    @SerializedName("timeObservation")
    @Expose
    private String timeObservation;
    @SerializedName("station")
    @Expose
    private Station station;
    @SerializedName("pm10")
    @Expose
    private Pm10 pm10;

    public String getTimeObservation() {
        return timeObservation;
    }

    public void setTimeObservation(String timeObservation) {
        this.timeObservation = timeObservation;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Pm10 getPm10() {
        return pm10;
    }

    public void setPm10(Pm10 pm10) {
        this.pm10 = pm10;
    }
}
