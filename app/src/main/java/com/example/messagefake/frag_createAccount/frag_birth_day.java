package com.example.messagefake.frag_createAccount;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.messagefake.R;
import com.example.messagefake.openFragCreateAccount;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class frag_birth_day extends Fragment {
    Button continue_birth_day;
    TextView suggest, error, old;
    DatePicker birth_day;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.birth_day, container, false);
        continue_birth_day = view.findViewById(R.id.continue_birth_day);
        suggest = view.findViewById(R.id.suggest);
        error = view.findViewById(R.id.error);
        birth_day = view.findViewById(R.id.birth_day);
        old = view.findViewById(R.id.old);
        DateFormat df = new SimpleDateFormat("yyyy");
        String date1 = df.format(Calendar.getInstance().getTime());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            birth_day.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                    old.setText(Integer.parseInt(date1) - i + " Tuổi");
                    error.setVisibility(View.GONE);
                    suggest.setVisibility(View.VISIBLE);

                }
            });
        }
        continue_birth_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(date1)-birth_day.getYear()>0) {
                    openFragCreateAccount openFragCreateAccount= (com.example.messagefake.openFragCreateAccount) getActivity();
                    openFragCreateAccount.getInfofromFragment(String.valueOf(birth_day.getDayOfMonth()+"/"+birth_day.getMonth()+"/"+birth_day.getYear()),"Ngày sinh");
                    openFragCreateAccount.openFrag(new frag_sex(),"Giới tính");
                }else {
                    error.setVisibility(View.VISIBLE);
                    suggest.setVisibility(View.GONE);

                }
            }
        });
        return view;
    }
}
