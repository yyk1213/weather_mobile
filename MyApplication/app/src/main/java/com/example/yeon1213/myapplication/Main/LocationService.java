package com.example.yeon1213.myapplication.Main;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.yeon1213.myapplication.DataBase.LocationDAO;
import com.example.yeon1213.myapplication.DataBase.LocationDatabase;
import com.example.yeon1213.myapplication.DataBase.LocationData;

import java.util.Calendar;
import java.util.Date;

public class LocationService extends IntentService{

    private static final String Location_Service ="location service";
    private Location mLocation;
    private double mLatitude, mLongitude;
    private Long mTime;
    private Date mDate;
    private String mDay_Of_Week;
    private static final int LOCATION_INTERVAL=1000*60;//1분으로 해보기

    public LocationService(){
        super(Location_Service);
    }

    private LocationListener[] mLocationListeners=new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    public static Intent newIntent(Context context){
        return new Intent(context,LocationService.class);
    }

    //알람 메서드 추가
    public static void setServiceAlarm(Context context, boolean isOn){
        Intent i= LocationService.newIntent(context);
        PendingIntent pi=PendingIntent.getService(context,0,i,0);
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        if(isOn){
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime(),LOCATION_INTERVAL,pi);
        }else{
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    //알람이 켜져있는지 확인
    public static boolean isServiceAlarmOn(Context context) {
        Intent i = LocationService.newIntent(context);
        PendingIntent pi = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    private class LocationListener implements android.location.LocationListener{

        public LocationListener(String provider) {
            mLocation=new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
                mLocation.set(location);
                mLatitude=mLocation.getLatitude();
                mLongitude=mLocation.getLongitude();
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

    //노티 주는 코드 추가, 기능별로 나누기
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(Location_Service,"Received an intent: "+intent);
        //네트워크 연결 가능한지 확인
        if(!isNetworkAvailable()) return;

        //위치 매니저 초기화
        LocationManager mLocationManager =(LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //위치 정보 수신 체크--안돼있으면 어떡하지?
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          return;
        }

        //최근 위치 정보 확인-- 현재 위치를 찾을 수 없을 때만 실행
        Location[] locations={ mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER),mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)};

        for(int i=0; i<locations.length; i++) {
            if (locations[i] != null) {
                mLatitude = locations[i].getLatitude();
                mLongitude = locations[i].getLongitude();
                Log.d("위도,경도1", "위도값,경도값" + mLatitude + " " + mLongitude);
            }
        }

        try{
            //GPS 제공자 정보가 바뀌면 콜백하도록 리스너 등록
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, mLocationListeners[0]);
        }catch (java.lang.SecurityException ex) {
            Log.i(Location_Service, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(Location_Service, "network provider does not exist, " + ex.getMessage());
        }

        try{
            //GPS 제공자 정보가 바뀌면 콜백하도록 리스너 등록
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 1, mLocationListeners[1]);
        }catch (java.lang.SecurityException ex) {
            Log.i(Location_Service, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(Location_Service, "network provider does not exist, " + ex.getMessage());
        }

        mTime=System.currentTimeMillis();//시간 보기 좋게 변경해야 하나?
        mDate=new Date(mTime);
        mDay_Of_Week=getDayOfWeek();

        //위치정보 미 수신할 때 자원해제
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(Location_Service, "fail to remove location listners, ignore", ex);
                }
            }
        }

        //위치 정보 디비에 넣기
        LocationData locationData=new LocationData();

        locationData.setMLatitude(mLatitude);
        locationData.setMLongitude(mLongitude);
        locationData.setMTime(mTime);
        locationData.setMDate(mDate);
        locationData.setMDay_of_week(mDay_Of_Week);

//        if(LocationDatabase.getDataBase(this).isOpen()){//왜 안열리지??
            LocationDAO locationDAO= LocationDatabase.getDataBase(this).getLocationDAO();
            locationDAO.insert(locationData);

            Log.d("DB",""+locationDAO.getLocation().get(0).getMDate());//날짜랑 시간도 들어오는데 조금 다른거 같음
//        }

        //노티 주는 코드
//        Resources resources=getResources();
//        Intent i=MainActivity.newIntent(this);
//        PendingIntent pi=PendingIntent.getActivity(this,0,i,0);
//        Notification notification=new NotificationCompat().Builder(this).

    }

    public boolean isNetworkAvailable(){
        ConnectivityManager cm=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable=cm.getActiveNetworkInfo()!=null;
        boolean isNetworkConnected=isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }

    private String getDayOfWeek(){
        Calendar cal=Calendar.getInstance();
        String week=null;

        int nWeek=cal.get(Calendar.DAY_OF_WEEK);
        if(nWeek==1){
            week="일";
        }else if(nWeek==2){
            week="월";
        }else if(nWeek==3){
            week="화";
        }else if(nWeek==4){
            week="수";
        }else if(nWeek==5){
            week="목";
        }else if(nWeek==6){
            week="금";
        }else if(nWeek==7){
            week="토";
        }

        return week;
    }
}
