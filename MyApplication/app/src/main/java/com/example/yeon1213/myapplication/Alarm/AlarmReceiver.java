package com.example.yeon1213.myapplication.Alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.example.yeon1213.myapplication.DataBase.LocationData;
import com.example.yeon1213.myapplication.DataBase.LocationDatabase;
import com.example.yeon1213.myapplication.Main.MainActivity;
import com.example.yeon1213.myapplication.Main.ResponseListener;
import com.example.yeon1213.myapplication.Main.WeatherData;
import com.example.yeon1213.myapplication.R;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.example.yeon1213.myapplication.Main.MainActivity.EXTRA_LATITUDE;
import static com.example.yeon1213.myapplication.Main.MainActivity.EXTRA_LONGITUDE;

public class AlarmReceiver extends BroadcastReceiver implements ResponseListener {

    private static final String TAG = "AlarmReceiver";
    private LocationDatabase locationDatabase;
    public static final String EXTRA_ALARM_ID = "com.example.yeon1213.myapplication.Alarm.alarm_id";

    private WeatherData receiver_weather_data;
    private String location_weather;
    private NotificationManager notificationManager;
    private Notification notification;
    private PendingIntent pendingIntent;
    private String location_name;
    private Context noti_context;

    public static Intent newIntent(Context context, int id) {
        Intent receiverIntent = new Intent(context, BroadcastReceiver.class);
        receiverIntent.putExtra(EXTRA_ALARM_ID, id);

        return receiverIntent;
    }

    @Override
    public void onWeatherResponseAvailable() {

            location_weather="";

            if(receiver_weather_data.getTemperature()!=null) {
                location_weather += "온도:" + receiver_weather_data.getTemperature() + " ";
            }

            if(receiver_weather_data.getPrecipitation()!=null) {
                location_weather += "강수량:" + receiver_weather_data.getPrecipitation() + " ";
            }

            else if(receiver_weather_data.getTemperature()==null && receiver_weather_data.getPrecipitation()==null)
                location_weather+="정보 제공 불가";

        notification = new NotificationCompat.Builder(noti_context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setContentTitle(location_name)
                .setContentText(location_weather)
                .setVibrate(new long[]{1000, 1000, 1000})
                .setAutoCancel(true)
                .build();

        notificationManager.notify(1, notification);
    }

    @Override
    public void onIndexResponseAvailable() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        locationDatabase = LocationDatabase.getDataBase(context);
        int id = intent.getIntExtra(EXTRA_ALARM_ID, 0);

        noti_context = context;

        receiver_weather_data = new WeatherData();

        LocationData locationData = locationDatabase.getLocationDAO().getData(id);

        double latitude=locationData.getMLatitude();
        double longitude=locationData.getMLongitude();

        receiver_weather_data.getWeatherAPIData(latitude, longitude);

        location_name = locationData.getMLocation_name();

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);

        Intent main_intent=new Intent(context,MainActivity.class);
        main_intent.putExtra(EXTRA_LATITUDE,latitude);
        main_intent.putExtra(EXTRA_LONGITUDE,longitude);
        //main_intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        pendingIntent = PendingIntent.getActivity(context, 0, main_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        receiver_weather_data.setmListener(this);
    }
}
