package com.example.messagefake.Account;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messagefake.MainActivity;
import com.example.messagefake.R;
import com.example.messagefake.dataFirebase.DAO_Friebase;
import com.example.messagefake.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Objects;

public class login extends AppCompatActivity {
    Button Forgot_password;
    Button createAccount, btnLogin;
    public static String db="a";
    FrameLayout linearLayout;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    TextInputLayout edtAccount, edtPassword;
    FirebaseAuth mAuths;
    static ArrayList<user> dataFromFireBase = DAO_Friebase.getDataUserFromFireBase();
    boolean status;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addEventView();
        dialog= new Dialog(this);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


         new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (dataFromFireBase.size() > 0) {
                        dialog.cancel();
                        break;

                    }
                    Log.e("Nguyen khang", "run: "+dataFromFireBase.size() );
                 }
            }
        }).start();

         mAuths = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuths.getCurrentUser();
        //chuyển sang activity MainActivity nếu người dùng đã đăng nhập
        if (currentUser != null) {
            dialog.cancel();
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        //Quên mật khẩu
        Forgot_password.setOnClickListener(view -> {
            Intent intent = new Intent(login.this, Forgotpasswords.class);
            startActivity(intent);
        });
        //Kiểm tra trạng thái mạng
        statusNetwork();
        //Tạo tài khoản
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, CreateAccount.class);
                startActivity(intent);
            }
        });
        //Đăng nhập
        accountLogin();
        //Check lỗi text
        textChangeListener();
    }


    private void textChangeListener() {
        edtAccount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    edtAccount.setErrorEnabled(false);
                    edtPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    edtAccount.setErrorEnabled(false);
                    edtPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void accountLogin() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

                String account = Objects.requireNonNull(edtAccount.getEditText()).getText().toString().trim();
                String password = Objects.requireNonNull(edtPassword.getEditText()).getText().toString().trim();
                dialog.show();
                Log.d("onClick", "onClick: "+dataFromFireBase.size());
                if (!account.isEmpty() && !password.isEmpty() && dataFromFireBase.size() > 0) {
                    for (user user1 : dataFromFireBase) {
                        if (user1.getPhone().equals(account) && user1.getPassword().equals(password)) {
                            account = user1.getEmail();

                            break;
                        }
                        if (user1.getEmail().equals(account) && user1.getPassword().equals(password)) {
                            account = user1.getEmail();

                            break;
                        }
                    }int x1;

                     firebaseAuth.signInWithEmailAndPassword(account, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                 Toast.makeText(login.this, "Success", Toast.LENGTH_SHORT).show();
                                SharedPreferences preferences= getSharedPreferences("nameSQl",MODE_PRIVATE);;
                                preferences.edit().putString("nameSQlite",FirebaseAuth.getInstance().getCurrentUser().getEmail()).apply();
                                 FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        //set lại token
                                        FirebaseDatabase.getInstance().getReference("user/" + FirebaseAuth.getInstance().getCurrentUser().getUid()+"/token").setValue(task.getResult()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Log.e("Khang", "onComplete:0 "+task.getResult());

                                                }
                                            }
                                        });
                                        Log.e("Khang", "onComplete: "+task.getResult());

                                    }
                                });
                                startActivity(new Intent(login.this, MainActivity.class));
                                overridePendingTransition(R.anim.batdau1, R.anim.ket_thuc);
                                dialog.cancel();

                                finish();

                            } else {
                                dialog.cancel();
                                if (!status) {
                                    Toast.makeText(login.this, "Không có kết nối.", Toast.LENGTH_SHORT).show();
                                } else {
                                    edtAccount.setErrorEnabled(true);
                                    edtAccount.setError(" ");
                                    edtPassword.setErrorEnabled(true);
                                    edtPassword.setError(" ");
                                    Toast.makeText(login.this, "Sai tên tài khoản hoặc mật khẩu.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                } else {
                    dialog.cancel();
                    edtAccount.setErrorEnabled(true);
                    edtAccount.setError(" ");
                    edtPassword.setErrorEnabled(true);
                    edtPassword.setError(" ");
                }
            }

        });
    }

    private void addEventView() {
        Forgot_password = findViewById(R.id.Forgot_password);
        createAccount = findViewById(R.id.createAccount);
        linearLayout = findViewById(R.id.rootView);
        btnLogin = findViewById(R.id.btnLogin);
        edtAccount = findViewById(R.id.edtAccount);
        edtPassword = findViewById(R.id.edtPassword);
    }


    public void statusNetwork() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                 if (connectivityManager.getActiveNetworkInfo() != null) {
                    dataFromFireBase = DAO_Friebase.getDataUserFromFireBase();
                    status = true;
                    Snackbar.make(linearLayout, "Đã kết nối.", BaseTransientBottomBar.LENGTH_LONG).show();
                } else {
                    status = false;
                    Snackbar.make(linearLayout, "Vui lòng kết nối Internet.", BaseTransientBottomBar.LENGTH_INDEFINITE).show();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, intentFilter);
    }


}