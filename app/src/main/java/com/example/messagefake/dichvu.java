package com.example.messagefake;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class dichvu extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        // Create MediaPlayer object, to play your song.
     }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        // Play song.
        Handler mHandler = new Handler();


         return START_STICKY;
    }

    @Nullable
    @Override
    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Toast.makeText(getApplicationContext(), "ccc", Toast.LENGTH_SHORT).show();
        super.onTaskRemoved(rootIntent);
    }
}

