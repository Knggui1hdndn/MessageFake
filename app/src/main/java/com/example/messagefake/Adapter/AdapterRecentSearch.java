package com.example.messagefake.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
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
import com.example.messagefake.user;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterRecentSearch extends RecyclerView.Adapter<AdapterRecentSearch.AdapterSearchViewHolder> {
    private ArrayList<user> users;
    private com.example.messagefake.openChat openChat;
    private Context context;
    DAO_sqlite dao_sqlite;

    @SuppressLint("NotifyDataSetChanged")
    public AdapterRecentSearch(com.example.messagefake.openChat openChat, Context context) {
        this.context = context;
        this.openChat = openChat;
        dao_sqlite = new DAO_sqlite(context);
    }

    public void setData(ArrayList<user> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterSearchViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recyview_recent_search, null));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSearchViewHolder holder, int position) {
        user user = users.get(position);
        if (!user.isStatus()) {
            holder.status.setVisibility(View.GONE);
        } else {
            holder.status.setVisibility(View.VISIBLE);

        }
        holder.txt_name.setText(TextUtils.substring(user.getName(), TextUtils.lastIndexOf(user.getName(), ' ') + 1, user.getName().length()));

        holder.itemverti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChat.openChat(user);
            }
        });
        try {
            byte b[] = dao_sqlite.getImg(user.getUID());
            if (b != null) {
                holder.img_searchView.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
            }else{
                Picasso.get().load(user.getAvata()).into(holder.img_searchView);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public int getItemCount() {
        if (users.size() > 8) {
            return 8;
        }
        return users.size();
    }


    public static class AdapterSearchViewHolder extends RecyclerView.ViewHolder {
        ImageView img_searchView;
        TextView txt_name;
        CardView status;
        RelativeLayout itemverti;

        public AdapterSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.imgStatus);
            img_searchView = itemView.findViewById(R.id.img_avatar);
            itemverti = itemView.findViewById(R.id.itemverti);
            txt_name = itemView.findViewById(R.id.txt_name);
        }
    }
}
