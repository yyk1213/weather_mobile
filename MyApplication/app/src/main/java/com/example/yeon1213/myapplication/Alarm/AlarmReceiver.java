package com.example.yeon1213.myapplication.Alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.yeon1213.myapplication.Main.MainActivity;
import com.example.yeon1213.myapplication.R;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG="AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
       //알람 시간이 되었을 때 onReceive를 호출함
        Toast.makeText(context, "왔따!@!@", Toast.LENGTH_SHORT).show();

        NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,new Intent(context, MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("날씨 알림")
                .setContentText("날씨 내용")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(1, notification);
    }
}
