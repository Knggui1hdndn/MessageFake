package com.example.messagefake;

import static com.example.messagefake.dataFirebase.DAO_Friebase.database_Reference;
import static com.example.messagefake.dataFirebase.DAO_Friebase.database_Reference1;
import static com.example.messagefake.dataFirebase.DAO_Friebase.getEmailUserSend;
import static com.example.messagefake.dataFirebase.DAO_Friebase.updateStatus;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.CursorWindow;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.messagefake.Adapter.AdapterVerti;
import com.example.messagefake.Adapter.Adapter_Horizontal;
import com.example.messagefake.bottomSheetFragment.BottomSheetFragment;
import com.example.messagefake.dataFirebase.DAO_Friebase;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements loadlist, SwipeRefreshLayout.OnRefreshListener {
    Button button;
    int z = 0;
    com.example.messagefake.Adapter.Adapter_Horizontal Adapter_Horizontal;
    AdapterVerti adapterVerti;
    RecyclerView recyclerView, recyclerView1;
    ImageView view;
    public static boolean status = false;
    public static List<user> userListFromFireBases1 = DAO_Friebase.getDataUserFromFireBase();//tất ả user
    public static List<String> friends = DAO_Friebase.getFriendsFireBase();//tất friends user
    public static ArrayList<user> listverti = new ArrayList<>();
    ArrayList<user> listhori = new ArrayList<>();
    ;
    Dialog dialog;
    DAO_sqlite dao_sqlite;
    int x = 0;
    byte a[];
    SharedPreferences preferences;
    SwipeRefreshLayout refreshLayout;

    @SuppressLint({"SetTextI18n", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = getSharedPreferences("ok", MODE_PRIVATE);
        if (preferences.getString("ok", "").equals("Light1")) {
            setTheme(R.style.Theme_Light1);
        } else {
            setTheme(R.style.Theme_Light);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();

        FirebaseDatabase.getInstance().getReference("user/" + DAO_Friebase.uid + "/friends/").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                 if(task.isSuccessful()){
                     Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
                 }else{
                     Toast.makeText(MainActivity.this, "f", Toast.LENGTH_SHORT).show();
                 }
            }
        });

        refreshLayout.setColorSchemeColors(getColor(R.color.purple_500));
        refreshLayout.setOnRefreshListener(this);
        try {

            @SuppressLint("DiscouragedPrivateApi") Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
        //JobService
//        ComponentName componentName = new ComponentName(this, JobService.class);
//        JobInfo jobInfo = new JobInfo.Builder(0, componentName).
//                setPersisted(true).setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED).build();
//        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//        jobScheduler.schedule(jobInfo);

        try {
            a = dao_sqlite.getImg(FirebaseAuth.getInstance().getCurrentUser().getUid());
            if (a.length > 0) {
                view.setImageBitmap(BitmapFactory.decodeByteArray(a, 0, a.length));

            }
        } catch (Exception exception) {

        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.messagefake.InstallOfMy.class);
                startActivity(intent);

            }
        });
        button.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, MainActivity2.class));
            overridePendingTransition(R.anim.bat_dau, R.anim.ket_thuc);
        });
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getActiveNetworkInfo() != null) {

                    status = true;
                    updateStatus(true);
                    z = 0;
                    setDataList(new ArrayList<>());
                    listenToChangeFireBase();
                    //  listenToChangeFireBase1();
                    dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.dialog_loading);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    dialog.show();

                    setAdapterRecyclerViewHorizontal((ArrayList<user>) usersOff());
                    setAdapterRecyclerViewverizontal((ArrayList<user>) usersOff());
                    new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            setDataList(new ArrayList<>());
                            Log.d("network", "onTick: " + userListFromFireBases1.size() + friends.size());
                            if (millisUntilFinished <= 57000 && millisUntilFinished >= 55000) {
                                dialog.cancel();
                            }
                            if (listverti.size() > 0 && listhori.size() > 0) {
                                setAdapterRecyclerViewHorizontal(listhori);
                                setAdapterRecyclerViewverizontal(listverti);
                                dialog.cancel();
                                cancel();
                            }
                        }

                        @Override
                        public void onFinish() {
                        }
                    }.start();
                } else {
                    listhori.clear();
                    listverti.clear();
                    Log.d("network", "onReceive: " + "ok cc");
                    z = 1;
                    status = false;
                    updateStatus(false);
                    userListFromFireBases1 = dao_sqlite.getSearchRecent();
                    setAdapterRecyclerViewHorizontal((ArrayList<user>) usersOff());
                    setAdapterRecyclerViewverizontal((ArrayList<user>) usersOff());
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, intentFilter);
    }

    private void friendsThereIsNoInternet() {
        database_Reference.child(getEmailUserSend() + "/friends").setValue(dao_sqlite.getFriends());
    }

    private List<user> usersOff() {
        List<user> usersOff = dao_sqlite.getUser();
        for (user user : usersOff) {
            user.setStatus(false);

        }
        return usersOff;
    }

    private void initview() {
        button = findViewById(R.id.button_search);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);
        Toolbar toolbar = findViewById(R.id.toolBar);
        recyclerView = findViewById(R.id.recyclerview_hori);
        recyclerView1 = findViewById(R.id.recyclerview_verti);
        view = findViewById(R.id.img);
        setSupportActionBar(toolbar);
        try {
            dao_sqlite = new DAO_sqlite(this);
        } catch (Exception ignored) {

        }
    }

    //k79tjf2
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
   try {
       view.setImageBitmap(BitmapFactory.decodeByteArray(new DAO_sqlite(this).getImg(DAO_Friebase.uid),0,
               new DAO_sqlite(this).getImg(DAO_Friebase.uid).length));
   }catch (Exception e){

   }
        if (adapterVerti != null && Adapter_Horizontal != null) {
            adapterVerti.notifyDataSetChanged();
            Adapter_Horizontal.notifyDataSetChanged();
        }
        super.onResume();
    }


    private ArrayList<user> setDataList(ArrayList<user> users) {
        listverti.clear();
        listhori.clear();
        try {
            for (user user : userListFromFireBases1) {

                for (String s : friends) {
                    if (user.getUID().equals(s)) {
                        if (user.getUID().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                            if (a == null) {
                                Picasso.get().load(user.getAvata()).into(view);
                            }
                            try {
                                if (user.getMessageWaiting() != null) {
                                    List<String> getMessageWaiting = user.getMessageWaiting();
                                    for (String s1 : getMessageWaiting) {
                                        dao_sqlite.addmessagewaiting(s1);
                                    }
                                }

                                if (user.getFriendRequest() != null) {
                                    List<String> getMessageWaiting = user.getMessageWaiting();
                                    for (String s1 : getMessageWaiting) {
                                        dao_sqlite.addmessagewaiting(s1);
                                    }
                                }

                                if (user.getArchivedMessage() != null) {
                                    List<String> getArchivedMessage = user.getArchivedMessage();
                                    for (String s1 : getArchivedMessage) {
                                        dao_sqlite.addStorage(s1);
                                    }
                                }
                                if (user.getBlock() != null) {
                                    List<String> listBlock = user.getBlock();//danh sách tôi block
                                    for (String s1 : listBlock) {
                                        dao_sqlite.addBlock(s1);
                                    }
                                }
                                List<String> listFriends = user.getFriends();
                                for (String s1 : listFriends) {
                                    dao_sqlite.addFriends(s1);
                                }

                            } catch (Exception ignored) {

                            }
                        }
                        users.add(user);
                        if (dao_sqlite.getStorageOrMessageWaiting("storage", null).size() == 0) {
                            listverti.add(user);
                            if (user.isStatus()) {
                                listhori.add(user);
                            }
                        } else {
                            for (com.example.messagefake.user ss : dao_sqlite.getStorageOrMessageWaiting("storage", null)) {
                                if (!s.equals(ss.getUID())) {
                                    listverti.add(user);
                                    if (user.isStatus()) {
                                        listhori.add(user);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception ignored) {

        }
        for (user user : userListFromFireBases1) {
            for (String s : dao_sqlite.getMessageWaiting()) {
                if (user.getUID().equals(s)) {
                    user.setStatus(false);
                    users.add(user);
                }
            }
        }
        if (users.size() > 0) {
            if (z == 0) {
                new Thread(() -> {
                    for (user user : users) {
                        Log.d("setdata", "setDataList: " + user.isStatus() + user.getAvata());
                        dao_sqlite.addUser(user);
                        dao_sqlite.addFriends(user.getUID());
                    }
                    friendsThereIsNoInternet();
                }).start();
                z = 1;
            }

        }
        return users;
    }


    private void setAdapterRecyclerViewverizontal(ArrayList<user> listverti) {
        adapterVerti = new AdapterVerti(user -> {
            try {
                Intent intent = new Intent(MainActivity.this, chat.class);
                if (!status) {
                    user.setAv(null);
                }
                intent.putExtra("user", user);
                intent.putExtra("main", "m");
                startActivity(intent);
            } catch (Exception e) {
                Log.d("setAdapterRecyclerViewverizontal", "setAdapterRecyclerViewverizontal: " + e.getMessage());
            }
        }, i -> {
            BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(this);
            Bundle args = new Bundle();
            args.putStringArrayList("index", (ArrayList<String>) friends);
            args.putString("user", listverti.get(i).getUID());
            args.putInt("index1", i);
            bottomSheetFragment.setArguments(args);
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });
        adapterVerti.setData(listverti);
        recyclerView1.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView1.setAdapter(adapterVerti);
    }

    private void setAdapterRecyclerViewHorizontal(ArrayList<user> listhori) {
        Adapter_Horizontal = new Adapter_Horizontal(user -> {
            Intent intent = new Intent(MainActivity.this, chat.class);
            if (!status) {
                user.setAv(null);
            }
            intent.putExtra("user", user);
            intent.putExtra("main", "m");
            startActivity(intent);
        }, MainActivity.this);
        Adapter_Horizontal.setData(listhori);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(Adapter_Horizontal);
    }

    private void listenToChangeFireBase() {
        database_Reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<user> users = new ArrayList<>();
                setDataList(users);
                if (adapterVerti != null && Adapter_Horizontal != null) {
                    adapterVerti.notifyDataSetChanged();
                    Adapter_Horizontal.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void listenToChangeFireBase1() {
        database_Reference1.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (listverti.size() > 0 && listhori.size() > 0) {
                    ArrayList<user> users = new ArrayList<>();
                    listverti.clear();
                    listhori.clear();
                    setDataList(users);

                    if (adapterVerti != null && Adapter_Horizontal != null) {
                        adapterVerti.notifyDataSetChanged();
                        Adapter_Horizontal.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.camera) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void loadUserBeforeStorega(int itemIndex) {
        List<user> users = new ArrayList();
        users.add(listverti.get(itemIndex));
        adapterVerti.removeItem(itemIndex);
        users.retainAll(listhori);
        if (users.size() > 0) {
            Adapter_Horizontal.removeItem(users.get(0));
        }
    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(() -> {
            refreshLayout.setRefreshing(false);
            recreate();

        }, 2000);
    }
}