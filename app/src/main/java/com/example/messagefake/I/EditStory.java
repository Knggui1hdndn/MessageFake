package com.example.messagefake.I;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.messagefake.R;
import com.google.android.material.textfield.TextInputLayout;

public class EditStory extends AppCompatActivity {
    MenuItem item;
    SpannableString s;
    String name = "";
    SpannableString string = new SpannableString("Lưu");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_story);
        setToolBar();
        TextView textView = findViewById(R.id.txt);
        TextInputLayout inputLayout = findViewById(R.id.tip);
        inputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s1, int start, int before, int count) {
                if (s1.toString().trim().length() > 0) {
                    s.setSpan(new ForegroundColorSpan(Color.BLUE), 0, s.length(), 0);
                    item.setEnabled(true);
                    item.setTitle(s);
                    name = s1.toString();
                } else {
                    s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length(), 0);
                    item.setEnabled(false);


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        findViewById(R.id.c).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackground(getDrawable(R.drawable.bgr2));
                textView.setTextColor(getColor(R.color.purple_500));
                textView.setTextSize(12);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        int positionOfMenuItem = 0; // or whatever...
        item = menu.getItem(positionOfMenuItem);
        item.setEnabled(false);
        s = new SpannableString("LƯU");
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length(), 0);
        item.setTitle(s);
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

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.batdau2, R.anim.ketthucfrag);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.batdau2, R.anim.ketthucfrag);
        }
        return true;
    }
}