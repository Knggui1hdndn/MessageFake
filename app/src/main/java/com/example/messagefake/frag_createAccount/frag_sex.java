package com.example.messagefake.frag_createAccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.messagefake.R;
import com.example.messagefake.openFragCreateAccount;

public class frag_sex extends Fragment {
    Button continue_sex;
RadioButton rdisex;
RadioGroup group;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.sex,container,false);
        continue_sex=view.findViewById(R.id.continue_sex);
        group=view.findViewById(R.id.group);
        int x= group.getCheckedRadioButtonId();
        rdisex=view.findViewById(x);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int x= group.getCheckedRadioButtonId();
                rdisex=view.findViewById(x);
            }
        });

        continue_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragCreateAccount openFragCreateAccount= (com.example.messagefake.openFragCreateAccount) getActivity();
                openFragCreateAccount.getInfofromFragment(rdisex.getText().toString(),"Giới tính");

                openFragCreateAccount.openFrag(new frag_phone_number(),"Số điện thoại và email");
            }
        });
        return view;
    }
}
