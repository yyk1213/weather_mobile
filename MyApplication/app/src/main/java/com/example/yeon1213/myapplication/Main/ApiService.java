package com.example.yeon1213.myapplication.Main;

import com.example.yeon1213.myapplication.Main.Weather.Data;
import com.example.yeon1213.myapplication.Main.Weather.FineDust.FineDust;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiService {

    String BASEURL="https://api2.sktelecom.com/weather/";
    String APPKEY="6dac13f4-6c4b-499a-a48d-c5b6cdff97c2";
    //미세먼지 API
    String DUST_BASEURL="http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/";
    String DUST_APPKEY="MATtWt3RcZepkS0jjT/K7V0A3Tw4EQoCRafIiHry+VgNWmhD+LqoYZeDwHvM4c9lSAz1CM2VtogaDyCIGcTH9w==";

    @GET("current/hourly")
    Call<Data> getHourly(@Header("appKey")String appKey, @Query("version")int version,
                         @Query("lat")double lat, @Query("lon")double lon);
    //생활기상지수 받아오기
    @GET("index/wct")
    Call<Data> getWct(@Header("appKey")String appKey, @Query("version")int version,
                       @Query("lat")double lat, @Query("lon")double lon);

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
    //미세먼지 불러오기
    @GET("getMsrstnAcctoRltmMesureDnsty")
    Call<FineDust> getDust(@Query("serviceKey")String serviceKey, @Query("numOfRows")double numOfRows,
                           @Query("pageSize")double pageSize,@Query("pageNo")double pageNo,@Query("startPage")double startPage,
                           @Query("stationName")String stationName,@Query("dataTerm")String dataTerm,@Query("ver")double ver,@Query("_returnType")String _returnType);

}
