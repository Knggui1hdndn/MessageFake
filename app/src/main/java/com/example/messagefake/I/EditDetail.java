package com.example.messagefake.I;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagefake.Adapter.AdapterVertiEditDetail;
import com.example.messagefake.R;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.example.messagefake.user;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class EditDetail extends AppCompatActivity {
    RecyclerView recyclerviewEducation, recyclerviewWork;
    user user;
    DAO_sqlite dao_sqlite;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Button btnAddEducation, btnAddWork, btnAddAddress, btnAddHabitat, btnAddRelationship;
    CheckBox ckHabitat, ckAddress, ckRelationship;
    View viewHabitat, viewAddress, viewRelationship;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail);
        initview();
        setToolBar();
        user = dao_sqlite.getUser(uid).get(0);
        check("Sống tại " + user.getHabitat(), ckHabitat, btnAddHabitat, viewHabitat);
        check("Đến từ " + user.getAddress(), ckAddress, btnAddAddress, viewAddress);
        check(user.getRelationship(), ckRelationship, btnAddRelationship, viewRelationship);
        if (user.getWork() != null && user.getWork().size() > 0) {
            setAdapter((ArrayList<String>) user.getWork(), recyclerviewWork);
        }
        if (user.getEducation() != null && user.getEducation().size() > 0) {
            setAdapter((ArrayList<String>) user.getEducation(), recyclerviewEducation);
        }
    }

    private void check(String s, CheckBox checkBox, Button button, View view) {
        try {
            if (!s.endsWith("null")) {
                if(user.getShowInIntroduce()!=null){
                    if(user.getShowInIntroduce().contains(user.getAddress())){
                        checkBox.setChecked(true);
                    }else{
                        checkBox.setChecked(false);
                        String s1 = s + "\nSẽ không hiển thị trong phần giới thiệu\nnhưng vẫn sẽ công khai";
                        checkBox.setText(spannableString(s1));
                    }
                }else{
                    checkBox.setChecked(true);
                }
                checkBox.setText(s);
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        checkBox.setText(s);
                    } else {
                        String s1 = s + "\nSẽ không hiển thị trong phần giới thiệu\nnhưng vẫn sẽ công khai";
                        checkBox.setText(spannableString(s1));
                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        if (button.getId() == R.id.ckAddress) {
                            intent.putExtra("info", user.getAddress());
                        } else if (button.getId() == R.id.ckHabitat) {
                            intent.putExtra("info", user.getHabitat());
                        } else {
                            intent.putExtra("info", user.getRelationship());
                        }
                        startActivity(intent);
                    }
                });
                button.setVisibility(View.GONE);
            } else {
                checkBox.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(v -> {

                });
            }
        } catch (Exception e) {
            checkBox.setVisibility(View.GONE);
        }
    }

    private SpannableString spannableString(String s) {
        SpannableString string = new SpannableString(s);
        string.setSpan(new AbsoluteSizeSpan(40), TextUtils.indexOf(s, '\n'), s.length(), 0);
        string.setSpan(new ForegroundColorSpan(Color.GRAY), TextUtils.indexOf(s, '\n'), s.length(), 0);
        return string;
    }

    private void initview() {
        viewRelationship = findViewById(R.id.viewRelationship);
        viewAddress = findViewById(R.id.viewAddress);
        viewHabitat = findViewById(R.id.viewHabitat);
        dao_sqlite = new DAO_sqlite(this);
        recyclerviewEducation = findViewById(R.id.recyclerviewEducation);
        recyclerviewWork = findViewById(R.id.recyclerviewWork);
        btnAddEducation = findViewById(R.id.btnAddEducation);
        btnAddWork = findViewById(R.id.btnAddWork);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        btnAddHabitat = findViewById(R.id.btnAddHabitat);
        btnAddRelationship = findViewById(R.id.btnAddRelationship);
        ckHabitat = findViewById(R.id.ckHabitat);
        ckAddress = findViewById(R.id.ckAddress);
        ckRelationship = findViewById(R.id.ckRelationship);
    }

    private void setAdapter(ArrayList<String> list, RecyclerView rcy) {
        AdapterVertiEditDetail editDetail = new AdapterVertiEditDetail();
        editDetail.setData(list);
        rcy.setLayoutManager(new LinearLayoutManager(EditDetail.this, RecyclerView.VERTICAL, false));
        rcy.setAdapter(editDetail);
    }

    public void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);//set icon tren toolbar
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_backspace_245);//set icon menu
    }
}