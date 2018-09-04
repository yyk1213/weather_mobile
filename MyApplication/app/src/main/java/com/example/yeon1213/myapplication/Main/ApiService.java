package com.example.yeon1213.myapplication.Main;

import com.example.yeon1213.myapplication.Main.WeatherData.Data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiService {

    static final String BASEURL="https://api2.sktelecom.com/weather/";
    static final String APPKEY="6dac13f4-6c4b-499a-a48d-c5b6cdff97c2";

    @GET("current/hourly")
    Call<Data> getHourly(@Header("appKey")String appKey, @Query("version")int version,
                         @Query("lat")double lat, @Query("lon")double lon);

    //미세먼지 api 제공안해주는 것 같음-- 기상청에서 받아오기
    @GET("dust")
    Call<Data> getDust(@Header("appKey")String appKey, @Query("version")int version,
                       @Query("lat")String lat, @Query("lon")String lon);
    //생활기상지수 받아오기
    @GET("index/heat")
    Call<Data> getHeat(@Header("appKey")String appKey, @Query("version")int version,
                       @Query("lat")double lat, @Query("lon")double lon);

    @GET("index/th")
    Call<Data> getTh(@Header("appKey")String appKey, @Query("version")int version,
                     @Query("lat")double lat, @Query("lon")double lon);

    @GET("index/carwash")
    Call<Data> getCarwash(@Header("appKey")String appKey, @Query("version")int version,
                          @Query("lat")double lat, @Query("lon")double lon);

    @GET("index/uv")
    Call<Data> getUv(@Header("appKey")String appKey, @Query("version")int version,
                     @Query("lat")double lat, @Query("lon")double lon);

    @GET("index/laundry")
    Call<Data> getLaundry(@Header("appKey")String appKey, @Query("version")int version,
                          @Query("lat")double lat, @Query("lon")double lon);

}
