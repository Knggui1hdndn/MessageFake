package com.example.messagefake.notification;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.messagefake.MainActivity;
import com.example.messagefake.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        RemoteMessage.Notification notification = message.getNotification();
        if (notification == null) {
            return;
        }
//        String s3 = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//        int x1 = TextUtils.lastIndexOf(s3, '@');
//        String s2 = TextUtils.substring(s3, 0, x1);
        Uri s1 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), s1);
        r.play();
//        //JobService
//        ComponentName componentName=new ComponentName(this, JobService.class);
//        JobInfo jobInfo=new JobInfo.Builder(0,componentName).
//                setPersisted(true).setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED).build();
//        JobScheduler jobScheduler= (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//        jobScheduler.schedule(jobInfo);
        String stile = notification.getTitle();
        String body = notification.getBody();


            sendNotification(stile, body);
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplication(), R.raw.a);
            mediaPlayer.start();

    }


    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(String stile, String body) {
        //thao tác trên notification
        Intent intent = new Intent(this, MainActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag")

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.S ? PendingIntent.FLAG_UPDATE_CURRENT : PendingIntent.FLAG_IMMUTABLE);
        Notification.Builder builder = new Notification.Builder(this, "1")
                .setContentTitle(stile)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_baseline_add_to_home_screen_24)
                .setShowWhen(true);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Để làm cho thông báo xuất hiện
        if (notificationManager != null) {
            notificationManager.notify((int) new Date().getTime(), notification);
        }

    }


    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("onNewToken", "onNewToken: " + token);
        super.onNewToken(token);
    }
}
