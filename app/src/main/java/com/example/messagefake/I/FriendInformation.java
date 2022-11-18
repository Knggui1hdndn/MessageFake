package com.example.messagefake.I;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.messagefake.Adapter.AdapterVertiFriend;
import com.example.messagefake.R;
import com.example.messagefake.chat;
import com.example.messagefake.dataFirebase.DAO_Friebase;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.example.messagefake.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendInformation extends AppCompatActivity {
    RoundedImageView img;
    Button btnCam, btnEditProfile, btnHabitat, btnAddress, btnMessage, btnIntroductoryInformation, btnEditPublicDetails, btnSeeAllFriends;
    TextView txtNumberOfFriends, txtFindFriend, txtName;
    DAO_sqlite dao_sqlite;
    int x = 0;
    SharedPreferences preferences;
    RecyclerView rcy;
    AdapterVertiFriend adapterVertiFriend;
    Toolbar toolbar;
    Intent intent;
    user user;
    LottieAnimationView a;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_friends);
        dao_sqlite = new DAO_sqlite(this);
        intent = getIntent();
        user = (com.example.messagefake.user) intent.getSerializableExtra("user");
        initview();
        setToolBar();
        btnMessage.setOnClickListener(v -> {
            Intent intent = new Intent(FriendInformation.this, chat.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });
        if (user.getAvata() != null) {
            Picasso.get().load(user.getAvata()).into(img);
        }

        if (!user.getFriends().contains(DAO_Friebase.getEmailUserSend())) {
            btnEditProfile.setText("Add Friends");
            btnEditProfile.setCompoundDrawables(getDrawable(R.drawable.ic_baseline_add_24), null, null, null);
        }


        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            boolean aBoolean = true;

            @Override
            public void onClick(View v) {
                if(btnEditProfile.getText().toString().equals("Bạn bè")){
                    aBoolean=false;
                }
                if (aBoolean) {
                    addFriendsRequest(aBoolean);
                    btnEditProfile.setText("Đã gửi");
                    btnEditProfile.setCompoundDrawables(getDrawable(R.drawable.ic_baseline_done_all_24), null, null, null);
                    aBoolean = false;
                } else {
                    addFriendsRequest(aBoolean);
                    btnEditProfile.setText("Add Friends");
                    btnEditProfile.setCompoundDrawables(getDrawable(R.drawable.ic_baseline_add_24), null, null, null);
                    aBoolean = true;
                }
            }
        });
        setAdapter();
    }

    private void addFriendsRequest(Boolean aBoolean) {
        FirebaseDatabase.getInstance().getReference("user/" + user.getUID() + "/friendRequest").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                };
                List<String> strings = snapshot.getValue(t);
                if(strings==null){
                    strings=new ArrayList<>();
                }
                if (aBoolean) {
                    strings.add(DAO_Friebase.uid);
                }else{
                  removeFriends();
                    strings.remove(DAO_Friebase.uid);
                }
                DAO_Friebase.addFriendRequestReceive((ArrayList<String>) strings, user.getUID());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void removeFriends(String UID) {
        FirebaseDatabase.getInstance().getReference("user/" + UID + "/friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                };
                List<String> strings = snapshot.getValue(t);
                strings.remove(DAO_Friebase.uid);
                FirebaseDatabase.getInstance().getReference("user/" + UID + "/friends/"+DAO_Friebase.uid).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAdapter() {
        List<user> users = new ArrayList<>();
        adapterVertiFriend = new AdapterVertiFriend(user -> {
            if (user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                Intent intent = new Intent(FriendInformation.this, PersonalPage.class);
                intent.putExtra("user", user);
                startActivity(intent);
            } else {
                Intent intent = new Intent(FriendInformation.this, FriendInformation.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
        adapterVertiFriend.setData(users);

        rcy.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        rcy.setAdapter(adapterVertiFriend);

        FirebaseDatabase.getInstance().getReference("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (String s : user.getFriends()) {
                        if (s.equals(Objects.requireNonNull(dataSnapshot.getValue(user.class)).getUID()) && !s.equals(user.getUID())) {
                            users.add(dataSnapshot.getValue(user.class));
                        }

                    }
                }
                if (users.size() > 0) {
                    a.cancelAnimation();
                    a.setVisibility(View.GONE);
                    adapterVertiFriend.setData(users);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void initview() {

        btnMessage = findViewById(R.id.btnMessage);
        toolbar = findViewById(R.id.toolBar);
        a = findViewById(R.id.a);
        img = findViewById(R.id.img);
        btnCam = findViewById(R.id.btnCam);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnHabitat = findViewById(R.id.btnHabitat);
        btnAddress = findViewById(R.id.btnAddress);
        btnIntroductoryInformation = findViewById(R.id.btnIntroductoryInformation);
        btnEditPublicDetails = findViewById(R.id.btnEditPublicDetails);
        btnSeeAllFriends = findViewById(R.id.btnSeeAllFriends);
        txtNumberOfFriends = findViewById(R.id.txtNumberOfFriends);
        txtFindFriend = findViewById(R.id.txtFindFriend);
        rcy = findViewById(R.id.rcy);
        txtName = findViewById(R.id.txtName);
        txtName.setText(user.getName());
        try {

            txtNumberOfFriends.setText(user.getFriends().size() + " Người bạn");
            btnHabitat.setText(setTextSpannable("Sống tại " + user.getHabitat()));
            btnAddress.setText(setTextSpannable("Đến từ " + user.getAddress()));
        } catch (Exception e) {
            btnHabitat.setText("Chưa bổ sung nơi sống");
            btnAddress.setText("Chưa bổ sung quê quán");
        }

    }

    private SpannableString setTextSpannable(String s) {
        SpannableString string = new SpannableString(s);
        string.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), TextUtils.indexOf(s, 'i'), s.length(), 0);
        return string;
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
        actionbar.setTitle("Trang cá nhân " + user.getName());
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