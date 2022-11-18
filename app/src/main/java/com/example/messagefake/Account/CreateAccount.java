package com.example.messagefake.Account;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.messagefake.R;
import com.example.messagefake.openFragCreateAccount;

public class CreateAccount extends AppCompatActivity implements openFragCreateAccount {
    Toolbar toolbar;
    ActionBar actionbar;
    FrameLayout viewCreateAccount;
    FragmentManager fragmentManager;
    LinearLayout viewGroup;
    private boolean statusNw;
    Bundle bundle = new Bundle();

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        addEventView();
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
         assert actionbar != null;
         actionbar.setDisplayHomeAsUpEnabled(true);//set icon tren toolbar
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_backspace_2452);//set icon menu
        fragmentManager = getSupportFragmentManager();
        actionbar.setTitle("Họ tên");

        statusNetwork();

    }

    private void addEventView() {
        toolbar = findViewById(R.id.toolBarCreateAccount);
        viewCreateAccount = findViewById(R.id.viewCreateAccount);
        viewGroup = findViewById(R.id.viewGroup);
    }

    @Override
    public void openFrag(Fragment fragment1, String title) {
        if (fragment1 != null) {
            actionbar.setTitle(title);
            if (title == "Xác Thực") {
                fragment1.setArguments(bundle);
            }
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.batdaufrag, R.anim.ketthucfrag);
            ft.replace(R.id.viewCreateAccount, fragment1, title);
            ft.addToBackStack(title);
            ft.commit();
        }
    }

    @Override
    public void getInfofromFragment(String info, String name) {
        bundle.putString(name, info);
    }

    @Override
    public void onBackPressed() {
        if (actionbar.getTitle().toString() != "") {
            createDialog();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            createDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);
        builder.setTitle("Bạn có muốn dừng tài khoản không?")
                .setMessage("Nếu dừng bây giờ, bạn sẽ mất toàn bộ tiến trình cho đến nay")
                .setPositiveButton("Dừng tạo tài khoản", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("Tiếp tục ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.show();


    }

    public void statusNetwork() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getActiveNetworkInfo() != null) {
                    statusNw = true;
                } else {
                    statusNw = false;
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, intentFilter);
    }

    public boolean isStatusNw() {
        return statusNw;
    }

}





