package com.example.messagefake.frag_createAccount;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.messagefake.Account.CreateAccount;
import com.example.messagefake.MainActivity;
import com.example.messagefake.R;
import com.example.messagefake.openFragCreateAccount;
import com.example.messagefake.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class frag_OTP extends Fragment {
    public Button Confirm;
    public Button sendOTP;
      static CreateAccount createAccount;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    TextInputLayout txtOtp;
    String codeVeryfiction;
    int time;
Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.otp, container, false);
        Confirm = view.findViewById(R.id.Confirm);
        sendOTP = view.findViewById(R.id.sendOTP);
        progressBar = view.findViewById(R.id.proggess);
        linearLayout = view.findViewById(R.id.otp);
        txtOtp = view.findViewById(R.id.mTextInputLayout);
        createAccount = (CreateAccount) getActivity();

        countDown();

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                    boolean b = createAccount.isStatusNw();
                    if (b) {
                        Bundle bundle = getArguments();
                        setDataFireBaseDataBase(bundle);
                    } else {
                        openFragCreateAccount openFragCreateAccount = (com.example.messagefake.openFragCreateAccount) getActivity();
                        openFragCreateAccount.openFrag(new frag_no_connection(), "Kết nối mạng.");
                    }
                }




        });
        return view;
    }

    private void sendOTP() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber("+84" + " 0867896418")       // Phone number to verify
                        .setTimeout(60l, TimeUnit.SECONDS) // hiệu lực của OTP
                        .setActivity(createAccount)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {//
                            // Nhà cung cấp xác thực điện thoại
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                String code = phoneAuthCredential.getSmsCode();
                                if (code != null) {
                                    codeVeryfiction = code;
                                }
                                Toast.makeText(createAccount, "ok", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(createAccount, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void countDown() {
        time = 60;
       // sendOTP();

        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long l) {
                sendOTP.setText(time + "s");
                time--;
            }

            @Override
            public void onFinish() {
                sendOTP.setText("Gửi lại OTP");
                sendOTP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        countDown();
                    }
                });
            }
        };
        countDownTimer.start();
    }

    public static void setDataFireBaseDataBase(Bundle bundle) {
        String[] token = {"d"};
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        // Get new FCM registration token
                        token[0] = task.getResult();
                        Log.d(MainActivity.class.getName(), token[0]);
                    }
                });
        String name = bundle.getString("Tên");
        String password = bundle.getString("Mật khẩu");
        String birthDay = bundle.getString("Ngày sinh");
        String sex = bundle.getString("Giới tính");
        String phoneNumber = bundle.getString("Số điện thoại");
        String email = bundle.getString("email");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(createAccount, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                    DatabaseReference databaseReference = firebaseDatabase.getReference("user/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ArrayList<String> strings = new ArrayList<>();
                    ArrayList<String> strings1 = new ArrayList<>();
                    strings.add(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    databaseReference.setValue(
                            new user("https://st.quantrimang.com/photos/image/2017/04/08/anh-dai-dien-FB-200.jpg",
                                    email,
                                    name
                                    , password
                                    , phoneNumber,
                                    birthDay,
                                    sex,
                                    false,"",token[0]
                                      , strings,strings1,strings1,strings1,strings1,FirebaseAuth.getInstance().getCurrentUser().getUid())).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(createAccount);
                                builder.setTitle("Đăng kí thành công")
                                        .setMessage("Nhấn xác nhận để đăng nhập")
                                        .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                createAccount.finish();
                                            }
                                        }).setCancelable(false).show();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(createAccount);
                                builder.setTitle("Hệ thống bận")
                                        .setMessage("Vui lòng đăng kí sau.")
                                        .setPositiveButton("Quay lại.", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                createAccount.finish();
                                            }
                                        }).setCancelable(false).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(createAccount, "Email đã được đăng kí hoặc không hợp lệ.", Toast.LENGTH_SHORT).show();
                    createAccount.finish();
                }
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context1) {
        context=context1;
        super.onAttach(context1);
    }
}

