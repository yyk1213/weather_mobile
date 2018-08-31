package com.example.yeon1213.myapplication.Main;

import com.example.yeon1213.myapplication.Main.WeatherData.Example;
import com.example.yeon1213.myapplication.Main.WeatherData.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiService {

    static final String BASEURL="https://api2.sktelecom.com/";
    static final String APPKEY="6dac13f4-6c4b-499a-a48d-c5b6cdff97c2";

    @GET("weather/current/Hourly")
    Call<Example> getHourly(@Header("appKey")String appKey, @Query("version")int version,
                            @Query("lat")double lat, @Query("lon")double lon);
}
