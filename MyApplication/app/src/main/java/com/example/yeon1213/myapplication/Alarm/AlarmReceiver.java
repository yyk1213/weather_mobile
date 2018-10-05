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

public class AlarmReceiver extends BroadcastReceiver implements ResponseListener {

    private static final String TAG = "AlarmReceiver";
    private LocationDatabase locationDatabase;
    public static final String EXTRA_ALARM_ID = "com.example.yeon1213.myapplication.Alarm.alarm_id";
    private WeatherData receiver_weather_data;
    private String location_weather = "";
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
        location_weather += "온도:" + receiver_weather_data.getTemperature() + " ";
        location_weather += "강수량:" + receiver_weather_data.getPrecipitation() + " ";

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

        receiver_weather_data.getWeatherAPIData(locationData.getMLatitude(), locationData.getMLongitude());

        location_name = locationData.getMLocation_name();

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        receiver_weather_data.setmListener(this);
    }
}
