package com.example.messagefake.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        createChannelId();
    }

    private void createChannelId() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel=new NotificationChannel("1","Noti", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//hoặc             NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }
}
