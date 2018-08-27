package com.example.yeon1213.myapplication;

public class Weather {
    private String temperature;

    public Weather(){
    }

    public Weather(String temperature){
        this.temperature=temperature;
    }

    public String getWeather() {
        return temperature;
    }

    public void setWeather(String tem) {
        this.temperature = tem;
    }
}
