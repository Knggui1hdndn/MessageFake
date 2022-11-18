package com.example.messagefake.dataFirebase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.messagefake.user;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DAO_sqlite {
    static String UID = "UID";
    static String av = "av";
    static String cI = "cI";
    static String name = "name";
    static String phone = "phone";
    static String birthDay = "birthDay";
    static String sex = "sex";
    static String status = "status";
    static String Habitat = "Habitat";
    static String Address = "Address";
    static String Work = "Work";
    static String Education = "Education";
    static String string33 = "string33";
    static String token = "token";
    static String table = "user";
    SQLiteHelper sqLiteHelper;
    SQLiteDatabase db;

    public DAO_sqlite(Context context) {
        this.sqLiteHelper = new SQLiteHelper(context, FirebaseAuth.getInstance().getCurrentUser().getUid());
        db = sqLiteHelper.getWritableDatabase();
    }

    private byte[] getLogoImage(String url) {
        ByteArrayBuffer baf = null;
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            InputStream is = ucon.getInputStream();

            BufferedInputStream bis = new BufferedInputStream(is);
            baf = new ByteArrayBuffer(500);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            return baf.toByteArray();
        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return null;
    }


    public void addSearchRecent(user user) {
        db = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        byte[] logoImage = getLogoImage(user.getAvata());
        values.put("avata", logoImage);
        values.put(UID, user.getUID());
        values.put(name, user.getName());

        int x = (int) db.insert("sreachRecent", null, values);

    }

    public void deleteSearchRecent(String UID) {
        db = sqLiteHelper.getWritableDatabase();

        db.delete("sreachRecent", "UID=?", new String[]{UID});
    }

    public void clearSearchRecent() {
        db = sqLiteHelper.getWritableDatabase();

        long x = db.delete("sreachRecent", null, null);
        Log.d("deleteSearchRecent", "deleteSearchRecent: " + x);

    }

    public ArrayList<user> getSearchRecent() {
        db = sqLiteHelper.getReadableDatabase();
        ArrayList<user> users = new ArrayList<>();
        Cursor cursor1 = getData("select * from user ");
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(" Select * from   sreachRecent", null);
        while (cursor.moveToNext()) {
            user user = new user();
            user.setAv(cursor.getBlob(1));
            user.setAvata("");
            user.setStatus(false);
            user.setUID(cursor.getString(0));
            user.setName(cursor.getString(2));

            users.add(user);
            Log.d("aaaaaaaaaaaaaa", "getSearchRecent: " + user + users.size());
        }


        cursor.close();
        return users;
    }


    public void deleteUser(String uid) {
        db = sqLiteHelper.getWritableDatabase();
        db.delete(table, UID + "=?", new String[]{uid});
    }

    public void addUser(user user) {
        Log.d("onBindViewHolder", "addUser: " + user.getEducation());
        // db.execSQL("Delete   from user");
        ContentValues values = new ContentValues();
        byte[] av1 = getLogoImage(user.getAvata());
        byte[] cI1 = getLogoImage(user.getCoverImage());

        values.put(UID, user.getUID());
        values.put(av, av1);
        values.put(cI, cI1);
        values.put(name, user.getName());
        if (user.getUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            values.put(phone, user.getPhone());
            values.put(birthDay, user.getBirthDay());
            values.put(sex, user.getSex());
            values.put(Work, setData(user.getWork()));
            values.put("publicDetails", setData(user.getPublicDetails()) );
            values.put("ShowInIntroduce", setData(user.getShowInIntroduce()) );
            values.put(Education, setData(user.getEducation()) );
            values.put(status, user.isStatus());
            values.put(Habitat, user.getHabitat());
            values.put(Address, user.getAddress());
            values.put(string33, user.getString33());
            values.put(token, user.getToken());
            values.put("friendRequest", setData(user.getFriendRequest()));
        }

        try {
            int x = (int) db.insert(table, null, values);
            Log.d("getUser", x + "" + user.getFriendRequest());
            if (x < 0) {
                int y = db.update("user", values, UID + "=?", new String[]{user.getUID()});
                Log.d("getUser", "getUser: " + y + user.isStatus());
            }
        } catch (Exception e) {
            Log.d("addUser", "addUser: " + e.getMessage());
        }
    }

    private String setData(List<String>strings) {
        StringBuilder builder = new StringBuilder();
        if (strings!= null) {
            for (String s : strings) {
                builder.append(s).append("\n");
            }
        }else {
            return null;
        }
        return builder.toString();
    }

    public Cursor getData(String sql) {
        db = sqLiteHelper.getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    @SuppressLint("Range")
    public ArrayList<user> getUser() {
        db = sqLiteHelper.getReadableDatabase();
        ArrayList<user> users = new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("Select * from   user where UID not in  (SELECT UID FROM storage) and  UID not in  (SELECT UID FROM messagewaiting)  ", null);
        while (cursor.moveToNext()) {

            user user = new user();
            user.setUID(cursor.getString(0));
            user.setAv(cursor.getBlob(1));
            user.setAvata("");
            user.setName(cursor.getString(3));
            user.setStatus(Boolean.parseBoolean(cursor.getString(7)));
            if (user.getUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                user.setPhone(cursor.getString(4));
                user.setcI(cursor.getBlob(2));

                user.setBirthDay(cursor.getString(5));
                user.setSex(cursor.getString(6));
                user.setHabitat(cursor.getString(8));
                user.setAddress(cursor.getString(9));
                String s = cursor.getString(10);
                if (s != null) {
                    String s1[] = s.split("\n");
                    user.setWork(new ArrayList<>(Arrays.asList(s1)));
                }else{
                    user.setWork(null);
                }
                String s2 = cursor.getString(11);
                if (s2 != null) {
                    String s1[] = s2.split("\n");
                    user.setEducation(new ArrayList<>(Arrays.asList(s1)));
                }else{
                    user.setEducation(null);
                }
                String s3 = cursor.getString(14);
                if (s3 != null) {
                    String s1[] = s3.split("\n");
                    user.setPublicDetails(new ArrayList<>(Arrays.asList(s1)));
                }else{
                    user.setPublicDetails(null);
                }
                String s4 = cursor.getString(15);
                if (s4 != null) {
                    String s1[] = s4.split("\n");
                    user.setShowInIntroduce(new ArrayList<>(Arrays.asList(s1)));
                }else{
                    user.setShowInIntroduce(null);
                }
                String s5 = cursor.getString(16);
                if (s5 != null) {
                    String s1[] = s5.split("\n");
                    user.setFriendRequest(new ArrayList<>(Arrays.asList(s1)));
                }else{
                    user.setFriendRequest(null);
                }
                user.setString33(cursor.getString(12));
                user.setToken(cursor.getString(13));
            }
            users.add(user);
            Log.d("aaaaaaaaaaaaaa9", "getUser: " + user.isStatus() + users.size());
        }
        return users;
    }

    public ArrayList<user> getUser(String e) {
        db = sqLiteHelper.getReadableDatabase();
        ArrayList<user> users = new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("Select * from   user where UID not in  (SELECT UID FROM storage) and  UID not in  (SELECT UID FROM messagewaiting)  and UID='" + e + "'", null);
        while (cursor.moveToNext()) {

            user user = new user();
            user.setUID(cursor.getString(0));
            user.setAv(cursor.getBlob(1));

            user.setAvata("");
            user.setName(cursor.getString(3));
            user.setStatus(Boolean.parseBoolean(cursor.getString(7)));
            if (user.getUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                user.setPhone(cursor.getString(4));
                user.setcI(cursor.getBlob(2));

                user.setBirthDay(cursor.getString(5));
                user.setSex(cursor.getString(6));
                user.setHabitat(cursor.getString(8));
                user.setAddress(cursor.getString(9));
                String s = cursor.getString(10);
                if (s != null) {
                    String s1[] = s.split("\n");
                    user.setWork(new ArrayList<>(Arrays.asList(s1)));
                }else{
                    user.setWork(null);
                }
                String s2 = cursor.getString(11);
                if (s2 != null) {
                    String s1[] = s2.split("\n");
                    user.setEducation(new ArrayList<>(Arrays.asList(s1)));
                }else{
                    user.setEducation(null);
                }
                String s3 = cursor.getString(14);
                if (s3 != null) {
                    String s1[] = s3.split("\n");
                    user.setPublicDetails(new ArrayList<>(Arrays.asList(s1)));
                }else{
                    user.setPublicDetails(null);
                }
                String s4 = cursor.getString(15);
                if (s4 != null) {
                    String s1[] = s4.split("\n");
                    user.setShowInIntroduce(new ArrayList<>(Arrays.asList(s1)));
                }else{
                    user.setShowInIntroduce(null);
                }
                String s5 = cursor.getString(16);
                if (s5 != null) {
                    String s1[] = s5.split("\n");
                    user.setFriendRequest(new ArrayList<>(Arrays.asList(s1)));
                }else{
                    user.setFriendRequest(null);
                }
                user.setString33(cursor.getString(12));
                user.setToken(cursor.getString(13));
            }
            users.add(user);
            Log.d("aaaaaaaaaaaaaa9", "getUser: " + user.isStatus() + users.size());
        }
        return users;
    }

    public void updateImg(String uid, String uri) {
        ContentValues values = new ContentValues();
        values.put(av, getLogoImage(uri.toString()));
        int x = (int) db.update("user", values, "UID=?", new String[]{uid});

    }

    public byte[] getImg(String uid) {
        db = sqLiteHelper.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("Select * from user where UID='" + uid + "'", null);
        while (cursor.moveToNext()) {
            Log.d("aaaaaa8", "getImg: " + cursor.getBlob(1));
            return cursor.getBlob(1);
        }
        return null;
    }

    public byte[] getImgRecentSearch(String uid) {
        db = sqLiteHelper.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("Select * from sreachRecent where UID='" + uid + "'", null);
        while (cursor.moveToNext()) {
            Log.d("aaaaaa8", "getImg: " + cursor.getBlob(1));
            return cursor.getBlob(1);
        }
        return null;
    }

    //friends auth
    public void addFriends(String uid) {
        ContentValues values = new ContentValues();
        values.put(UID, uid);
        int x = (int) db.insert("friends", null, values);
        Log.d("addStorage", "addStorage: " + x);

    }

    public void deleteFriend(String uid) {
        db = sqLiteHelper.getWritableDatabase();
        int x = (int) db.delete("friends", UID + "=?", new String[]{uid});
        Log.d("deleteStorage", "deleteStorage: " + x);
    }

    public List<String> getFriends() {
        List<String> getFriends = new ArrayList<>();
        db = sqLiteHelper.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("Select * from friends  ", null);
        while (cursor.moveToNext()) {
            getFriends.add(cursor.getString(0));
        }
        return getFriends;
    }

    //my block
    public void addBlockMe(String uid) {
        ContentValues values = new ContentValues();
        values.put(UID, uid);
        int x = (int) db.insert("blockMe", null, values);

    }

    public void deleteBlockMe(String uid) {
        db = sqLiteHelper.getWritableDatabase();
        int x = (int) db.delete("blockMe", UID + "=?", new String[]{uid});
    }

    public List<String> getListBlockMe() {
        List<String> getListBlock = new ArrayList<>();
        db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * From blockMe", null);
        while (cursor.moveToNext()) {
            {
                getListBlock.add(cursor.getString(0));
            }
        }
        return getListBlock;
    }

    //my block
    public void addBlock(String uid) {
        ContentValues values = new ContentValues();
        values.put(UID, uid);
        int x = (int) db.insert("block", null, values);

    }

    public void deleteBlock(String uid) {
        db = sqLiteHelper.getWritableDatabase();
        int x = (int) db.delete("block", UID + "=?", new String[]{uid});
    }

    public List<String> getListBlock() {
        List<String> getListBlock = new ArrayList<>();
        db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * From block", null);
        while (cursor.moveToNext()) {
            {
                getListBlock.add(cursor.getString(0));
            }
        }
        return getListBlock;
    }

    //kaymessage
    public void addKeyMessage(String uid, String keyMessage) {
        ContentValues values = new ContentValues();
        values.put(UID, uid);
        values.put("keymessage", keyMessage);
        int x = (int) db.insert("key_message", null, values);
        Log.d("addKeyMessage", "addKeyMessage: " + uid + keyMessage + x);

    }

    @SuppressLint("Range")
    public String getKeyMessage(String uid) {

        Cursor cursor = getData("SELECT * from  key_message where UID='" + uid + "'");
        while (cursor.moveToNext()) {
            return cursor.getString(1);
        }
        return null;
    }

    //Tin nhắn từ người lạ
    public void addmessagewaiting(String uid) {
        ContentValues values = new ContentValues();
        values.put(UID, uid);
        int x = (int) db.insert("messagewaiting", null, values);
        Log.d("messagewaiting", "addmessagewaiting: " + x + uid);

    }

    public void deleteMessageWaiting(String uid) {
        db = sqLiteHelper.getWritableDatabase();
        int x = (int) db.delete("messagewaiting", UID + "=?", new String[]{uid});
        Log.d("deleteStorage", "deleteStorage: " + x);
    }

    //lưu trữ
    public void addStorage(String uid) {
        ContentValues values = new ContentValues();
        values.put(UID, uid);
        int x = (int) db.insert("storage", null, values);
        Log.d("addStorage", "addStorage: " + x);

    }

    public void deleteStorage(String uid) {
        db = sqLiteHelper.getWritableDatabase();
        int x = (int) db.delete("storage", UID + "=?", new String[]{uid});
        Log.d("deleteStorage", "deleteStorage: " + x);
    }

    @SuppressLint("Range")
    public List<user> getStorageOrMessageWaiting(String storage, String messagewaiting) {
        List<user> users = new ArrayList<>();
        String table = storage != null ? storage : messagewaiting;
        Cursor cursor = getData("SELECT * from " + table + " INNER JOIN user ON " + table + ".UID = user.UID ");
        while (cursor.moveToNext()) {
            user user = new user();
            user.setUID(cursor.getString(1));
            user.setAv(cursor.getBlob(2));
            user.setAvata("");
            user.setName(cursor.getString(4));

            if (storage == null) {
                user.setStatus(false);
            }
            user.setStatus(Boolean.parseBoolean(cursor.getString(8)));

            users.add(user);
            Log.d("getStorage", "getStorage: " + user.toString());
        }
        return users;
    }

    @SuppressLint("Range")
    public List<String> getMessageWaiting() {
        List<String> users = new ArrayList<>();

        Cursor cursor = getData("SELECT * from  messagewaiting ");
        while (cursor.moveToNext()) {

            users.add(cursor.getString(0));
        }
        Log.d("getMessageWaiting", "getMessageWaiting: " + users);
        return users;
    }

    @SuppressLint("Range")
    public List<String> getStorage() {
        List<String> users = new ArrayList<>();

        Cursor cursor = getData("SELECT * from  storage ");
        while (cursor.moveToNext()) {

            users.add(cursor.getString(0));
        }
        return users;
    }

    public String[] getMessage(String UID) {
        Cursor cursor = getData("select *  from message WHERE UID='" + UID + "' ");
        while (cursor.moveToNext()) {
            String s[] = cursor.getString(1).split("\n");
            Log.d("aaa", "getMessage: " + s.length);
            return s;
        }
        return null;
    }

    public void addMessage(ArrayList<String> strings, String uid) {
        if (strings.size() > 0) {
            try {    StringBuilder builder = new StringBuilder();

            for (String s : strings) {
                builder.append(s).append("\n");
            }
            ContentValues values = new ContentValues();
            values.put(UID, uid);
            values.put("chatAll", builder.toString());

                int x = (int) db.insert("message", null, values);
                if (x < 0) {
                    int y = db.update("message", values, UID + "=?", new String[]{uid});
                    Log.d("sssss", "addMessage: " + "y=" + y);
                }

            } catch (Exception e) {
                Log.d("addUser", "addUser: " + e.getMessage());
            }
        }
    }

    public void deleteChatUser(String uid) {
        int x = (int) db.delete("message", UID + "=?", new String[]{uid});
        Log.d("deleteChatUser", "deleteChatUser: "+x+UID);

    }

}

