package com.example.messagefake.frag_createAccount;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.messagefake.R;
import com.example.messagefake.openFragCreateAccount;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class frag_password extends Fragment {
    Button continue_;
    TextInputLayout edtPassword;
    TextView error, suggest;
    int name, lastName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.password, container, false);
        continue_ = view.findViewById(R.id.continue_);
        edtPassword = view.findViewById(R.id.edtPassword);
        error = view.findViewById(R.id.error);
        suggest = view.findViewById(R.id.suggest);
        edtPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    edtPassword.setErrorEnabled(false);
                    suggest.setVisibility(View.VISIBLE);
                    error.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        continue_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = edtPassword.getEditText().getText().toString().toLowerCase(Locale.ROOT).trim();
                int xxx = 0;
                int yyy=0;
                int zzz=0;
               if(s.length()>=6){
                   for (int i = 0; i < s.length(); i++) {
                       if (((int)s.charAt(i) > (int)'a' && (int)s.charAt(i) < (int)'z')) {
                           xxx = 2;
                       } else if ((int)s.charAt(i) >= (int)'0' && (int)s.charAt(i) <= (int)'9') {
                           yyy = 2;
                       } else {
                           zzz = 2;

                       }
                   }

                   if (xxx == 2 && yyy == 2 && zzz == 2) {
                       openFragCreateAccount openFragCreateAccount = (com.example.messagefake.openFragCreateAccount) getActivity();
                       openFragCreateAccount.getInfofromFragment(edtPassword.getEditText().getText().toString().trim(),"Mật khẩu");
                       openFragCreateAccount.openFrag(new frag_OTP(), "Xác Thực");
                   }else{
                       suggest.setVisibility(View.GONE);
                       error.setVisibility(View.VISIBLE);
                       edtPassword.setErrorEnabled(true);
                       edtPassword.setError(" ");
                   }
               }else {
                   suggest.setVisibility(View.GONE);
                   error.setVisibility(View.VISIBLE);
                   edtPassword.setErrorEnabled(true);
                   edtPassword.setError(" ");
               }
               }

        });

        return view;
    }
}
