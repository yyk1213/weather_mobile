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

import com.example.yeon1213.myapplication.R;
import com.example.yeon1213.myapplication.Health_Weather.HealthWeather;
import com.example.yeon1213.myapplication.Life_Radius.LifeRadius;
import com.example.yeon1213.myapplication.Living_Weather.LivingWeather;
import com.example.yeon1213.myapplication.Weather_alarm.WeatherAlarm;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private TextView temperature, fine_dust, precipitation, humidity, wind;
    private double latitude, longitude;

    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    private RecyclerView main_RecyclerView;
    private RecyclerView.Adapter main_Adapter;
    private RecyclerView.LayoutManager main_LayoutManager;

    private List<String> recycler_livingData = new ArrayList<>();
    private WeatherData main_weatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        main_weatherData = new WeatherData();
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        List<String> list = locationManager.getAllProviders();
        Log.d("위치 제공자 확인", "" + list);

        //위치 정보 수신 체크
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }
        //최근 위치 정보 확인
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //최근 위치 정보의 위도와 경도를 받아야하나?
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("위도,경도1", "위도값,경도값" + latitude + " " + longitude);
        }
        //데이터 가져오면 값 넣기
        ResponseListener responseListener=new ResponseListener() {
            @Override
            public void onWeatherResponseAvailable() {
                temperature.setText(main_weatherData.getTemperature());
                fine_dust.setText(main_weatherData.getDust());
                precipitation.setText("강수량: " + main_weatherData.getPrecipitation());
                humidity.setText("습도: " + main_weatherData.getHumidity());
                wind.setText("풍량: " + main_weatherData.getWind());
            }

            @Override
            public void onIndexResponseAvailable() {
                recycler_livingData = main_weatherData.getLivingData();
                main_Adapter = new MaiinAdapter(recycler_livingData);
                main_RecyclerView.setAdapter(main_Adapter);
                main_Adapter.notifyDataSetChanged();
            }
        };

        main_weatherData.setmListener(responseListener);

        //위치 리스너 구현
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 1, locationListener);

        //위치정보 미 수신할 때 자원해제
        locationManager.removeUpdates(locationListener);

        //기상 지수를 담는 리사이클러 뷰
        main_RecyclerView = findViewById(R.id.main_recycler_view);
        main_RecyclerView.setHasFixedSize(true);

        main_LayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        main_RecyclerView.setLayoutManager(main_LayoutManager);

        //위치값에 따라 이름 바뀌게
        getSupportActionBar().setTitle("강남구 청담동");
        //날씨 값 가져오기
        main_weatherData.getData(latitude,longitude);
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
}