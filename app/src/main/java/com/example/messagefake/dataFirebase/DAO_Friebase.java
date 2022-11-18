package com.example.messagefake.dataFirebase;

import androidx.annotation.NonNull;

import com.example.messagefake.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAO_Friebase {


    public static DatabaseReference database_Reference = FirebaseDatabase.getInstance().getReference("user");
    public static DatabaseReference database_Reference1 = FirebaseDatabase.getInstance().getReference("message");


    public static ArrayList<user> getDataActiveUserFromFireBases() {
        ArrayList<user> users = new ArrayList<>();
        Query query = database_Reference.orderByChild("status").equalTo(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                user user1 = snapshot.getValue(user.class);
                if (user1 != null) {
                    if (user1.isStatus()) {
                        users.add(user1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return users;
    }

    //  cần thời gian để load data
    public static ArrayList<user> getDataUserFromFireBase() {
        ArrayList<user> users = new ArrayList<>();
        database_Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    user user1 = dataSnapshot.getValue(user.class);
                    users.add(user1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return users;
    }

    public static void updateStatus(Boolean aBoolean) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("user/" + FirebaseAuth.getInstance().getUid() + "/status");
        databaseReference.setValue(aBoolean);
    }

    public static String getEmailUserSend() {
        return uid;
    }

    public static ArrayList<String> getFriendsFireBase() {
        ArrayList<String> strings = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("user").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    strings.clear();
                    user user1 = snapshot.getValue(user.class);
                    List<String> getFriends = user1.getFriends();
                    if (getFriends.size() > 0) {
                        strings.addAll(getFriends);
                    } else {
                        strings.add(uid);
                        addFriends(strings);

                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return strings;
    }

    public static void deleteMessage(String path) {
        FirebaseDatabase.getInstance().getReference("message/" + path + "/chatAll" + uid).setValue(" ");
    }

    public static String uid = a();

    private static String a() {
        try {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (Exception e) {
            return "uid";
        }
        return uid;
    }

    public static void addMessageWaitingFireBase(ArrayList<String> getMessageWaitingFireBase) {
        FirebaseDatabase.getInstance().getReference("user/" + uid + "/messageWaiting").setValue(getMessageWaitingFireBase);
    }

    public static void addBlock(ArrayList<String> getBlock) {
        FirebaseDatabase.getInstance().getReference("user/" + uid + "/block").setValue(getBlock);
    }

    public static void addArchivedMessage(ArrayList<String> getArchivedMessage) {
        FirebaseDatabase.getInstance().getReference("user/" + uid + "/archivedMessage").setValue(getArchivedMessage);
    }

    public static void addFriends(ArrayList<String> getFriendsFireBase) {
        FirebaseDatabase.getInstance().getReference("user/" + uid + "/friends").setValue(getFriendsFireBase);
    }

    public static boolean addFriendRequestReceive(ArrayList<String> addFriendRequest,String uidReceiver) {
       return FirebaseDatabase.getInstance().getReference("user/" + uidReceiver + "/friendRequest").setValue(addFriendRequest).isSuccessful();
     }



}
