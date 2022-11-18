package com.example.messagefake.I;

import static com.example.messagefake.dataFirebase.DAO_Friebase.updateStatus;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.messagefake.R;

public class Status_Custom extends AppCompatActivity {
    Switch aSwitch;
    TextView txt;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = getSharedPreferences("ok", MODE_PRIVATE);
        if (preferences.getString("ok", "").equals("Light1")) {
            setTheme(R.style.Theme_Light1);

        } else {
            setTheme(R.style.Theme_Light);

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_custom);
        setToolBar();
        aSwitch = findViewById(R.id.switch1);
        txt = findViewById(R.id.txt);
        if (preferences.getBoolean("status", true)) {
            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }
        setTextSpannable();
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.edit().clear().apply();
                if (b) {
                    preferences.edit().putBoolean("status", true).apply();
                    updateStatus(true);

                } else {
                    preferences.edit().putBoolean("status", false).apply();
                    updateStatus(false);

                }
            }
        });


    }

    private void setTextSpannable() {
        String noiDung = getString(R.string.b_n_s_kh_ng_bi_t_khi_n_o_b_n_b_v_c_c_quan_h_k_t_n_i_c_a_m_nh_ang_ho_t_ng_ho_c_ho_t_ng_g_n_y_tr_n_trang_c_nh_n_n_y_m_b_o_h_kh_ng_th_y_c_th_ng_tin_n_y_m_i_n_i_b_n_ang_d_ng_messenger_khi_tr_ng_th_i_ho_t_ng_c_a_b_n_s_kh_ng_hi_n_th_n_a_t_m_hi_u_th_m);
        SpannableString string = new SpannableString(noiDung);
        string.setSpan(new ForegroundColorSpan(Color.parseColor("#0000ff")), TextUtils.lastIndexOf(noiDung, '.'), noiDung.length(), 0);
        string.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);    // this remove the underline
            }

            @Override
            public void onClick(@NonNull View view) {
                recreate();
            }
        }, TextUtils.lastIndexOf(noiDung, '.'), noiDung.length(), 0);
        txt.setMovementMethod(LinkMovementMethod.getInstance());
        txt.setText(string);
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
        actionbar.setDisplayHomeAsUpEnabled(true);//set icon trên toolbar
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_backspace_245);//set icon menu
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