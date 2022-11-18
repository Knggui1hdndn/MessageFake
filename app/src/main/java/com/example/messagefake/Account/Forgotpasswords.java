package com.example.messagefake.Account;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.messagefake.R;
import com.example.messagefake.frag_password.frag_accountName;

public class Forgotpasswords extends AppCompatActivity {
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        frameLayout = findViewById(R.id.Fram);
        frag_accountName firstFragment = new frag_accountName();

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.replace(R.id.Fram, firstFragment);

        ft.commit();

    }
}