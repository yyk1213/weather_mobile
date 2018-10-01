package com.example.yeon1213.myapplication.Alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.yeon1213.myapplication.DataBase.LocationData;
import com.example.yeon1213.myapplication.DataBase.LocationDatabase;
import com.example.yeon1213.myapplication.Main.MainActivity;
import com.example.yeon1213.myapplication.Main.MainAdapter;
import com.example.yeon1213.myapplication.Main.ResponseListener;
import com.example.yeon1213.myapplication.Main.Weather.Weather;
import com.example.yeon1213.myapplication.Main.WeatherData;
import com.example.yeon1213.myapplication.R;

public class AlarmReceiver extends BroadcastReceiver implements ResponseListener{

    private static final String TAG="AlarmReceiver";
    private LocationDatabase locationDatabase;
    public static final String EXTRA_ALARM_ID="com.example.yeon1213.myapplication.Alarm.alarm_id";
    private WeatherData receiver_weather_data;
    private String location_weather="";
    private NotificationManager notificationManager;
    private Notification notification;

    public static Intent newIntent(Context context, int id){
        Intent receiverIntent=new Intent(context,BroadcastReceiver.class);
        receiverIntent.putExtra(EXTRA_ALARM_ID,id);

        return receiverIntent;
    }

//    //데이터 가져오면 값 넣기
//    ResponseListener responseListener=new ResponseListener() {
//        @Override
//        public void onWeatherResponseAvailable() {
//            location_weather +="온도: "+weather_data.getTemperature()+" ";
//            location_weather +="강수량: "+weather_data.getPrecipitation()+" ";
//
//        }
//
//        @Override
//        public void onIndexResponseAvailable() {
//
//        }
//    };

    @Override
    public void onWeatherResponseAvailable() {
        location_weather +="온도:"+receiver_weather_data.getTemperature()+" ";
        location_weather +="강수량:"+receiver_weather_data.getPrecipitation()+" ";

        notificationManager.notify(1, notification);
        Log.d("노티 내용",""+location_weather);
    }

    @Override
    public void onIndexResponseAvailable() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        locationDatabase= LocationDatabase.getDataBase(context);
        int id=intent.getIntExtra(EXTRA_ALARM_ID,0);

        Log.e("리시버 id 값", ""+id);
        receiver_weather_data=new WeatherData();

        LocationData locationData=locationDatabase.getLocationDAO().getData(id);

        receiver_weather_data.setmListener(this);
        receiver_weather_data.getData(locationData.getMLatitude(),locationData.getMLongitude());

        String location_name=locationData.getMLocation_name();

        notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,new Intent(context, MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);

        notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(location_name)
                .setContentText(location_weather)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
    }
}
