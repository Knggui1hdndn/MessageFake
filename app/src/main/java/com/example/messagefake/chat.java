package com.example.messagefake;


import static com.example.messagefake.dataFirebase.DAO_Friebase.database_Reference;
import static com.example.messagefake.dataFirebase.DAO_Friebase.getEmailUserSend;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.messagefake.Adapter.Adapter_Chat;
import com.example.messagefake.dataFirebase.DAO_Friebase;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.example.messagefake.notification.FcmNotificationsSender;
import com.example.messagefake.videoCall.IncomingCallActivity;
import com.example.messagefake.videoCall.OutgoingCallActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.stringee.StringeeClient;
import com.stringee.call.StringeeCall;
import com.stringee.call.StringeeCall2;
import com.stringee.exception.StringeeError;
import com.stringee.listener.StringeeConnectionListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class chat extends AppCompatActivity {
    ImageView imgStatus, img_avatar, img;
    TextView txtName, txtBody, txt1, txtTen;
    RecyclerView recyclerView;
    ImageButton button;
    NestedScrollView nestedScrollView;
    ArrayList<String> strings;
    private String token = "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTS1Q3ODVTT0NmRXBqMG91bHl6a01WVDlWZjFOclp3eW1ULTE2NTYxODAzMzQiLCJpc3MiOiJTS1Q3ODVTT0NmRXBqMG91bHl6a01WVDlWZjFOclp3eW1UIiwiZXhwIjoxNjU4NzcyMzM0LCJ1c2VySWQiOiJraGFuZyJ9.eLx-21BHXhy-3e0UmdYeMKs9ki_gu7GydeZhLIcNd8Q";
    public static StringeeClient client;
    public static HashMap<String, StringeeCall2> callsMap = new HashMap<>();
    DAO_sqlite dao_sqlite;
    Adapter_Chat adapter_chat;
    String path_ = "";
    com.example.messagefake.user user;
    Button btndelete, btnChan, btnXoa, btnUnblock;
    View notice_to_strangers;
    View block_user;
    View blocked;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    LottieAnimationView lottieAnimationView;
    Dialog dialog;
    String messageIsWaiting;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint({"SimpleDateFormat", "RestrictedApi", "ResourceType", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("ok", MODE_PRIVATE);
        if (preferences.getString("ok", "").equals("Light1")) {
            setTheme(R.style.Theme_Light1);
        } else {
            setTheme(R.style.Theme_Light);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        addView();
        messageIsWaiting = getIntent().getStringExtra("messageIsWaiting");
        byte b[] = dao_sqlite.getImg(user.getUID());
        if (dao_sqlite.getMessage(user.getUID()) != null) {
            strings = new ArrayList<>(Arrays.asList(dao_sqlite.getMessage(user.getUID())));
        } else {
            strings = new ArrayList<>();
        }
        adapter_chat = new Adapter_Chat(strings, chat.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(chat.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter_chat);
        adapter_chat.notifyDataSetChanged();
        nestedScrollView.pageScroll(View.FOCUS_DOWN);
        connectAndListenTEvent();
        checkKeyPathMessage();
        sendMessage();
        listenToChangeFireBase_setDataChat();
        if (checkFriend()) {
            txt1.setText("Các bạn đã là bạn bè.");
        } else {
            txt1.setText("Các không phải là bạn bè.");
        }
        if (messageIsWaiting != null) {
            checkStrangers();
        } else {
            checkBlocked();
            checkBlock();
        }
        try {
            if (b != null) {
                img_avatar.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
                img.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
            } else {
                Picasso.get().load(user.getAvata()).into(img_avatar);
                Picasso.get().load(user.getAvata()).into(img);
            }
        } catch (Exception e) {

        } finally {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 0);
            }
            startService(new Intent(this,chatHead_bubble.class));

        }


    }




    private void checkBlock() {
        if (MainActivity.status) {
            checkBlockWhenThereIsInternet();
        } else {
            showViewBlockUser(checkBlockWhenThereIsNoInternet());
        }
        Log.d("ssssssss", "checkBlock:1 " + checkBlockWhenThereIsNoInternet());
    }

    private void checkBlocked() {
        if (MainActivity.status) {
            checkBlockedWhenThereIsInternet();
        } else {
            showViewBlockUser(checkBlockedWhenThereIsNoInternet());
        }
        Log.d("ssssssss", "checkBlock: " + checkBlockedWhenThereIsNoInternet());
    }

    private Boolean checkBlockedWhenThereIsNoInternet() {
        for (String s : dao_sqlite.getListBlockMe()) {
            if (s.equals(user.getUID())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkBlockWhenThereIsNoInternet() {
        for (String s : dao_sqlite.getListBlock()) {
            if (s.equals(user.getUID())) {
                return true;
            }
        }
        return false;
    }

    private void checkTypingStatus() {

        database.getReference("message/" + path_ + "/" + getEmailUserRecived()).child("change").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean aBoolean = snapshot.getValue(Boolean.class);
                if (aBoolean != null) {
                    if (aBoolean) {
                        lottieAnimationView.playAnimation();
                        lottieAnimationView.setVisibility(View.VISIBLE);
                    } else {
                        lottieAnimationView.cancelAnimation();
                        lottieAnimationView.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        txtBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    database.getReference("message/" + path_ + "/" + getEmailUserSend()).
                            setValue(new message(getEmailUserRecived(), "", "", true));
                } else {
                    database.getReference("message/" + path_ + "/" + getEmailUserSend()).
                            setValue(new message(getEmailUserRecived(), "", "", false));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private boolean checkKeyMessage() {
        String s = dao_sqlite.getKeyMessage(user.getUID());
        if (s != null) {
            path_ = s;
            getChatAll(path_);
            Log.d("asasa", "checkKeyMessage: " + path_);
            return true;
        }
        return false;
    }


    private void checkKeyPathMessage() {

        int[] i = {0};
        int[] x1 = {0};
        if (!MainActivity.status) {
            dialog.cancel();
        }
        if (!checkKeyMessage()) {
            database.getReference("message").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (MainActivity.status) {
                        int x = 0;
                        for (DataSnapshot key : snapshot.getChildren()) {
                            x = 1;
                            if (key.getKey().contains("dHiKQUqtRuqs")) {
                                String s[] = key.getKey().split("dHiKQUqtRuqs");
                                if ((s[0].equals(getEmailUserSend()) && s[1].equals(getEmailUserRecived())) || (s[1].equals(getEmailUserSend()) && s[0].equals(getEmailUserRecived()))) {
                                    path_ = key.getKey();
                                    x1[0] = 1;
                                    break;
                                }
                            }
                        }
                        if (x == 1) {
                            if (x1[0] == 1) {//      orderByChild sắp  xếp từ bé đến lớn
                                getChatAll(path_);
                                dialog.cancel();
                            }
                            if (x1[0] == 0) {
                                path_ = getEmailUserSend() + "dHiKQUqtRuqs" + getEmailUserRecived();
                                database.getReference("message/" + path_ + "/" + getEmailUserSend()).
                                        setValue(new message(getEmailUserRecived(), " ", new SimpleDateFormat("hh:mm").format(new Date()), false));
                                database.getReference("message/" + path_ + "/" + getEmailUserSend()).
                                        setValue(new message(getEmailUserSend(), " ", new SimpleDateFormat("hh:mm").format(new Date()), false));
                                database.getReference("message/" + path_ + "/chatAll" + getEmailUserSend()).setValue(" ");
                                database.getReference("message/" + path_ + "/chatAll" + getEmailUserRecived()).setValue(" ");
                                Log.d("asd", "onDataChange: " + i[0] + "sfs");
                                dialog.cancel();
                            }
                            dao_sqlite.addKeyMessage(user.getUID(), path_);
                            checkTypingStatus();

                        }

                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    private void getChatAll(String path_) {
        database.getReference("message/" + path_).child("chatAll" + getEmailUserSend()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String s1 = snapshot.getValue(String.class);
                String clip[];
                if (s1 != null && !s1.equals(" ")) {
                    clip = s1.trim().split("\n");
                    strings = new ArrayList<>(Arrays.asList(clip));
                    if (dao_sqlite.getMessage(user.getUID()) != null) {
                        List<String> strings1 = new ArrayList<>(Arrays.asList(dao_sqlite.getMessage(user.getUID())));
                        aStrings(strings1, strings);
                    }
                    dialog.cancel();
                    adapter_chat.setData(strings);
                    adapter_chat.notifyDataSetChanged();
                    nestedScrollView.pageScroll(View.FOCUS_DOWN);

                } else {
                    assert s1 != null;
                    if (s1.equals(" ")) {
                        dialog.cancel();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void aStrings(List<String> list1, List<String> list2) {
        int i = list1.size() - list2.size();
        StringBuilder s = new StringBuilder();

        if (i > 0) {
            for (int i1 = list2.size(); i1 <= list1.size() - 1; i1++) {
                s.append(list1.get(i1));
            }
            String finalS = s.toString();
            new Thread(() -> addMessageToFireBase(finalS)).start();
        } else {
            dao_sqlite.addMessage(strings, user.getUID());

        }

    }

    private void checkBlockWhenThereIsInternet() {
        database_Reference.child(getEmailUserSend()).child("block").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                try {
                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                    };
                    List<String> yourStringArray = snapshot.getValue(t);
                    boolean b = false;
                    for (String s : Objects.requireNonNull(yourStringArray)) {
                        if (s.equals(user.getUID())) {
                            b = true;
                            break;
                        }
                    }
                    showViewBlockUser(b);
                } catch (Exception exception) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkBlockedWhenThereIsInternet() {
        database_Reference.child(getEmailUserRecived()).child("block").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                    };
                    List<String> strings = snapshot.getValue(t);
                    boolean b = false;
                    for (String s : Objects.requireNonNull(strings)) {
                        if (s.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                            b = true;
                        }
                    }
                    showViewBlocked(b);
                } catch (Exception ignored) {
                    showViewBlocked(false);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showViewBlocked(Boolean aBoolean) {
        if (aBoolean) {
            notice_to_strangers.setVisibility(View.GONE);
            block_user.setVisibility(View.GONE);
            blocked.setVisibility(View.VISIBLE);
            txtBody.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            dao_sqlite.addBlockMe(user.getUID());
        } else {
            hideView();
            dao_sqlite.deleteBlockMe(user.getUID());
            txtBody.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
        }
        Log.d("showViewBlocked", "showViewBlocked: " + aBoolean);
    }

    private void showViewBlockUser(Boolean aBoolean) {
        if (aBoolean) {
            notice_to_strangers.setVisibility(View.GONE);
            block_user.setVisibility(View.VISIBLE);
            blocked.setVisibility(View.GONE);
            txtBody.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            btnXoa.setOnClickListener(view -> deleteInViewBlockUser());
            btnUnblock.setOnClickListener(view -> unBlockInViewBlockUser());
        } else {
            hideView();
            dao_sqlite.deleteBlock(user.getUID());
            txtBody.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
        }
    }

    private void unBlockInViewBlockUser() {
        if (MainActivity.status) {
            List<String> strings = dao_sqlite.getListBlock();
            strings.remove(user.getUID());
            DAO_Friebase.addBlock((ArrayList<String>) strings);
            hideView();
            checkStrangers();
            button.setVisibility(View.VISIBLE);
            txtBody.setVisibility(View.VISIBLE);
        } else {
            Snackbar.make(nestedScrollView, "Vui lòng kiểm tra lại kết nối mạng.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void deleteInViewBlockUser() {
        if (MainActivity.status) {
            dao_sqlite.deleteUser(user.getUID());
            dao_sqlite.deleteChatUser(user.getUID());
            DAO_Friebase.deleteMessage(dao_sqlite.getKeyMessage(user.getUID()));
            finish();
        } else {
            Snackbar.make(nestedScrollView, "Vui lòng kiểm tra lại kết nối mạng.", Snackbar.LENGTH_SHORT).show();
        }

    }


    private void hideView() {
        notice_to_strangers.setVisibility(View.GONE);
        block_user.setVisibility(View.GONE);
        blocked.setVisibility(View.GONE);
    }

    private void checkStrangers() {//thống báo đối với người lạ
        if (messageIsWaiting != null) {
            blockMessageIsWaiting();
            deleteMessageIsWaiting();
            hideView();
            notice_to_strangers.setVisibility(View.VISIBLE);
        }
    }

    private void blockMessageIsWaiting() {
        btnChan.setOnClickListener(view -> {
            if (MainActivity.status) {
                dao_sqlite.addBlock(user.getUID());
                DAO_Friebase.addBlock((ArrayList<String>) dao_sqlite.getListBlock());
                hideView();
                showViewBlockUser(true);
            } else {
                Snackbar.make(nestedScrollView, "Vui lòng kiểm tra lại kết nối mạng.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteMessageIsWaiting() {
        btndelete.setOnClickListener(view -> {
            if (MainActivity.status) {
                dao_sqlite.deleteMessageWaiting(user.getUID());
                dao_sqlite.deleteChatUser(user.getUID());
                DAO_Friebase.addMessageWaitingFireBase((ArrayList<String>) dao_sqlite.getMessageWaiting());
            } else {
                Snackbar.make(nestedScrollView, "Vui lòng kiểm tra lại kết nối mạng.", Snackbar.LENGTH_SHORT).show();
            }
            finish();
        });
    }

    private void addFriendsFromStrangers() {//thêm bạn từ người lạ
        dao_sqlite.deleteMessageWaiting(user.getUID());
        dao_sqlite.addFriends(user.getUID());
        DAO_Friebase.addFriends((ArrayList<String>) dao_sqlite.getFriends());
        notice_to_strangers.setVisibility(View.GONE);
    }

    private void addMessageToFireBase(String message) {
        database.getReference("message/" + path_).child("chatAll" + getEmailUserSend()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String s = snapshot.getValue(String.class).trim();
                if (s != null) {
                    String s2[] = message.split("70sWUEEum");
                    String ss = "";
                    for (int i = 0; i < s2.length; i++) {
                        ss += s2[i] + "70sWUEEum" + "\n";
                    }
                    FirebaseDatabase.getInstance().getReference("message/" + path_ + "/chatAll" + getEmailUserSend()).setValue(s + "\n" + ss);
                    FirebaseDatabase.getInstance().getReference("message/" + path_).child("chatAll" + getEmailUserRecived()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String s = snapshot.getValue(String.class).trim();
                            if (s != null) {
                                String s2[] = message.split("70sWUEEum");
                                String ss = "";
                                for (int i = 0; i < s2.length; i++) {
                                    ss += s2[i] + "dHiKQUqtRuqs" + "\n";
                                }
                                FirebaseDatabase.getInstance().getReference("message/" + path_ + "/chatAll" + getEmailUserRecived()).setValue(s + "\n" + ss);
                                dialog.cancel();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage() {
        int[] i1 = {0};
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                try {
                    if (txtBody.getText().toString().trim().length() > 0 && messageIsWaiting == null) {
                        saveToSqliteAndSendNotifiAndPutDataToFireBase();
                        if (i1[0] == 0) {
                            int i = 0;
                            Log.d("ffffffffffffffffffff", "onClick: " + user.getFriends().size());
                            for (String s : user.getFriends()) {
                                if (s.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    i = 1;
                                    Log.d("adddd", "onClick: " + s);

                                }
                            }
                            Log.d("adddd", "onClick: " + i);

                            if (i == 0) {
                                int[] finalI = {0};
                                database_Reference.child(getEmailUserRecived() + "/messageWaiting").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                                        };
                                        List<String> strings = snapshot.getValue(t);
                                        if (strings == null) {
                                            strings = new ArrayList<>();
                                        }
                                        strings.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        database_Reference.child(getEmailUserRecived()).child("messageWaiting").setValue(strings);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Snackbar.make(nestedScrollView, "Vui lòng kiểm tra lại kết nối mạng hoặc thử lại sau.", Snackbar.LENGTH_SHORT).show();

                                    }
                                });
                            }
                            i1[0] = 1;
                        }

                    }
                    if (txtBody.getText().toString().trim().length() > 0 && messageIsWaiting != null) {
                        if (!MainActivity.status) {
                            hideView();
                            addFriendsFromStrangers();
                            String x1 = txtBody.getText().toString().trim();
                            strings.add(txtBody.getText().toString().trim() + new SimpleDateFormat("hh:mm").format(new Date()) + "70sWUEEum");

                            dao_sqlite.addUser(user);
                            dao_sqlite.addMessage(strings, user.getUID());
                            txtBody.setText("");
                            sendNotification(x1);
                        } else {
                            addFriendsFromStrangers();
                            saveToSqliteAndSendNotifiAndPutDataToFireBase();

                        }
                    }

                } catch (Exception e) {
                    Log.d("dddd", "onClick: " + e.getMessage());
                }

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void saveToSqliteAndSendNotifiAndPutDataToFireBase() {

        String path_2 = "message/" + path_ + "/" + getEmailUserSend();//tk gui tin nhắn
        DatabaseReference databaseReference_1 = FirebaseDatabase.getInstance().getReference(path_2);
        String x1 = txtBody.getText().toString().trim();
        strings.add(txtBody.getText().toString().trim() + new SimpleDateFormat("hh:mm").format(new Date()) + "70sWUEEum");
        if (!dao_sqlite.getFriends().contains(user.getUID())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dao_sqlite.addUser(user);
                }
            }).start();

        }
        dao_sqlite.addMessage(strings, user.getUID());
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplication(), R.raw.a);
        mediaPlayer.start();
        txtBody.setText("");
        adapter_chat.setData(strings);
        adapter_chat.notifyDataSetChanged();
        nestedScrollView.fullScroll(View.FOCUS_DOWN);

        addMessageToFireBase(strings.get(strings.size() - 1));
        databaseReference_1.setValue(new message(getEmailUserRecived(), x1 + "", new SimpleDateFormat("hh:mm").format(new Date())))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseDatabase.getInstance().getReference("message/" + path_ + "/" + getEmailUserSend() + "/change").setValue(false);
                        sendNotification(x1);
                        FirebaseDatabase.getInstance().getReference("message/" + path_ + "/" + getEmailUserSend()).setValue(new message(getEmailUserRecived(), "", "", false));
                    }
                });
    }

    private void sendNotification(String txtSend) {
        FcmNotificationsSender fcmNotificationsSender = new FcmNotificationsSender(user.getToken(), dao_sqlite.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).get(0).getName(), txtSend, chat.this);
        fcmNotificationsSender.SendNotifications();
    }

    public String getEmailUserRecived() {

        return user.getUID();
    }

    private boolean checkFriend() {
        for (String s : dao_sqlite.getFriends()) {
            if (s.equals(user.getUID())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    private void listenToChangeFireBase_setDataChat() {
        Intent intent = getIntent();
        com.example.messagefake.user user = (com.example.messagefake.user) intent.getSerializableExtra("user");
        txtName.setText(user.getName());
        if (!user.isStatus()) {
            imgStatus.setVisibility(View.GONE);
        } else {
            imgStatus.setVisibility(View.VISIBLE);
        }
        txtTen.setText(user.getName());
        database_Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    com.example.messagefake.user user1 = dataSnapshot.getValue(com.example.messagefake.user.class);
                    if (user1.getUID() != null) {
                        txtName.setText(user1.getName());
                        if (user1.getUID().equals(user.getUID())) {
                            if (!user1.isStatus()) {
                                imgStatus.setVisibility(View.GONE);
                            } else {
                                imgStatus.setVisibility(View.VISIBLE);
                            }
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.batdau2, R.anim.ketthucfrag);
        super.onBackPressed();
    }

    private void addView() {
        Toolbar toolbar = findViewById(R.id.toolBarChat);
        setSupportActionBar(toolbar);
        nestedScrollView = findViewById(R.id.scrollView);
        lottieAnimationView = findViewById(R.id.change);
        user = (com.example.messagefake.user) getIntent().getSerializableExtra("user");
        dao_sqlite = new DAO_sqlite(this);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        imgStatus = findViewById(R.id.imgStatus);
        img = findViewById(R.id.img);
        txt1 = findViewById(R.id.txt1);
        txtTen = findViewById(R.id.txtTen);
        txtBody = findViewById(R.id.txtBody);
        button = findViewById(R.id.btnSend);
        img_avatar = findViewById(R.id.img_avatar_horizontal);
        txtName = findViewById(R.id.txt_name);
        recyclerView = findViewById(R.id.recyclerview);


        notice_to_strangers = findViewById(R.id.a);
        btndelete = notice_to_strangers.findViewById(R.id.btndelete);
        btnChan = notice_to_strangers.findViewById(R.id.btnChan);

        block_user = findViewById(R.id.b);
        btnXoa = block_user.findViewById(R.id.btnXoa);
        btnUnblock = block_user.findViewById(R.id.btnUnblock);

        blocked = findViewById(R.id.c);
        RoundedImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.batdau2, R.anim.ketthucfrag);

            }
        });
    }

    private void connectAndListenTEvent() {
        client = new StringeeClient(this);
        client.setConnectionListener(new StringeeConnectionListener() {
            @Override
            public void onConnectionConnected(StringeeClient stringeeClient, boolean b) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }

            @Override
            public void onConnectionDisconnected(StringeeClient stringeeClient, boolean b) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

            @Override
            public void onIncomingCall(StringeeCall stringeeCall) {

            }

            @Override
            public void onIncomingCall2(StringeeCall2 stringeeCall2) {//Khi máy khách có cuộc gọi đến, phương thức onIncomingCall2 (StringeeCall2 stringeeCall2) được gọi.
                Dialog dialog = new Dialog(chat.this);
                dialog.setContentView(R.layout.cuocgoiden);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Button tuchoi = dialog.findViewById(R.id.tuchoi);
                Button nghe = dialog.findViewById(R.id.nghe);
                tuchoi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stringeeCall2.reject();
                    }
                });
                nghe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        callsMap.put(stringeeCall2.getCallId(), stringeeCall2);
                        Intent intent = new Intent(chat.this, IncomingCallActivity.class);
                        intent.putExtra("call_id", stringeeCall2.getCallId());
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onConnectionError(StringeeClient stringeeClient, StringeeError
                    stringeeError) {

            }

            @Override
            public void onRequestNewToken(StringeeClient stringeeClient) {

            }

            @Override
            public void onCustomMessage(String s, JSONObject jsonObject) {

            }

            @Override
            public void onTopicMessage(String s, JSONObject jsonObject) {

            }
        });
        client.connect(token);

        //        Khi máy khách kết nối với Máy chủ Stringee, phương thức onConnectionConnected(StringeeClient stringeeClient,
//        boolean isReconnecting) được gọi.
//        Khi máy khách ngắt kết nối với Máy chủ Stringee, phương thức onConnectionDisconnected (StringeeClient stringeeClient, boolean isReconnecting) được gọi
//        Khi máy khách không kết nối được với Máy chủ Stringee, phương thức onConnectionError (StringeeClient stringeeClient, StringeeError stringeeError) được gọi.
//        Khi mã thông báo hết hạn, onRequestNewToken (StringeeClient stringeeClient) được gọi. Bạn sẽ cần tạo lại mã thông báo mới và kết nối lại.
//        Khi máy khách có cuộc gọi đến, phương thức onIncomingCall2 (StringeeCall2 stringeeCall2) được gọi.
//        Nếu bạn sử dụng máy chủ On-Premise, bạn có thể làm theo mã bên dưới để kết nối:


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.callvideo:
                request();
                FcmNotificationsSender fcmNotificationsSender = new FcmNotificationsSender("dmOk3LiZR0Gmm-ozYwc8zN:APA91bE84-beLXvDDKmf8cfHHgCcCXnZxJw6N-Z9IDNHx1z7ouNZagU2AjKhQpxRkjwpX4s6kKeWHChxOusOGtGBSDI5Aw7Epfkd2eXiDUtxUgwf8brWZ1q8t3vH8M1RbCzl0353q2B5", "Cuoc goi", "ok", chat.this);
                fcmNotificationsSender.SendNotifications();
                Intent intent = new Intent(chat.this, OutgoingCallActivity.class);
                intent.putExtra("from", client.getUserId());
                intent.putExtra("to", "299999");
                startActivity(intent);
                break;

        }
        return true;
    }

    private void request() {
        ActivityCompat.requestPermissions(chat.this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        }, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestPermissionsResult", "onRequestPermissionsResult: " + permissions);
            }

        }

    }
}