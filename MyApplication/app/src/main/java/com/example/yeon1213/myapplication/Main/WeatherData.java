package com.example.yeon1213.myapplication.Main;

public class WeatherData {
    private String temperature;
    private int imageUrl;

    public WeatherData(){
    }

    public WeatherData(String temperature,int imgUrl){
        this.temperature=temperature;
        this.imageUrl=imgUrl;
    }

    public String getWeather() {
        return temperature;
    }

    public int getImageUrl() {
        return imageUrl;
    }
}
