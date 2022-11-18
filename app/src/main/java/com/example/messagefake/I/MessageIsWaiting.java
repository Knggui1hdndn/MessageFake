package com.example.messagefake.I;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagefake.Adapter.AdapterVerti;
import com.example.messagefake.MainActivity;
import com.example.messagefake.R;
import com.example.messagefake.bottomSheetFragment.BottomSheetFragmentMessageWaiting;
import com.example.messagefake.chat;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.example.messagefake.loadlist;
import com.example.messagefake.user;

import java.util.ArrayList;
import java.util.List;

public class MessageIsWaiting extends AppCompatActivity implements loadlist {
    RecyclerView recyclerView;
    AdapterVerti adapterVerti;
    TextView textView;
    List<user> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("ok", MODE_PRIVATE);
        if (preferences.getString("ok", "").equals("Light1")) {
            setTheme(R.style.Theme_Light1);
        } else {
            setTheme(R.style.Theme_Light);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_is_waiting);
        setToolBar();
        textView = findViewById(R.id.textI);
        recyclerView = findViewById(R.id.recyclerview_verti);

        setTextSpannable();
        DAO_sqlite dao_sqlite = new DAO_sqlite(this);
        users = dao_sqlite.getStorageOrMessageWaiting(null, "messagewaiting").size() > 0 ? dao_sqlite.getStorageOrMessageWaiting(null, "messagewaiting") : new ArrayList<>();
         setAdapter(users);
    }

    private void setAdapter(List<user> users) {
        adapterVerti = new AdapterVerti(  user -> {
            Intent intent = new Intent(MessageIsWaiting.this, chat.class);
            if (!MainActivity.status) {
                user.setAv(null);
            }
            intent.putExtra("user", user);
            intent.putExtra("main", "m");
            intent.putExtra("messageIsWaiting", "messageIsWaiting");
            startActivity(intent);
            overridePendingTransition(R.anim.ket_thuc, 0);
        }, i -> {
            BottomSheetFragmentMessageWaiting fragmentStorage = new BottomSheetFragmentMessageWaiting(MessageIsWaiting.this);
            Bundle bundle = new Bundle();
            bundle.putString("email", users.get(i).getUID());
            bundle.putInt("index1", i);
            bundle.putStringArrayList("index", (ArrayList<String>)new DAO_sqlite(getApplicationContext()).getMessageWaiting());
            fragmentStorage.setArguments(bundle);
            fragmentStorage.show(getSupportFragmentManager(), "tag");
        });
        adapterVerti.setData(users);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapterVerti);
    }

    private void setTextSpannable() {
        String noiDung = "Hãy mở đoạn chat để xem thông tin về người gửi.\nChỉ khi bạn trả lời họ thì họ mới biết là họ đã xem tin nhắn. Quyết định ai có thể nhắn tin cho bạn.";
        SpannableString string = new SpannableString(noiDung);
        string.setSpan(new ForegroundColorSpan(Color.parseColor("#0000ff")), TextUtils.indexOf(noiDung, "Q"), noiDung.length(), 0);
        string.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);    // this remove the underline
            }

            @Override
            public void onClick(@NonNull View view) {
                recreate();
            }
        }, TextUtils.indexOf(noiDung, "Q"), noiDung.length(), 0);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(string);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.batdau2, R.anim.ketthucfrag);
        super.onBackPressed();
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
        if(item.getItemId()==android.R.id.home){
            finish();
            overridePendingTransition(R.anim.batdau2, R.anim.ketthucfrag);
        }
        return true;
    }

    @Override
    public void loadUserBeforeStorega(int itemIndex) {
        adapterVerti.removeItem(itemIndex);
       
    }
}