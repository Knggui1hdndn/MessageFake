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

import java.util.Objects;

public class frag_name extends Fragment {
    Button continue_c;
    TextInputLayout create_name, create_last_name;
    TextView error, suggest;
    int name, lastName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.name, container, false);
        continue_c = view.findViewById(R.id.continue_c);
        create_name = view.findViewById(R.id.create_name);
        error = view.findViewById(R.id.error);
        suggest = view.findViewById(R.id.suggest);
        create_last_name = view.findViewById(R.id.create_last_name);
        TextChangedListener();

        continue_c.setOnClickListener(new View.OnClickListener() {
             @Override
            public void onClick(View view) {
                if (name > 0 && lastName > 0) {
                    openFragCreateAccount   openFragCreateAccount = (com.example.messagefake.openFragCreateAccount) getActivity();
                    openFragCreateAccount.getInfofromFragment(create_last_name.getEditText().getText().toString().trim().concat(" "+create_name.getEditText().getText().toString().trim()),"Tên");
                    openFragCreateAccount.openFrag(new frag_birth_day(), "Ngày sinh");
                }
                if (name == 0) {
                    error.setText("Vui lòng nhập tên của bạn.");
                    showError();
                }
                if (lastName == 0) {
                    error.setText("Vui lòng nhập họ của bạn.");
                    showError();
                }
                if (name == 0 && lastName == 0) {
                    error.setText("Vui lòng nhập họ và tên của bạn.");
                    showError();
                }
            }
        });

        return view;
    }

    private void TextChangedListener() {

        Objects.requireNonNull(create_name.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = charSequence.length();
                if (charSequence.toString().trim().length() > 0) {
                    hideError();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        create_last_name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lastName = charSequence.length();
                if (charSequence.toString().trim().length() > 0) {
                    hideError();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void showError() {
        create_name.setErrorEnabled(true);
        create_last_name.setErrorEnabled(true);
        suggest.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        create_name.setError(" ");
        create_last_name.setError(" ");

    }

    private void hideError() {
        create_name.setErrorEnabled(false);
        create_last_name.setErrorEnabled(false);
        suggest.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
    }

}
