package com.example.messagefake.I;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.messagefake.MainActivity;
import com.example.messagefake.R;
import com.example.messagefake.dataFirebase.DAO_Friebase;
import com.example.messagefake.user;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditName extends AppCompatActivity {
    TextView txt;
    TextInputLayout textInputLayout;
    int x = 0;
    MenuItem item;
    SpannableString s;
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("ok", MODE_PRIVATE);
        if (preferences.getString("ok", "").equals("Light1")) {
            setTheme(R.style.Theme_Light1);
            x = 1;
        } else {
            setTheme(R.style.Theme_Light);
            x = 0;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        txt = findViewById(R.id.txt);
        textInputLayout = findViewById(R.id.a);
        setToolBar();
        setTextSpannable();
        textChange();
    }

    private void textChange() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s1, int start, int before, int count) {
                if (s1.length() >= 5) {
                    progressBar.setVisibility(View.VISIBLE);

                    textInputLayout.setErrorEnabled(false);

                    FirebaseDatabase.getInstance().getReference("user").orderByChild("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int i = 0;
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.getValue(user.class).getName() .equals(s1.toString())) {
                                    i = 1;
                                    break;
                                }
                            }
                            if (i == 0) {
                                s.setSpan(new ForegroundColorSpan(Color.BLUE), 0, s.length(), 0);
                                item.setEnabled(true);
                                item.setTitle(s);
                                progressBar.setVisibility(View.GONE);
                                name = s1.toString();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length(), 0);
                                item.setEnabled(false);

                                textInputLayout.setErrorEnabled(false);
                                textInputLayout.setError("Tên người dùng này không khả dụng");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    progressBar.setVisibility(View.GONE);
                    textInputLayout.setErrorEnabled(false);
                    textInputLayout.setError("Tên người dùng này không khả dụng");
                    s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length(), 0);
                    item.setEnabled(false);
                    item.setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setTextSpannable() {
        String noiDung = "Tên người dùng của bạn sẽ trở thành một trong những phàn liên kết tùy chỉnh.Với liên kêt này, mọi người có thể đi đến liên kết trang cá nhân Facebook của bạn hay liên hệ với bạn bè trên Messager.\nBạn cần trợ giúp ư? Xem mẹo chọn tên người dùng.";
        SpannableString string = new SpannableString(noiDung);
        string.setSpan(new ForegroundColorSpan(Color.parseColor("#0000ff")), TextUtils.lastIndexOf(noiDung, '?')+1, noiDung.length(), 0);
        string.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);    // this remove the underline
            }

            @Override
            public void onClick(@NonNull View view) {
                recreate();
            }
        }, TextUtils.lastIndexOf(noiDung, '.'), noiDung.length(), 0);
        txt.setMovementMethod(LinkMovementMethod.getInstance());
        txt.setText(string);
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
        if (item.getItemId() == R.id.save) {
            FirebaseDatabase.getInstance().getReference("user/"+ DAO_Friebase.getEmailUserSend()+"/name").setValue(name);
            if (!MainActivity.status) {
                Snackbar.make(textInputLayout, "Vui lòng kiểm tra lại kết nối mạng.", Snackbar.LENGTH_SHORT).show();
            }else {
                finish();
                overridePendingTransition(R.anim.batdau2, R.anim.ketthucfrag);
            }
        } else {
            finish();
            overridePendingTransition(R.anim.batdau2, R.anim.ketthucfrag);
        }
        return true;
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
}