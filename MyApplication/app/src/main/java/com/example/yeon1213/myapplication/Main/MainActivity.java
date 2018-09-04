package com.example.yeon1213.myapplication.Main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.yeon1213.myapplication.Main.WeatherData.Data;
import com.example.yeon1213.myapplication.Main.WeatherData.Dust;
import com.example.yeon1213.myapplication.Main.WeatherData.Weather;
import com.example.yeon1213.myapplication.R;
import com.example.yeon1213.myapplication.Health_Weather.HealthWeather;
import com.example.yeon1213.myapplication.Life_Radius.LifeRadius;
import com.example.yeon1213.myapplication.Living_Weather.LivingWeather;
import com.example.yeon1213.myapplication.Weather_alarm.WeatherAlarm;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView temperature, fine_dust, precipitation, humidity, wind;
    private double latitude;
    private double longitude;

    int temp = 0;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    private RecyclerView main_RecyclerView;
    private RecyclerView.Adapter main_Adapter;
    private RecyclerView.LayoutManager main_LayoutManager;

    private Weather weatherData; //날씨 데이터 넣는 것
    private List<Dust> dustData;//미세먼지 데이터
    private String heatData;//생활기상 지수
    private List<String> livingData=new ArrayList<>();//생활기상 지수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        LocationManager locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        List<String> list=locationManager.getAllProviders();
        Log.d("위치 제공자", "" + list);

        //위치 정보 수신 체크
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    MY_PERMISSION_ACCESS_FINE_LOCATION );
        }
        //최근 위치 정보 확인
        Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //최근 위치 정보의 위도와 경도를 받아야하나?
        if(location!=null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            Log.d("위도, 경도",""+latitude+" "+longitude);
            //날씨 데이터 받아오기
            //getData(latitude, longitude);
        }
        getData(37.5233198,127.05453650000004);

        //위치 리스너 구현
       LocationListener locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude=location.getLatitude();
                longitude=location.getLongitude();
                Log.d("위도,경도","위도값,경도값"+latitude+" "+longitude);
                //getData(latitude,longitude);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        //GPS 제공자 정보가 바뀌면 콜백하도록 리스너 등록
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,1,locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,1,locationListener);

        //위치정보 미 수신할 때 자원해제
        //locationManager.removeUpdates(locationListener);

        //기상 지수를 담는 리사이클러 뷰
        main_RecyclerView=findViewById(R.id.main_recycler_view);
        main_RecyclerView.setHasFixedSize(true);

        main_LayoutManager=new GridLayoutManager(getApplicationContext(),3);
        main_RecyclerView.setLayoutManager(main_LayoutManager);

        main_Adapter =new MyAdapter(livingData);
        main_RecyclerView.setAdapter(main_Adapter);

        //위치값에 따라 이름 바뀌게
        getSupportActionBar().setTitle("강남구 청담동");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.life:
                Intent i = new Intent(MainActivity.this, LivingWeather.class);
                startActivity(i);
                break;
            case R.id.health:
                Intent i1 = new Intent(MainActivity.this, HealthWeather.class);
                startActivity(i1);
                break;
            case R.id.life_radius_setting:
                Intent i2 = new Intent(MainActivity.this, LifeRadius.class);
                startActivity(i2);
                break;
            case R.id.weather_alarm:
                Intent i3 = new Intent(MainActivity.this, WeatherAlarm.class);
                startActivity(i3);
                break;
        }
        return true;
    }

    private void initView() {
        temperature = findViewById(R.id.temperature);
        fine_dust = findViewById(R.id.fine_dust);
        precipitation = findViewById(R.id.precipitation);
        humidity = findViewById(R.id.humidity);
        wind = findViewById(R.id.wind);
    }

    private void getData(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(ApiService.BASEURL).build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<Data> call = apiService.getHourly(ApiService.APPKEY, 2, latitude, longitude);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()) {
                    //시간별 데이터를 받음-- 나중에 분별로 바꾸기
                    weatherData = response.body().getWeather();
                    Log.d("Hourly DATA 결과", "" + weatherData.getHourly().get(temp).getGrid().getVillage());
                    if (weatherData != null) {
                        temperature.setText(weatherData.getHourly().get(temp).getTemperature().getTc());
                        precipitation.setText("강수량: " + weatherData.getHourly().get(temp).getPrecipitation().getSinceOntime());
                        humidity.setText("습도: " + weatherData.getHourly().get(temp).getHumidity());
                        wind.setText("바람: " + weatherData.getHourly().get(temp).getWind().getWdir());
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });

        Call<Data> call_heat = apiService.getHeat(ApiService.APPKEY, 2, latitude, longitude);

        call_heat.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call_heat, Response<Data> response) {

                if (response.isSuccessful()) {
                    Log.d("데이터 성공?","");
                    //시간별 데이터를 받음
                    heatData = response.body().getWeather().getWIndex().getHeatIndex().get(0).getCurrent().getIndex();
                    Log.d("열지수",""+ heatData);
                    if (heatData != null) {
                        livingData.add(heatData);

                        //데이터 바뀐거 알리기
                        main_Adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call_heat, Throwable t) {

            }
        });
    }

    //미세먼지 받아오는 함수
//    private void getDust() {
//        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(ApiService.BASEURL).build();
//        ApiService apiService = retrofit.create(ApiService.class);
//
//        Call<Data> call = apiService.getDust(ApiService.APPKEY, 2, lat_string, long_string);
//
//        call.enqueue(new Callback<Data>() {
//            @Override
//            public void onResponse(Call<Data> call, Response<Data> response) {
//
//                if (response.isSuccessful()) {
//                    //시간별 데이터를 받음
//                    dustData = response.body().getData().getDust();
//                    Log.d("미세먼지",""+dustData.get(0).getPm10().getGrade());
//                    if (dustData != null) {
//                        fine_dust.setText(dustData.get(0).getPm10().getGrade());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Data> call, Throwable t) {
//
//            }
//        });
//    }
}