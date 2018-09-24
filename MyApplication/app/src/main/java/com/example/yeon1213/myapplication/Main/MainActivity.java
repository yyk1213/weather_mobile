package com.example.yeon1213.myapplication.Main;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.provider.CalendarContract;
import android.support.v4.app.NotificationCompat;
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
import com.example.yeon1213.myapplication.Life_Radius.LifeRadiusActivity;
import com.example.yeon1213.myapplication.Living_Weather.LivingWeather;
import com.example.yeon1213.myapplication.Weather_alarm.WeatherAlarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private TextView temperature, fine_dust, precipitation, humidity, wind,city,county,village;
    private double latitude, longitude;

    private RecyclerView main_RecyclerView;
    private RecyclerView.Adapter main_Adapter;
    private RecyclerView.LayoutManager main_LayoutManager;

    private List<String> recycler_livingData = new ArrayList<>();
    private WeatherData main_weatherData;

    private LocationDatabase database=LocationDatabase.getDataBase(this,0);

    //좀 더 함수로 나누기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //서비스 반복 실행--언제까지 자료수집해야할지 생각하기
//        if(!LocationService.isServiceAlarmOn(this)){
//            LocationService.setServiceAlarm(this,true);
//        }

        initView();
        main_weatherData = new WeatherData();

        location_check();
//        Intent i= PersonalLocationService.newIntent(this);
//        startService(i);

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
                main_Adapter = new MainAdapter(recycler_livingData);
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

        //날씨 값 가져오기
        main_weatherData.getData(latitude,longitude);
        //좌표를 주소로 변환
        final Geocoder geocoder=new Geocoder(this);
        List<Address> list=null;
        try {
            list = geocoder.getFromLocation(latitude, longitude, 10);
            getSupportActionBar().setTitle(list.get(0).getAddressLine(0));
        }catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }
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
                Intent i2 = new Intent(MainActivity.this, LifeRadiusActivity.class);
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
            //위치 받아오는 거는 서비스에서만 실행해서 그 값을 불러온다.
            //우선은 특정 값으로 고정시켜 놓기
            latitude = 36.1234;
            longitude=127.1234;
        }
    }

    private void createNotification(){
        //현재 시간 1시간 후와 db에 저장된 시간이 일치할 경우 noti를 띄워서 날 씨를 알려준다.

        Calendar now=Calendar.getInstance();
        int hour=now.get(Calendar.HOUR_OF_DAY);
        int minute=now.get(Calendar.MINUTE);
        boolean is24hour=true;

        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(this);

    }
}