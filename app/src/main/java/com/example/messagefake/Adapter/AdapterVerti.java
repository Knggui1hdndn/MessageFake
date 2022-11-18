package com.example.messagefake.Adapter;

import static com.example.messagefake.dataFirebase.DAO_Friebase.getEmailUserSend;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagefake.R;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.example.messagefake.openBottomSheet;
import com.example.messagefake.openChat;
import com.example.messagefake.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AdapterVerti extends RecyclerView.Adapter<AdapterVerti.AdapterVertiViewHolder> {
    private List<user> users;
    private com.example.messagefake.openChat openChat;
    private openBottomSheet sheet;
    DAO_sqlite dao_sqlite;

    public AdapterVerti(openChat openChat, openBottomSheet sheet) {
        this.openChat = openChat;
        int i = 0;
        this.sheet = sheet;
    }

    public void setData(List<user> users) {
        this.users = users;

        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public AdapterVertiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        dao_sqlite = new DAO_sqlite(parent.getContext());
        return new AdapterVertiViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyview_verti, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterVertiViewHolder holder, @SuppressLint("RecyclerView") int position) {

        user user = users.get(position);
        FirebaseDatabase.getInstance().getReference("message/" + dao_sqlite.getKeyMessage(user.getUID())).child("chatAll" + getEmailUserSend()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String s1 = snapshot.getValue(String.class);
                String clip[];
                List<String> strings;
                if (s1 != null && !s1.equals(" ")) {
                    clip = s1.trim().split("\n");
                    strings = new ArrayList<>(Arrays.asList(clip));
                    dao_sqlite.addMessage((ArrayList<String>) strings, user.getUID());
                    String sLate = strings.get(strings.size() - 1);
                    String clip1[];
                    if (sLate.endsWith("dHiKQUqtRuqs")) {
                        clip1 = sLate.split("dHiKQUqtRuqs");
                    } else {
                        clip1 = sLate.split("70sWUEEum");
                    }
                    String time = TextUtils.substring(clip1[0], clip1[0].length() - 5, clip1[0].length());
                    String message = TextUtils.substring(clip1[0], 0, clip1[0].length() - 5);
                    holder.timeSend_vertical.setText(time);
                    holder.message_vertical.setText(message);
                    if (dao_sqlite.getMessage(user.getUID()).length < strings.size()) {
                        holder.timeSend_vertical.setText(string(time));
                        holder.message_vertical.setText(string(message));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (dao_sqlite.getMessage(user.getUID()) != null) {
            String late = dao_sqlite.getMessage(user.getUID())[dao_sqlite.getMessage(user.getUID()).length - 1];
            String clip1[];
            if (late.endsWith("dHiKQUqtRuqs")) {
                clip1 = late.split("dHiKQUqtRuqs");
            } else {
                clip1 = late.split("70sWUEEum");
            }
            String time = TextUtils.substring(clip1[0], clip1[0].length() - 5, clip1[0].length());
            String message = TextUtils.substring(clip1[0], 0, clip1[0].length() - 5);
            holder.timeSend_vertical.setText(time);
            holder.message_vertical.setText(message);
        }

        if (!user.isStatus()) {
            holder.imgStatus.setVisibility(View.GONE);
        } else {
            holder.imgStatus.setVisibility(View.VISIBLE);
        }
        if (user.getUID().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))) {
            user.setName("Tôi");
        }
        holder.name_vertical.setText(user.getName());

        holder.itemverti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChat.openChat(user);
            }
        });
        holder.itemverti.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                sheet.BottomSheetBehavior(position);
                return true;
            }
        });
        try {
            byte b[] = dao_sqlite.getImg(user.getUID());
            if (b != null) {
                holder.img_avatar_vertical.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
            } else {
                Picasso.get().load(user.getAvata()).into(holder.img_avatar_vertical);
            }
        } catch (Exception e) {

        }

    }


    private SpannableString string(String s) {
        SpannableString string = new SpannableString(s);
        string.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), 0);
        return string;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class AdapterVertiViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img_avatar_vertical;
        private final TextView name_vertical;
        private final TextView message_vertical;
        private final TextView timeSend_vertical;
        RelativeLayout itemverti;
        CardView imgStatus;

        public AdapterVertiViewHolder(@NonNull View itemView) {
            super(itemView);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            itemverti = itemView.findViewById(R.id.itemverti);
            img_avatar_vertical = itemView.findViewById(R.id.img_avatar_vertical);
            name_vertical = itemView.findViewById(R.id.name_vertical);
            message_vertical = itemView.findViewById(R.id.message_vertical);
            timeSend_vertical = itemView.findViewById(R.id.timeSend_vertical);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void removeItem(int i) {
        users.remove(i);
        notifyDataSetChanged();
    }

}
