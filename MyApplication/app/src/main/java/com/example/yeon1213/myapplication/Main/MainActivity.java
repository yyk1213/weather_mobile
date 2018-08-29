package com.example.yeon1213.myapplication.Main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.yeon1213.myapplication.R;
import com.example.yeon1213.myapplication.Health_Weather.HealthWeather;
import com.example.yeon1213.myapplication.Life_Radius.LifeRadius;
import com.example.yeon1213.myapplication.Living_Weather.LivingWeather;
import com.example.yeon1213.myapplication.Weather_alarm.WeatherAlarm;

public class MainActivity extends AppCompatActivity {
//    private RecyclerView main_RecyclerView;
//    private RecyclerView.Adapter main_Adapter;
//    private RecyclerView.LayoutManager main_LayoutManager;
//    private List<WeatherData> weatherDataList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.setting_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.life:
                Intent i=new Intent(MainActivity.this,LivingWeather.class );
                startActivity(i);
                break;
            case R.id.health:
                Intent i1=new Intent(MainActivity.this,HealthWeather.class );
                startActivity(i1);
                break;
            case R.id.life_radius_setting:
                Intent i2=new Intent(MainActivity.this,LifeRadius.class );
                startActivity(i2);
                break;
            case R.id.weather_alarm:
                Intent i3=new Intent(MainActivity.this,WeatherAlarm.class );
                startActivity(i3);
                break;
        }
        return true;
    }

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
