package com.example.messagefake.notification;

import static com.example.messagefake.MainActivity.userListFromFireBases1;
import static com.example.messagefake.dataFirebase.DAO_Friebase.database_Reference;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.messagefake.R;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.example.messagefake.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class JobService extends android.app.job.JobService {
    private Boolean aBoolean=false;

    @Override
    public boolean onStartJob(final JobParameters params) {
      new Thread(new Runnable() {
          @Override
          public void run() {
              database_Reference.addValueEventListener(new ValueEventListener() {
                  @SuppressLint("NotifyDataSetChanged")
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                      Log.d("Zzzzzzzzzzzzzzz", "onDataChange: ");
                      List<user>users=new ArrayList<>();
                      for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                          user user1 = dataSnapshot.getValue(user.class);
                          users.add(user1);
                       }
                     new Thread(new Runnable() {
                         @Override
                         public void run() {
                             for (user user : users) {
                                 for (user s : new DAO_sqlite(getApplicationContext()).getUser()) {
                                     if (user.getEmail().equals(s.getEmail())) {
                                         new DAO_sqlite(getApplicationContext()).addUser(user);
                                     }

                                 }
                             }
                         }
                     }).start();

                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
              });
          }
      }).start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        aBoolean = true;
        Log.e("run", "run: stop");

        return true;
    }
}
