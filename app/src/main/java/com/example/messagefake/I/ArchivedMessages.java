package com.example.messagefake.I;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagefake.Adapter.AdapterVerti;
import com.example.messagefake.R;
import com.example.messagefake.bottomSheetFragment.BottomSheetFragmentStorage;
import com.example.messagefake.chat;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.example.messagefake.loadlist;
import com.example.messagefake.user;

import java.util.ArrayList;
import java.util.List;

public class ArchivedMessages extends AppCompatActivity implements loadlist {
    RecyclerView recyclerView;
    AdapterVerti adapterVerti;
    int x = 0;
    LinearLayout mLinearLayout;
    List<user> users;

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
        setContentView(R.layout.activity_archived_messages);
        setToolBar();
        recyclerView = findViewById(R.id.recyclerview_verti);
        mLinearLayout = findViewById(R.id.mLinearLayout);
        DAO_sqlite dao_sqlite = new DAO_sqlite(this);
        users = dao_sqlite.getStorageOrMessageWaiting("storage", null).size() > 0 ? dao_sqlite.getStorageOrMessageWaiting("storage", null) : new ArrayList<>();

        if (users.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.GONE);
        }
        adapterVerti = new AdapterVerti(user -> {
            Intent intent = new Intent(ArchivedMessages.this, chat.class);
            user.setAv(null);
            intent.putExtra("user", user);
            intent.putExtra("main", "m");
            startActivity(intent);
            overridePendingTransition(R.anim.ket_thuc, 0);
        }, i -> {
            BottomSheetFragmentStorage fragmentStorage = new BottomSheetFragmentStorage(ArchivedMessages.this);
            Bundle bundle = new Bundle();
            bundle.putString("email", users.get(i).getUID());
            bundle.putInt("index1", i);
            bundle.putStringArrayList("index", (ArrayList<String>) new DAO_sqlite(ArchivedMessages.this).getStorage());
            fragmentStorage.setArguments(bundle);
            fragmentStorage.show(getSupportFragmentManager(), "tag");
        });
        adapterVerti.setData(users);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapterVerti);
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
            finish();
            overridePendingTransition(R.anim.batdau2, R.anim.ketthucfrag);
        }
        return true;
    }

    @Override
    public void loadUserBeforeStorega(int itemIndex) {
        adapterVerti.removeItem(itemIndex);
        if (users.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            mLinearLayout.setVisibility(View.VISIBLE);
        }
    }
}