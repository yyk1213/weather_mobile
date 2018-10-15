package com.example.yeon1213.myapplication.Main;

import android.content.Context;
import android.content.SharedPreferences;
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

import static android.content.Context.MODE_PRIVATE;

public class WeatherData{

    private Weather weatherData; //날씨 데이터 넣는 것
    private List<String> livingData=new ArrayList<>();//리사이클러뷰에 담는 생활기상지수
    private ResponseListener mListener;//응답 값이 왔는지 확인하는 리스너
    private Context context;
    //날씨 데이터
    private String temperature;
    private String precipitation;
    private String humidity;
    private String wind;
    private String station;
    //생활기상지수 데이터
    private String heatIndex;
    private String wctIndex;
    private String thIndex;
    private String carWash;
    private String uvIndex;
    private String laundry;
    //미세먼지
    private String dust;
    private String dust_comment;
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

    public String getDust_comment() {
        return dust_comment;
    }

    public void setDust_comment(String dust_comment) {
        this.dust_comment = dust_comment;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void getWeatherAPIData(double latitude, double longitude){

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(ApiService.BASEURL).build();
        ApiService apiService = retrofit.create(ApiService.class);
        //시간별 날씨
        Call<Data> call = apiService.getMinutely(ApiService.APPKEY, 2, latitude, longitude);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()) {
                    //시간별 데이터를 받음-- 나중에 분별로 바꾸기
                    weatherData = response.body().getWeather();

                    if (weatherData != null) {
                        temperature = weatherData.getMinutely().get(temp).getTemperature().getTc();
                        precipitation = weatherData.getMinutely().get(temp).getPrecipitation().getSinceOntime();
                        humidity = weatherData.getMinutely().get(temp).getHumidity();
                        wind = weatherData.getMinutely().get(temp).getWind().getWspd();
                        //station=weatherData.getMinutely().get(temp).getStation().getName();

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

        //통신 가로채서 값 보여주는 것
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit dust_retrofit = new Retrofit.Builder().client(client).addConverterFactory(GsonConverterFactory.create()).baseUrl(ApiService.DUST_BASEURL).build();
        ApiService dust_apiService = dust_retrofit.create(ApiService.class);
        //minutely의 station 값을 미세먼지가 늦게 받아와서 오류가 뜬다.// 어떻게 받을지 생각하기...
        Call<FineDust> call_dust = dust_apiService.getDust(ApiService.DUST_APPKEY, 10, 10, 1, 1, "강남구", "DAILY", 1.3, "json");
        call_dust.enqueue(new Callback<FineDust>() {
            @Override
            public void onResponse(Call<FineDust> call_dust, Response<FineDust> response) {
                if (response.isSuccessful()) {
                    //통합대기환경지수
                    dust = response.body().getList().get(0).getKhaiGrade();
                    dust_comment=commentFineDust(dust);

                    Log.d("미세먼지", "" + dust);
                    mListener.onWeatherResponseAvailable();
                }
            }

            @Override
            public void onFailure(Call<FineDust> call_dust, Throwable t) {
                Log.e("미세먼지 에러", "" + t.toString());
            }
        });
    }
    public void getHealthIndex(){

        //통신 가로채서 값 보여주는 것
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder().client(client).addConverterFactory(GsonConverterFactory.create()).baseUrl(ApiService.HEALTH_BASEURL).build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<JsonObject> flower_object=apiService.getFolwer(ApiService.DUST_APPKEY,1100000000,"json");
        flower_object.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                        String result=response.body().getAsJsonObject("Response").getAsJsonObject("body").getAsJsonObject("indexModel").get("today").toString();
                        Log.d(""," "+result);
                    }
                }
            @Override
            public void onFailure(Call<JsonObject> flower_object, Throwable t) {
                //프로그래스바 날씨 데이터를 받아오고 있습니다 띄우기
                Log.e("fail", "" + t.toString());
            }
        });
    }

    public void getIndexData(double latitude, double longitude) {

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(ApiService.BASEURL).build();
        ApiService apiService = retrofit.create(ApiService.class);

        SharedPreferences indexSetting_pref=context.getSharedPreferences("index_setting", MODE_PRIVATE);
        boolean fire_Index=indexSetting_pref.getBoolean("열지수",false);

        if(fire_Index) {
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
        }

        boolean tem_index=indexSetting_pref.getBoolean("체감온도",false);

        if(tem_index) {

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
        }

        boolean uncomfortable_index=indexSetting_pref.getBoolean("불쾌지수",false);

        if(uncomfortable_index) {

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
        }

        boolean carwash_index=indexSetting_pref.getBoolean("세차지수",false);

        if(carwash_index) {

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
        }

        boolean uv_Index=indexSetting_pref.getBoolean("자외선지수",false);

        if(uv_Index) {

            Call<Data> call_uvIndex = apiService.getUv(ApiService.APPKEY, 2, latitude, longitude);
            call_uvIndex.enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call_uvIndex, Response<Data> response) {
                    if (response.isSuccessful()) {
                        uvIndex = response.body().getWeather().getWIndex().getUvIndex().get(0).getDay01().getComment();
                        Log.d("uvIndex", "" + uvIndex);
                        if (uvIndex != null) {
                            livingData.add("자외선지수: " + uvIndex);
                            mListener.onIndexResponseAvailable();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Data> call_uvIndex, Throwable t) {

                }
            });
        }

        boolean laundry_index=indexSetting_pref.getBoolean("빨래지수",false);

        if(laundry_index) {

            Call<Data> call_Laundry = apiService.getLaundry(ApiService.APPKEY, 2, latitude, longitude);

            call_Laundry.enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call_Laundry, Response<Data> response) {

                    if (response.isSuccessful()) {
                        //시간별 데이터를 받음
                        laundry = response.body().getWeather().getWIndex().getLaundry().get(0).getDay01().getComment();
                        if (laundry != null) {
                            livingData.add("빨래지수: " + laundry);
                            mListener.onIndexResponseAvailable();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Data> call_Laundry, Throwable t) {

                }
            });

        }
    }

    private String commentFineDust(String dust){
        if(dust.equals("1")) return "좋음";
        else if(dust.equals("2")) return "보통";
        else if(dust.equals("3")) return "나쁨";
        else if(dust.equals("4")) return "매우 나쁨";

        return "";
    }
}

