package com.example.yeon1213.myapplication.Main;

import android.app.Activity;
import android.arch.persistence.room.Room;
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

import com.example.yeon1213.myapplication.DataBase.LocationDAO;
import com.example.yeon1213.myapplication.DataBase.LocationDatabase;
import com.example.yeon1213.myapplication.Location_Analysis.PersonalLocationService;
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

    private RecyclerView main_RecyclerView;
    private RecyclerView.Adapter main_Adapter;
    private RecyclerView.LayoutManager main_LayoutManager;

    private List<String> recycler_livingData = new ArrayList<>();
    private WeatherData main_weatherData;

    private LocationDatabase database;

    public static Intent newIntent(Context context){
        return new Intent(context,MainActivity.class);
    }
    //좀 더 함수로 나누기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //서비스 반복 실행--언제까지 자료수집해야할지 생각하기
        if(!LocationService.isServiceAlarmOn(this)){
            LocationService.setServiceAlarm(this,true);
        }

        initView();
        main_weatherData = new WeatherData();

        location_check();
        Intent i= PersonalLocationService.newIntent(this);
        startService(i);

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

    private void location_check(){
       database=LocationDatabase.getDataBase(this,0);

       //기존에 저장된 DB 값이 있을 경우-- 패턴 값 분석해서 그 DB 값 불러오기
        if(database.getLocationDAO().getLocation().size()>0) {
//        if(database.isOpen()){//열리면--왜 안열렸다고 나오는거야
           LocationDAO locationDAO = database.getLocationDAO();
           latitude = locationDAO.getLocation().get(0).getMLatitude();
           longitude = locationDAO.getLocation().get(0).getMLongitude();
           Log.d("메인 위경도", "" + latitude + "," + longitude);
//        }
       }
       else{//기존에 패턴 분석 DB가 없을 경우-- 우선 현재 위치를 불러오고 서비스에 위치값 저장 시작
            //현재 위치 값 불러오는 클래스 따로 만들기--서비스, 액티비티에서 쓰임
            //우선은 특정 값으로 고정시켜 놓기
            latitude = 36.1234;
            longitude=127.1234;
        }
    }
}