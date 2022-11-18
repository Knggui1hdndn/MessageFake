package com.example.messagefake.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagefake.R;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.example.messagefake.user;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class AdapterHistorySearch extends RecyclerView.Adapter<AdapterHistorySearch.AdapterSearchViewHolder> {
    private ArrayList<user> users;
    private com.example.messagefake.openChat openChat;
    private Context context;
    DAO_sqlite dao_sqlite;

    @SuppressLint("NotifyDataSetChanged")
    public AdapterHistorySearch(ArrayList<user> users, Context context) {
        this.users = users;
        this.context = context;
        this.openChat = openChat;
        dao_sqlite = new DAO_sqlite(context);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterSearchViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search_recent, null));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSearchViewHolder holder, @SuppressLint("RecyclerView") int position) {
        user user = users.get(position);
        if (!user.isStatus()) {
            holder.imgStatus.setVisibility(View.GONE);
        } else {
            holder.imgStatus.setVisibility(View.VISIBLE);

        }
        int k = 0;

        for (user s : new DAO_sqlite(context).getUser()) {
            if (s.getUID().equals(user.getUID())) {
                k = 1;
                break;
            }
        }
        if (k == 1) {
            holder.status.setText("Đã kết nối");
        } else {
            holder.status.setText("Chưa kết nối");

        }
        holder.txtName.setText(user.getName());

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                new DAO_sqlite(context).deleteSearchRecent(user.getUID());
                users.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
        try {
            byte b[] = dao_sqlite.getImg(user.getUID());
            if (b != null) {
                holder.img.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));

            } else {
                notifyDataSetChanged();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public static class AdapterSearchViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView img;
        ImageView imgStatus;
        TextView txtName, status;
        RelativeLayout itemverti;
        Button cancel;

        public AdapterSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            img = itemView.findViewById(R.id.img);
            txtName = itemView.findViewById(R.id.txtName);
            cancel = itemView.findViewById(R.id.cancel);
            itemverti = itemView.findViewById(R.id.itemverti);
        }
    }
}
