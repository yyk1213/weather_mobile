package com.example.yeon1213.myapplication.Weather_alarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yeon1213.myapplication.R;

public class WeatherAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_alarm);

        getSupportActionBar().setTitle("날씨 알림 설정");
    }
}
