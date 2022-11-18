package com.example.messagefake.I;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.messagefake.MainActivity3;
import com.example.messagefake.R;

public class DarkMode extends AppCompatActivity {
    RadioButton buttonTat, buttonBat;
    int i = 0;
    SharedPreferences preferences ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = getSharedPreferences("ok", MODE_PRIVATE);
        if (preferences.getString("ok", "").equals("Light1")) {
            setTheme(R.style.Theme_Light1);
            i = 1;
        } else {
            setTheme(R.style.Theme_Light);
            i = 0;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dark_mode);
        setToolBar();
        buttonTat = findViewById(R.id.rdiTat);
        buttonBat = findViewById(R.id.rdiBat);

        if (i == 1) {
            buttonBat.setChecked(true);
        }

        buttonTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog̣("Light");

             }
        });

        buttonBat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog̣("Light1");

            }
        });
    }
    private void dialog̣(String theme){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Thay đổi giao diện?\nẤn reset để thay đổi cài đặt.") .setCancelable(false).setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                preferences.edit().clear().commit();
                preferences.edit().putString("ok", theme).apply();
                finish();
                startActivity(new Intent(DarkMode.this, MainActivity3.class));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 1) {
                    buttonBat.setChecked(true);
                }else{
                    buttonTat.setChecked(true);

                }
                dialogInterface.cancel();
            }
        }) ;
       builder.show();




    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.batdau2, R.anim.ketthucfrag);
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            overridePendingTransition(R.anim.batdau2, R.anim.ketthucfrag);
        }
        return true;
    }
    public void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);//set icon tren toolbar
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_backspace_245);//set icon menu
    }


}