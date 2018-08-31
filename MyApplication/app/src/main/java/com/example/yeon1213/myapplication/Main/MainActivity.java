package com.example.yeon1213.myapplication.Main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.yeon1213.myapplication.Main.WeatherData.Example;
import com.example.yeon1213.myapplication.Main.WeatherData.Hourly;
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
    //LocationManager locationManager;
    private double latitude = 36.1234;
    private double longitude = 127.1234;
     int temp=4;
    // private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    // private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    //    private RecyclerView main_RecyclerView;
//    private RecyclerView.Adapter main_Adapter;
//    private RecyclerView.LayoutManager main_LayoutManager;
    private List<Hourly> weatherDataList = new ArrayList<>(); //날씨 데이터 넣는 것

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        getWeather(latitude, longitude);
        //locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

//        LocationListener locationListener=new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                latitude=location.getLatitude();
//                longitude=location.getLongitude();
//                Log.d("위도,경도","위도값,경도값"+longitude);
//                getWeather(latitude,longitude);
//
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//
//        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
//
//            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
//                    MY_PERMISSION_ACCESS_COARSE_LOCATION );
//        }
        //locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER,0,0,MIN_TIME_BW_UPDATES,this);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        //getWeather(latitude,longitude);
//        main_RecyclerView=(RecyclerView)findViewById(R.id.main_recycler_view);
//        main_RecyclerView.setHasFixedSize(true);
//
//        main_LayoutManager=new LinearLayoutManager(getApplicationContext());
//        main_RecyclerView.setLayoutManager(main_LayoutManager);
//
//        main_Adapter =new MyAdapter(weatherDataList);
//        main_RecyclerView.setAdapter(main_Adapter);

        //WeatherData();

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

    //데이터 형식 바꿔서 다시 작성
    private void getWeather(double latitude, double longitude) {
        Log.d("getWeather 들어옴","ㅇㅇㅇ");

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(ApiService.BASEURL).build();
        Log.d("retrofit 생성","ㅇㅇㅇ");
        ApiService apiService = retrofit.create(ApiService.class);

        Log.d("apiservice 생성","ㅇㅇㅇ");
        Call<Example> call = apiService.getHourly(ApiService.APPKEY, 2, latitude, longitude);//여기서 안됨

        Log.d("call 생성","ooo");
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                Log.d("onResponse 들어옴","dd");
                if (response.isSuccessful()) {
                    //시간별 데이터를 받음
                    weatherDataList = response.body().getWeather().getHourly();
                    Log.d("Hourly DATA 결과",""+weatherDataList.get(temp).getTemperature().getTc());
                    if (weatherDataList != null) {
                        temperature.setText(weatherDataList.get(temp).getTemperature().getTc());
                    }
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });

//    private void WeatherData(){
//        WeatherData weatherData =new WeatherData("온도: 35");
//        weatherDataList.add(weatherData);
//
//        weatherData =new WeatherData("온도: 40");
//        weatherDataList.add(weatherData);
//
//        main_Adapter.notifyDataSetChanged();
//    }
    }
}