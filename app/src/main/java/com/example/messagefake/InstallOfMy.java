package com.example.messagefake;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.messagefake.Account.login;
import com.example.messagefake.I.ArchivedMessages;
import com.example.messagefake.I.DarkMode;
import com.example.messagefake.I.MessageIsWaiting;
import com.example.messagefake.I.PersonalPage;
import com.example.messagefake.I.Status_Custom;
import com.example.messagefake.bottomSheetFragment.BottomSheetFragmentEditName;
import com.example.messagefake.dataFirebase.DAO_Friebase;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.google.firebase.auth.FirebaseAuth;

public class InstallOfMy extends AppCompatActivity {
    Button btndark, btnsignOut,btnStorage,btnMessageWaiting,btnStatus,btnEditName,btnEditProfile;
    int x = 0;
ImageView imageView;SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
          preferences = getSharedPreferences("ok", MODE_PRIVATE);
        if (preferences.getString("ok", "").equals("Light1")) {
            setTheme(R.style.Theme_Light1);
            x = 1;
        } else {
            setTheme(R.style.Theme_Light);
            x = 0;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_of_my);
        setToolBar();
        initView();


        btndark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InstallOfMy.this, DarkMode.class));
             }
        });
        btnMessageWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InstallOfMy.this, MessageIsWaiting.class));
            }
        });
        btnsignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(InstallOfMy.this, login.class));
                finish();
            }
        });
        btnStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 startActivity(new Intent(InstallOfMy.this, ArchivedMessages.class));
            }
        });
        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InstallOfMy.this, Status_Custom.class));


            }
        });
        btnEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetFragmentEditName bottomSheetFragmentEditName=new BottomSheetFragmentEditName(getApplicationContext());
                bottomSheetFragmentEditName.show(getSupportFragmentManager(),"tag");
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InstallOfMy.this, PersonalPage.class));

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(new DAO_sqlite(this).getImg(DAO_Friebase.uid),0,
                    new DAO_sqlite(this).getImg(DAO_Friebase.uid).length));
        }catch (Exception exception){

        }
        if (x == 1) {
            btndark.setText("Chế độ tối \nBật");
        }else{
            btndark.setText("Chế độ tối \nTắt");

        }
        if(  preferences.getBoolean("status",true)){
            btnStatus.setText("Trạng thái hoạt động \nBật");
        }else{
            btnStatus.setText("Trạng thái hoạt động \nTắt");

        }
    }

    public void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);//set icon tren toolbar
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_backspace_245);//set icon menu
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            overridePendingTransition(R.anim.ket_thuc, 0);
            finish();
        }
        return true;
    }
    private void initView() {
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btndark = findViewById(R.id.btndark);
        btnsignOut = findViewById(R.id.btnsignOut);
        btnStorage = findViewById(R.id.btnStorage);
        btnMessageWaiting = findViewById(R.id.btnMessageWaiting);
        imageView=findViewById(R.id.img);
        btnStatus=findViewById(R.id.btnStatus);
        btnEditName=findViewById(R.id.btnEditName);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.ket_thuc, 0);

        super.onBackPressed();
    }
}