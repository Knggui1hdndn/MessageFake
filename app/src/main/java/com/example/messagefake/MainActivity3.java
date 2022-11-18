package com.example.messagefake;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.messagefake.Account.login;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences=getSharedPreferences("ok",MODE_PRIVATE);
        if(preferences.getString("ok","").equals("Light1")){
            setTheme(R.style.Theme_Light1);
        }else{
            setTheme(R.style.Theme_Light);

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
//        FirebaseApp.initializeApp(/*context=*/ this);
//        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
//        firebaseAppCheck.installAppCheckProviderFactory(
//                SafetyNetAppCheckProviderFactory.getInstance());

      Handler handler=  new Handler();
      Runnable runnable= () -> {
          startActivity(new Intent(MainActivity3.this, login.class));
          finish();

      };
        handler.postDelayed(runnable,1500);
     }


}