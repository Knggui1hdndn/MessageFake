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

public class frag_phone_number extends Fragment {
    Button continue_phone_number;
    TextInputLayout phone_number, email;
    TextView error, suggest;
     @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.phone_number,container,false);
        continue_phone_number=view.findViewById(R.id.continue_phone_number);
        phone_number=view.findViewById(R.id.phone_number);
        email=view.findViewById(R.id.email);
        error=view.findViewById(R.id.error);
        suggest=view.findViewById(R.id.suggest);

        phone_number.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              if(charSequence.toString().trim().length()>0){
                  phone_number.setErrorEnabled(false);
                  email.setErrorEnabled(false);

                  error.setVisibility(View.GONE);
                  suggest.setVisibility(View.VISIBLE);
              }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()>0){
                    phone_number.setErrorEnabled(false);
                    email.setErrorEnabled(false);
                    error.setVisibility(View.GONE);
                    suggest.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        continue_phone_number.setOnClickListener(view1 -> {

            if(phone_number.getEditText().getText().toString().length()==10&&email.getEditText().getText().toString().length()>0){
                openFragCreateAccount openFragCreateAccount= (com.example.messagefake.openFragCreateAccount) getActivity();
                openFragCreateAccount.getInfofromFragment(phone_number.getEditText().getText().toString().trim(),"Số điện thoại");
                openFragCreateAccount.getInfofromFragment(email.getEditText().getText().toString().trim(),"email");
                assert openFragCreateAccount != null;
                openFragCreateAccount.openFrag(new frag_password(),"Mật khẩu");
            } else{
                error.setVisibility(View.VISIBLE);
                suggest.setVisibility(View.GONE);
                email.setErrorEnabled(true);
                phone_number.setErrorEnabled(true);
                email.setError(" ");
                phone_number.setError(" ");
            }
        });
        return view;
    }
}
