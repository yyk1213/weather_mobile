package com.example.yeon1213.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView main_RecyclerView;
    private RecyclerView.Adapter main_Adaper;
    private RecyclerView.LayoutManager main_LayoutManager;
    private List<Weather> weatherList=new ArrayList<>();
    //private String[] myDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar=(Toolbar)findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);

        main_RecyclerView=(RecyclerView)findViewById(R.id.main_recycler_view);
        main_RecyclerView.setHasFixedSize(true);

        main_LayoutManager=new LinearLayoutManager(getApplicationContext());
        main_RecyclerView.setLayoutManager(main_LayoutManager);

        //myDataset[]={"날씨"};

        main_Adaper=new MyAdaper(weatherList);
        main_RecyclerView.setAdapter(main_Adaper);

        WeatherData();
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
                Intent i=new Intent(MainActivity.this,living_weather.class );
                startActivity(i);
                break;
            case R.id.health:
                Intent i1=new Intent(MainActivity.this,health_weather.class );
                startActivity(i1);
                break;
            case R.id.life_radius_setting:
                Intent i2=new Intent(MainActivity.this,life_radius.class );
                startActivity(i2);
                break;
            case R.id.weather_alarm:
                Intent i3=new Intent(MainActivity.this,weather_alarm.class );
                startActivity(i3);
                break;
        }
        return true;
    }

    private void WeatherData(){
        Weather weather=new Weather("온도: 35");
        weatherList.add(weather);

        weather=new Weather("온도: 40");
        weatherList.add(weather);

        main_Adaper.notifyDataSetChanged();
    }
}
