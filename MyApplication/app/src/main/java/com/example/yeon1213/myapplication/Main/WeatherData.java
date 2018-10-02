package com.example.yeon1213.myapplication.Main;

import android.util.Log;

import com.example.yeon1213.myapplication.Main.Weather.Data;
import com.example.yeon1213.myapplication.Main.Weather.FineDust.FineDust;
import com.example.yeon1213.myapplication.Main.Weather.Weather;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherData{

    private Weather weatherData; //날씨 데이터 넣는 것
    private List<String> livingData=new ArrayList<>();//리사이클러뷰에 담는 생활기상지수
    private ResponseListener mListener;//응답 값이 왔는지 확인하는 리스너
    //날씨 데이터
    private String temperature;
    private String precipitation;
    private String humidity;
    private String wind;
    //생활기상지수 데이터
    private String heatIndex;
    private String wctIndex;
    private String thIndex;
    private String carWash;
    private String uvIndex;
    private String laundry;
    //미세먼지
    private  String dust;
    private int temp=0;

    public WeatherData() {
    }

    public Weather getWeatherData() {
        return weatherData;
    }

    public List<String> getLivingData() {
        return livingData;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getWind() {
        return wind;
    }

    public void setmListener(ResponseListener mListener) {
        this.mListener = mListener;
    }

    public String getDust() {
        return dust;
    }

    public void getData(double latitude, double longitude) {

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(ApiService.BASEURL).build();
        ApiService apiService = retrofit.create(ApiService.class);
        //시간별 날씨---분단위로 바꾸기
        Call<Data> call = apiService.getHourly(ApiService.APPKEY, 2, latitude, longitude);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()) {
                    //시간별 데이터를 받음-- 나중에 분별로 바꾸기
                    weatherData = response.body().getWeather();

                    Log.d("Hourly DATA 결과", "" + weatherData.getHourly().get(temp).getGrid().getVillage());
                    if (weatherData != null) {
                        temperature = weatherData.getHourly().get(temp).getTemperature().getTc();
                        Log.d("함수 온도", "" + temperature);
                        precipitation = weatherData.getHourly().get(temp).getPrecipitation().getSinceOntime();
                        humidity = weatherData.getHourly().get(temp).getHumidity();
                        wind = weatherData.getHourly().get(temp).getWind().getWdir();

                        mListener.onWeatherResponseAvailable();
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                //프로그래스바 날씨 데이터를 받아오고 있습니다 띄우기
                Log.e("fail", "" + t.toString());
            }
        });
        //열지수
        Call<Data> call_heat = apiService.getHeat(ApiService.APPKEY, 2, latitude, longitude);
        call_heat.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call_heat, Response<Data> response) {
                if (response.isSuccessful()) {
                    heatIndex = response.body().getWeather().getWIndex().getHeatIndex().get(0).getCurrent().getIndex();
                    Log.d("열지수", "" + heatIndex);
                    if (heatIndex != null) {
                        livingData.add("열지수: " + heatIndex);
                        mListener.onIndexResponseAvailable();
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call_heat, Throwable t) {
                Log.e("fail", "" + t.toString());
            }
        });

        Call<Data> call_wct = apiService.getWct(ApiService.APPKEY, 2, latitude, longitude);
        call_wct.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call_wct, Response<Data> response) {
                if (response.isSuccessful()) {
                    wctIndex = response.body().getWeather().getWIndex().getWctIndex().get(0).getCurrent().getIndex();
                    Log.d("wctIndex", "" + wctIndex);
                    if (wctIndex != null) {
                        livingData.add("체감온도: " + wctIndex);
                        mListener.onIndexResponseAvailable();
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call_wct, Throwable t) {

            }
        });

        Call<Data> call_thIndex = apiService.getTh(ApiService.APPKEY, 2, latitude, longitude);
        call_thIndex.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call_thIndex, Response<Data> response) {
                if (response.isSuccessful()) {
                    thIndex = response.body().getWeather().getWIndex().getThIndex().get(0).getCurrent().getIndex();
                    Log.d("thIndex", "" + thIndex);
                    if (thIndex != null) {
                        livingData.add("불쾌지수: " + thIndex);
                        mListener.onIndexResponseAvailable();
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call_thIndex, Throwable t) {

            }
        });

        Call<Data> call_carwash = apiService.getCarwash(ApiService.APPKEY, 2, latitude, longitude);
        call_carwash.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call_carwash, Response<Data> response) {

                if (response.isSuccessful()) {
                    carWash = response.body().getWeather().getWIndex().getCarWash().get(0).getComment();
                    Log.d("세차지수", "" + carWash);
                    if (carWash != null) {
                        livingData.add("세차지수:" + carWash);
                        mListener.onIndexResponseAvailable();
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call_carwash, Throwable t) {
                Log.e("fail", "" + t.toString());
            }
        });

//        Call<Data> call_uvIndex = apiService.getUv(ApiService.APPKEY, 2, latitude, longitude);
//        call_uvIndex.enqueue(new Callback<Data>() {
//            @Override
//            public void onResponse(Call<Data> call_uvIndex, Response<Data> response) {
//                if (response.isSuccessful()) {
//                    uvIndex = response.body().getWeather().getWIndex().getUvIndex().get(0).getDay00().getComment();
//                    Log.d("uvIndex", "" + uvIndex);
//                    if (uvIndex != null) {
//                        livingData.add("자외선지수: " + uvIndex);
//                        mListener.onIndexResponseAvailable();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Data> call_uvIndex, Throwable t) {
//
//            }
//        });
//        Call<Data> call_Laundry = apiService.getLaundry(ApiService.APPKEY, 2, latitude, longitude);
//
//        call_Laundry.enqueue(new Callback<Data>() {
//            @Override
//            public void onResponse(Call<Data> call_Laundry, Response<Data> response) {
//
//                if (response.isSuccessful()) {
//                    Log.d("데이터 성공?", "");
//                    //시간별 데이터를 받음
//                    laundry = response.body().getWeather().getWIndex().getUvIndex().get(0).getDay01().getComment();
//                    Log.d("laundry", "" + laundry);
//                    if (laundry != null) {
//                        livingData.add("빨래지수" + laundry);
//                        mListener.onIndexResponseAvailable();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Data> call_Laundry, Throwable t) {
//
//            }
//        });
//
//        //통신 가로채서 값 보여주는 것
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
//
//        Retrofit dust_retrofit = new Retrofit.Builder().client(client).addConverterFactory(GsonConverterFactory.create()).baseUrl(ApiService.DUST_BASEURL).build();
//        ApiService dust_apiService = dust_retrofit.create(ApiService.class);
//
//        Call<FineDust> call_dust = dust_apiService.getDust(ApiService.DUST_APPKEY, 10, 10, 1, 1, "강남구", "DAILY", 1.3, "json");
//        call_dust.enqueue(new Callback<FineDust>() {
//            @Override
//            public void onResponse(Call<FineDust> call_dust, Response<FineDust> response) {
//                Log.d("미세먼지", "응답");
//                if (response.isSuccessful()) {
//                    //통합대기환경지수
//                    dust = response.body().getList().get(0).getKhaiGrade();
//                    Log.d("미세먼지", "" + dust);
//                    mListener.onIndexResponseAvailable();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<FineDust> call_dust, Throwable t) {
//                Log.e("미세먼지 에러", "" + t.toString());
//            }
//        });
//    }
    }
}

