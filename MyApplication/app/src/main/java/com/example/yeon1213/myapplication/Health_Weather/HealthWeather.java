package com.example.yeon1213.myapplication.Health_Weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yeon1213.myapplication.R;

public class HealthWeather extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_weather);

        getSupportActionBar().setTitle("보건기상 지수 선택");
    }
}
