package com.example.yeon1213.myapplication.Main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.yeon1213.myapplication.Life_Radius.LifeRadiusActivity;
import com.example.yeon1213.myapplication.Living_Weather.LivingWeather;
import com.example.yeon1213.myapplication.R;
import com.example.yeon1213.myapplication.Weather_alarm.WeatherAlarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.yeon1213.myapplication.Living_Weather.LivingWeather.EXTRA_ACTIVITY_POSITION;

public class MainActivity extends AppCompatActivity{
    private TextView temperature, fine_dust, precipitation, humidity, wind;
    private double latitude, longitude;

    private RecyclerView main_RecyclerView;
    private RecyclerView.Adapter main_Adapter;
    private RecyclerView.LayoutManager main_LayoutManager;

    private List<String> recycler_livingData = new ArrayList<>();
    private WeatherData main_weatherData;

    public static final String EXTRA_LATITUDE="com.example.yeon1213.myapplication.Alarm.latitude";
    public static final String EXTRA_LONGITUDE="com.example.yeon1213.myapplication.Alarm.longtitude";

    private LocationListener[] mLocationListeners;

    public static Intent newIntent(Context context, double latitude, double longitude){
        Intent mainIntent = new Intent(context, MainActivity.class);

        mainIntent.putExtra(EXTRA_LATITUDE,latitude);
        mainIntent.putExtra(EXTRA_LONGITUDE, longitude);

        return mainIntent;
    }

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
        main_weatherData.setContext(this);

        location_check();
        //주소로 액션바 타이틀 변경
        reverse_address();

//        Intent i= PersonalLocationService.newIntent(this);
//        startService(i);

        //데이터 가져오면 값 넣기
        ResponseListener responseListener=new ResponseListener() {
            @Override
            public void onWeatherResponseAvailable() {
                temperature.setText(main_weatherData.getTemperature());
                fine_dust.setText(main_weatherData.getDust()+" "+main_weatherData.getDust_comment());
                precipitation.setText(main_weatherData.getPrecipitation());
                humidity.setText(main_weatherData.getHumidity());
                wind.setText(main_weatherData.getWind());
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

        main_LayoutManager = new LinearLayoutManager(getApplicationContext());
        main_RecyclerView.setLayoutManager(main_LayoutManager);

        //날씨 값 가져오기
        main_weatherData.getWeatherAPIData(latitude,longitude);
        //선택 지수 값 가져오기
       main_weatherData.getIndexData(latitude,longitude);
    }

    private void reverse_address(){
        //좌표를 주소로 변환
        final Geocoder geocoder=new Geocoder(this);
        List<Address> list=null;
        try {
            list = geocoder.getFromLocation(latitude, longitude, 10);
            getSupportActionBar().setTitle(list.get(0).getAddressLine(0).substring(5));
        }catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        inflater.inflate(R.menu.action_button, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.life:
                Intent lifeIntent = new Intent(MainActivity.this, LivingWeather.class);
                lifeIntent.putExtra(EXTRA_ACTIVITY_POSITION,item.getItemId());

                startActivity(lifeIntent);
                break;
            case R.id.health:
                Intent healthIntent = new Intent(MainActivity.this, LivingWeather.class);
                healthIntent.putExtra(EXTRA_ACTIVITY_POSITION,item.getItemId());

                startActivity(healthIntent);
                break;
            case R.id.life_radius_setting:
                Intent lifeSettingIntent = new Intent(MainActivity.this, LifeRadiusActivity.class);
                startActivity(lifeSettingIntent);
                break;
            case R.id.weather_alarm:
                Intent weatherAlarmIntent = new Intent(MainActivity.this, WeatherAlarm.class);
                startActivity(weatherAlarmIntent);
                break;

            case R.id.change_location:
                getCurrentLocation();
                recycler_livingData.clear();

                //날씨 값 가져오기
                main_weatherData.getWeatherAPIData(latitude,longitude);
                //선택 지수 값 가져오기
                main_weatherData.getIndexData(latitude,longitude);
                reverse_address();
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

    //현재위치 받아오기
    private void location_check(){

        //database=LocationDatabase.getDataBase(this);

//       //기존에 저장된 DB 값이 있을 경우-- 패턴 값 분석해서 그 DB 값 불러오기--현재 위치 값으로 불러오기
//        if(database.getLocationDAO().getLocation().size()>0) {
////        if(database.isOpen()){//열리면--왜 안열렸다고 나오는거야
//           LocationDAO locationDAO = database.getLocationDAO();
//           latitude = locationDAO.getLocation().get(0).getMLatitude();
//           longitude = locationDAO.getLocation().get(0).getMLongitude();
//           Log.d("메인 위경도", "" + latitude + "," + longitude);
////        }
//       }
//       else{//기존에 패턴 분석 DB가 없을 경우-- 우선 현재 위치를 불러오고 서비스에 위치값 저장 시작
            //위치 받아오는 거는 서비스에서만 실행해서 그 값을 불러온다.
            //우선은 특정 값으로 고정시켜 놓기
//        }

        //들어온 인텐트 값이 있으면
        if((getIntent().getDoubleExtra(EXTRA_LATITUDE,0.0)!=0.0) && (getIntent().getDoubleExtra(EXTRA_LONGITUDE,0.0)!=0.0)){
            latitude=getIntent().getDoubleExtra(EXTRA_LATITUDE,0.0);
            longitude=getIntent().getDoubleExtra(EXTRA_LONGITUDE,0.0);
        }
        else{
            //현재위치 값 구하기
            getCurrentLocation();
        }
    }

    class LocationListener implements android.location.LocationListener {
        Location mLocation;

        public LocationListener(String provider) {
            mLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            mLocation.set(location);

            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
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
    }

    private void getCurrentLocation(){

        //네트워크 연결 가능한지 확인
        if(!isNetworkAvailable()) return;

        //위치 정보 수신 체크--안돼있으면 어떡하지?
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //위치 매니저 초기화
        LocationManager mLocationManager =(LocationManager)getSystemService(Context.LOCATION_SERVICE);

        LocationListener[]  mLocationListeners=new LocationListener[]{
                new LocationListener(LocationManager.GPS_PROVIDER),
                new LocationListener(LocationManager.NETWORK_PROVIDER)
        };

        try{
            //GPS 제공자 정보가 바뀌면 콜백하도록 리스너 등록
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, mLocationListeners[0]);
        }catch (java.lang.SecurityException ex) {
            Log.i("MainActivity", "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d("MainActivity", "network provider does not exist, " + ex.getMessage());
        }

        try{
            //GPS 제공자 정보가 바뀌면 콜백하도록 리스너 등록
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 1, mLocationListeners[1]);
        }catch (java.lang.SecurityException ex) {
            Log.i("MainActivity", "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d("MainActivity", "network provider does not exist, " + ex.getMessage());
        }

        //최근 위치 정보 확인-- 현재 위치를 찾을 수 없을 때만 실행
        Location[] locations={ mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER),mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)};

        for(int i=0; i<locations.length; i++) {
            if (locations[i] != null) {
                latitude = locations[i].getLatitude();
                longitude = locations[i].getLongitude();
            }
        }

        //위치정보 미 수신할 때 자원해제
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i("MainActivity", "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager cm=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable=cm.getActiveNetworkInfo()!=null;
        boolean isNetworkConnected=isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }
}