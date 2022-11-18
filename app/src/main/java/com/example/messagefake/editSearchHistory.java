package com.example.messagefake;

import static com.example.messagefake.dataFirebase.DAO_Friebase.database_Reference;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagefake.Adapter.AdapterHistorySearch;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class editSearchHistory extends AppCompatActivity {
    RecyclerView recyclerView;
    Button btndelete;
    ActionBar actionbar;
    List<user> strings;
    List<user> users = new ArrayList<>();
    AdapterHistorySearch adapterHistorySearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("ok", MODE_PRIVATE);
        if (preferences.getString("ok", "").equals("Light1")) {
            setTheme(R.style.Theme_Light1);
        } else {
            setTheme(R.style.Theme_Light);

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_search_history);
        Toolbar toolbar = findViewById(R.id.toolBar);
        recyclerView = findViewById(R.id.recyclerview);
        btndelete = findViewById(R.id.btndelete);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);//set icon tren toolbar
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_backspace_245);//set icon menu
        actionbar.setTitle("Lịch sử tìm kiếm");
        strings = new DAO_sqlite(this).getSearchRecent();//user timf gan day
        for (user s : strings) {
            for (user user : com.example.messagefake.MainActivity.userListFromFireBases1) {
                if (s.getUID().equals(user.getUID())) {
                    users.add(user);
                }
            }
        }


        adapterHistorySearch = new AdapterHistorySearch((ArrayList<user>) users, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterHistorySearch);
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Xóa nội dung tìm kiếm?");
        Button btnXoa = dialog.findViewById(R.id.btnXoa);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DAO_sqlite(editSearchHistory.this).clearSearchRecent();
                offActivity();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        listenToChangeFireBase();
    }

    private void listenToChangeFireBase() {
        database_Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (user s : strings) {
                    for (user user : com.example.messagefake.MainActivity.userListFromFireBases1) {
                        if (s.getUID().equals(user.getUID())) {
                            users.add(user);
                        }
                    }
                }
                adapterHistorySearch.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void offActivity() {
        finish();
        startActivity(new Intent(editSearchHistory.this, MainActivity2.class));
        overridePendingTransition(R.anim.ket_thuc, 0);
    }

    @Override
    public void onBackPressed() {
        offActivity();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            offActivity();

        }
        return true;
    }
}