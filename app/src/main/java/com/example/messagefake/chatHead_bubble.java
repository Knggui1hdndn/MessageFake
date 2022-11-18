package com.example.messagefake;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class chatHead_bubble extends Service {
    View chatHead_bubble;
    WindowManager windowManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("sssssrs", "onCreate: " + "dddok");
        chatHead_bubble = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams parama = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        parama.gravity = Gravity.TOP | Gravity.LEFT;
        parama.x = 0;
        parama.y = 0;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(chatHead_bubble, parama);
        LottieAnimationView view = chatHead_bubble.findViewById(R.id.a);
        AtomicInteger initialx = new AtomicInteger();
        AtomicInteger initialY = new AtomicInteger();
        AtomicReference<Float> touchX = new AtomicReference<>((float) 0);
        AtomicReference<Float> touchY = new AtomicReference<>((float) 0);
        AtomicInteger lastAction = new AtomicInteger(-1);
        view.setOnTouchListener((v, event) -> {


//            getRowX: tọa độ của điểm tiếp xúc so với màn hình
//
//            getX: tọa độ của điểm tiếp xúc so với nút
            if (event.getAction() == MotionEvent.ACTION_DOWN) {//khi người dùng chạm vào view lần đầu tiên,
                Log.d("dsssssssssssssssss", "onStartCommand: " + "ACTION_DOWN");

                initialx.set(parama.x);
                initialY.set(parama.y);
                touchX.set(event.getRawX());
                touchY.set(event.getRawY());
                lastAction.set(event.getAction());
                return true;

            }
            if (event.getAction() == MotionEvent.ACTION_UP) {//MotionEvent.ACTION_UP Một cử chỉ được nhấn đã kết thúc
                Log.d("dsssssssssssssssss", "onStartCommand: " + "ACTION_UP" + lastAction);
                if (lastAction.get() ==MotionEvent.ACTION_DOWN) {
                    startActivity(new Intent(getApplicationContext(), MainActivity3.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                lastAction.set(event.getAction());
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {//Chuyển động chứa điểm gần đây nhất
                Log.d("dsssssssssssssssss", "onStartCommand: " + "ACTION_MOVE");

                parama.x = initialx.get() + (int) (event.getRawX() - touchX.get());
                parama.y = initialY.get() + (int) (event.getRawY() - touchY.get());
                windowManager.updateViewLayout(chatHead_bubble, parama);
                lastAction.set(event.getAction());
                return true;
            }


            return false;
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead_bubble != null) {
            windowManager.removeView(chatHead_bubble);
        }
    }

}
