package com.example.messagefake.frag_createAccount;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.messagefake.Account.CreateAccount;
import com.example.messagefake.R;

public class frag_no_connection extends Fragment {
    Button load;
    ProgressBar progressBar;
    FrameLayout view1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.no_connection, container, false);
        load = view.findViewById(R.id.load);
        progressBar = view.findViewById(R.id.proggess);
        view1 = view.findViewById(R.id.view);
        //
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount createAccount = (CreateAccount) getActivity();
                boolean b = createAccount.isStatusNw();
                CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long l) {
                        progressBar.setVisibility(View.VISIBLE);
                        view1.setClickable(false);
                    }

                    @Override
                    public void onFinish() {
                        view1.setClickable(true);
                        progressBar.setVisibility(View.GONE);
                        CreateAccount createAccount = (CreateAccount) getActivity();
                        boolean b = createAccount.isStatusNw();
                        if (b == true) {
                            Bundle bundle=getArguments();
                            frag_OTP.setDataFireBaseDataBase(bundle);
                        }
                    }
                };
                countDownTimer.start();
            }
        });
        return view;
    }
}
