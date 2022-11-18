package com.example.messagefake.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagefake.R;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.example.messagefake.user;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterVertiFriend extends RecyclerView.Adapter<AdapterVertiFriend.AdapterVertiViewHolder> {
    private List<user> users;
    private openProfile openProfile;
     DAO_sqlite dao_sqlite;

    public AdapterVertiFriend(  openProfile openChat ) {
         this.openProfile = openChat;
      }
    public void setData(List<user> users ) {
        this.users = users;
       notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterVertiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        dao_sqlite = new DAO_sqlite(parent.getContext());
        return new AdapterVertiViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterVertiViewHolder holder, @SuppressLint("RecyclerView") int position) {

        user user = users.get(position);
        Picasso.get().load(user.getAvata()).into(holder.imgAvatar);
        holder.txtTen.setText(user.getName());

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openProfile.openProfile(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(users.size()<6){
            return users.size();

        }
        return 6;
    }

    public static class AdapterVertiViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imgAvatar;
        private TextView txtTen;
LinearLayout mLinearLayout;

        public AdapterVertiViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            txtTen = itemView.findViewById(R.id.txtTen);
            mLinearLayout = itemView.findViewById(R.id.mLinearLayout);

        }
    }


}
