package com.example.yeon1213.myapplication.Main;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class LocationService extends IntentService {
    private static final String TAG="LocaationService";
    private Location mLocation;
    private double mLatitude, mLongitude;

    private LocationListener[] mLocationListeners=new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    public static Intent newIntent(Context context){
        return new Intent(context,LocationService.class);
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public LocationService(){
        super(TAG);
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

    //내부 디비에 저장하는 코드 적기, 서비스 값 전달하고 노티 주는 코드
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG,"Received an intent: "+intent);
        //위치 매니저 초기화
        LocationManager mLocationManager =(LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //위치 정보 수신 체크--나중에 생각
//        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},//액티비티 고치기
//                    MY_PERMISSION_ACCESS_FINE_LOCATION);
//        }
//        //나중에 생각하기
//        //최근 위치 정보 확인-- 현재 위치를 찾을 수 없을 때
//        Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        Location location1 = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        //최근 위치 정보의 위도와 경도를 받아야하나?
//        if (location != null) {
//            mLatitude = location.getLatitude();
//            mLongitude = location.getLongitude();
//            Log.d("위도,경도1", "위도값,경도값" + mLatitude + " " + mLongitude);
//        }

        try{
            //GPS 제공자 정보가 바뀌면 콜백하도록 리스너 등록
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, mLocationListeners[0]);
        }catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        try{
            //GPS 제공자 정보가 바뀌면 콜백하도록 리스너 등록
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 1, mLocationListeners[1]);
        }catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        //위치정보 미 수신할 때 자원해제
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
        //코드가 이해가 안감
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("latitude",getLatitude());
        intent.putExtra("longtitude",getLongitude());
        startActivity(intent);
    }
}
