package com.example.yeon1213.myapplication.Location_Analysis;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.yeon1213.myapplication.DataBase.LocationData;
import com.example.yeon1213.myapplication.DataBase.LocationDatabase;

import java.util.List;

public class PersonalLocationService extends IntentService {
    //백그라운드에서 진행돼야 함
    //우선은 서비스로 한번만 진행
    private List<LocationData> locationDatabase;
    private static final String Personal_Service = "personal service";

    public PersonalLocationService() {
        super(Personal_Service);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, PersonalLocationService.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(Personal_Service, "Received an intent: " + intent);

        location_save();
    }

    //DB에 저장된 위치를 요일별 DB에 나눠서 저장
    private void location_save() {

        locationDatabase = LocationDatabase.getDataBase(this, 0).getLocationDAO().getLocation();

        //요일별로 분류해서 각 DB에 넣기
        for (int i = 0; i < locationDatabase.size(); i++) {

            LocationData locationData = locationDatabase.get(i);

            LocationData dayData = new LocationData();

            dayData.setMLatitude(locationData.getMLatitude());
            dayData.setMLongitude(locationData.getMLongitude());
            dayData.setMDate(locationData.getMDate());
            dayData.setMTime(locationData.getMTime());

            String day_of_week = locationData.getMDay_of_week();

            switch (day_of_week) {
                case "일":
                    LocationDatabase.getDataBase(this, 1).getLocationDAO().insert(dayData);
                    LocationDatabase.getDataBase(this, 1).getLocationDAO().update(dayData);
                    break;
                case "월":
                    LocationDatabase.getDataBase(this, 2).getLocationDAO().insert(dayData);
                    LocationDatabase.getDataBase(this, 2).getLocationDAO().update(dayData);
                    break;
                case "화":
                    LocationDatabase.getDataBase(this, 3).getLocationDAO().insert(dayData);
                    LocationDatabase.getDataBase(this, 3).getLocationDAO().update(dayData);
                    break;
                case "수":
                    LocationDatabase.getDataBase(this, 4).getLocationDAO().insert(dayData);
                    LocationDatabase.getDataBase(this, 4).getLocationDAO().update(dayData);
                    break;
                case "목":
                    LocationDatabase.getDataBase(this, 5).getLocationDAO().insert(dayData);
                    LocationDatabase.getDataBase(this, 5).getLocationDAO().update(dayData);
                    break;
                case "금":
                    LocationDatabase.getDataBase(this, 6).getLocationDAO().insert(dayData);
                    LocationDatabase.getDataBase(this, 6).getLocationDAO().update(dayData);
                    break;
                case "토":
                    LocationDatabase.getDataBase(this, 7).getLocationDAO().insert(dayData);
                    LocationDatabase.getDataBase(this, 7).getLocationDAO().update(dayData);
                    break;
            }
        }
    }

    //나눠진 DB에서 위경도가 달라진 시간대 파악
    private void checkLocation(LocationDatabase dayDatabase) {
        //요일 디비
        List<LocationData> dayDB = dayDatabase.getLocationDAO().getLocation();
        //사용자 패턴이 담길 최종디비
        LocationDatabase final_database = LocationDatabase.getDataBase(this, 8);

        for (int i = 1; i < dayDB.size(); i++) {
            double latitude = dayDB.get(i).getMLatitude();
            double longtitude = dayDB.get(i).getMLongitude();

            double latitude_next = dayDB.get(i + 1).getMLatitude();
            double longtitude_next = dayDB.get(i + 1).getMLongitude();
            //다르면 최종 db에 저장
            if ((latitude != latitude_next) || (longtitude != longtitude_next)) {

                LocationData personal_data=new LocationData();

                personal_data.setMLatitude(latitude);
                personal_data.setMLongitude(longtitude);
                personal_data.setMTime(dayDB.get(i).getMTime());
                personal_data.setMDay_of_week(dayDB.get(i).getMDay_of_week());

                final_database.getLocationDAO().insert(personal_data);
                //마지막 케이스만
                if(i==dayDB.size()-1){
                    personal_data.setMLatitude(latitude_next);
                    personal_data.setMLongitude(longtitude_next);
                    personal_data.setMTime(dayDB.get(i+1).getMTime());
                    personal_data.setMDay_of_week(dayDB.get(i+1).getMDay_of_week());

                    final_database.getLocationDAO().insert(personal_data);
                }
            }
        }


    }
}
