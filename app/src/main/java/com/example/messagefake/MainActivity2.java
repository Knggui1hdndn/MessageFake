package com.example.messagefake;


import static com.example.messagefake.MainActivity.status;
import static com.example.messagefake.dataFirebase.DAO_Friebase.database_Reference;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorWindow;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagefake.Adapter.AdapterRecentSearch;
import com.example.messagefake.Adapter.AdapterSearchView;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity2 extends AppCompatActivity {
    RecyclerView goiY, recentSearch, search;
    SearchView searchView;
    AdapterSearchView vertical, vertical1;
    AdapterRecentSearch adapterRecentSearch;
    ArrayList<com.example.messagefake.user> listRecent;
    DAO_sqlite dao_sqlite;
    Button button;
    LinearLayout mLinearLayout;
    List<com.example.messagefake.user> userSearchRecent;
    List<com.example.messagefake.user> users;
    List<com.example.messagefake.user> Allusers;
    List<com.example.messagefake.user> friendsUser;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("ok", MODE_PRIVATE);
        if (preferences.getString("ok", "").equals("Light1")) {
            setTheme(R.style.Theme_Light1);
        } else {
            setTheme(R.style.Theme_Light);

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        addView();
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toolbar toolbar = findViewById(R.id.toolBarChat);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);//set icon tren toolbar
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_black24);//set icon tren toolbar
        listRecent = new ArrayList<>();
        dao_sqlite = new DAO_sqlite(this);
        userSearchRecent = dao_sqlite.getSearchRecent();//user tim kiem gan dday
        users = new ArrayList<>();

            loadRecentlySearchedUsers();
        if(listRecent.size()<dao_sqlite.getSearchRecent().size()){{
            listRecent = dao_sqlite.getSearchRecent();

        }}

        button = findViewById(R.id.btnEdit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity2.this, com.example.messagefake.editSearchHistory.class));
                overridePendingTransition(R.anim.ket_thuc, 0);
            }
        });
        //setdata recentSearch

        adapterRecentSearch = new AdapterRecentSearch(user -> {
            eventClickRecycelview(user);

        }, MainActivity2.this);
        recentSearch.setHasFixedSize(true);
        adapterRecentSearch.setData(listRecent);
        recentSearch.setLayoutManager(new GridLayoutManager(this, 4));
        recentSearch.setAdapter(adapterRecentSearch);

        //set data recycalview


        if (MainActivity.listverti.size() > 0 && status) {
            friendsUser = MainActivity.listverti;
        } else {
            friendsUser = dao_sqlite.getUser();
        }
        vertical = new AdapterSearchView((ArrayList<user>) friendsUser, this::eventClickRecycelview, MainActivity2.this);
        goiY.setLayoutManager(new LinearLayoutManager(MainActivity2.this, LinearLayoutManager.VERTICAL, false));
        goiY.setAdapter(vertical);

        vertical1 = new AdapterSearchView((ArrayList<user>) MainActivity.userListFromFireBases1, this::eventClickRecycelview, MainActivity2.this);
        search.setLayoutManager(new LinearLayoutManager(MainActivity2.this, LinearLayoutManager.VERTICAL, false));
        search.setAdapter(vertical1);
        //Bọ lọc searchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                vertical1.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    mLinearLayout.setVisibility(View.VISIBLE);
                    search.setVisibility(View.GONE);
                } else {
                    mLinearLayout.setVisibility(View.GONE);
                    search.setVisibility(View.VISIBLE);

                }
                vertical1.getFilter().filter(newText);
                return false;
            }
        });
        listenToChangeFireBase();

    }

    private void loadRecentlySearchedUsers() {
        Allusers = com.example.messagefake.MainActivity.userListFromFireBases1;
        for (com.example.messagefake.user s : userSearchRecent) {
            for (com.example.messagefake.user user : Allusers) {
                if (s.getUID().equals(user.getUID())) {
                    users.add(user);
                }
            }
        }
        listRecent.addAll(users);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void eventClickRecycelview(user user) {
        Intent intent = new Intent(MainActivity2.this, com.example.messagefake.chat.class);
        if (!status||MainActivity.userListFromFireBases1.size()==0) {
            user.setAv(null);
        }
        intent.putExtra("user", user);
        dao_sqlite.clearSearchRecent();
        listRecent.remove(user);
        listRecent.add(0, user);
        for (int i = 0; i < listRecent.size(); i++) {
            dao_sqlite.addSearchRecent(listRecent.get(i));
        }
        startActivity(intent);
        overridePendingTransition(R.anim.ket_thuc, 0);
        adapterRecentSearch.notifyDataSetChanged();

    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    private void listenToChangeFireBase() {
        database_Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vertical.notifyDataSetChanged();
                listRecent.clear();
                users.clear();
                loadRecentlySearchedUsers();
                adapterRecentSearch.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void addView() {
        mLinearLayout = findViewById(R.id.mLinearLayout);
        goiY = findViewById(R.id.goiY);
        search = findViewById(R.id.search);
        searchView = findViewById(R.id.searchView);
        recentSearch = findViewById(R.id.recyTimGanDay);
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.batdau1, R.anim.ket_thuc);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }
}
