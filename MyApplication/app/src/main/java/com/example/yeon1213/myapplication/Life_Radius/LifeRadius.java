package com.example.yeon1213.myapplication.Life_Radius;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yeon1213.myapplication.R;

public class LifeRadius extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_radius);

        getSupportActionBar().setTitle("생활반경 설정");
    }
}
