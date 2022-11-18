package com.example.messagefake.dataFirebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {


    public SQLiteHelper(@Nullable Context context, String name) {
        super(context, "a", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table user(UID Text  not null primary key,"+
                "av blob ," +
                "cI blob ," +
                "name Text ," +
                "phone Text ," +
                "birthDay Text ," +
                "sex Text ," +
                "status Text ," +
                "Habitat Text ," +
                "Address Text ," +
                "Work Text ," +
                "Education Text ," +
                "string33 Text ," +
                "token Text," +
                "publicDetails Text," +
                "ShowInIntroduce Text," +
                "friendRequest Text )");



        db.execSQL("Create table storage(UID Text  not null primary key)");//lưu trữ
        db.execSQL("Create table messagewaiting(UID Text  not null primary key)");//đang chờ
        db.execSQL("Create table friends(UID Text  not null primary key)");//list Ban  bè
        db.execSQL("Create table block(UID Text  not null primary key)");//danh sách tôi block
        db.execSQL("Create table blockMe(UID Text  not null primary key)");//danh sách block tôi
        db.execSQL("Create table sreachRecent(UID Text  not null primary key," +
                "avata blob ," +
                "name Text ) "
        );//tìm kiếm gần đây
        db.execSQL("Create table message(UID Text  not null primary key," +
                "chatAll Text)");//đoạn chat
        db.execSQL("Create table key_message(UID Text  not null primary key," +
                "keymessage Text)");//luu key doan chat
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
