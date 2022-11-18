package com.example.messagefake.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagefake.R;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.example.messagefake.openChat;
import com.example.messagefake.user;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_Horizontal extends RecyclerView.Adapter<Adapter_Horizontal.AdapterHoriViewHolder> {

    private List<user> users;
    private com.example.messagefake.openChat openChat;
    private Context context;
DAO_sqlite dao_sqlite;
    public Adapter_Horizontal( openChat openChat, Context context) {
        dao_sqlite=new DAO_sqlite(context);
        this.openChat = openChat;
        this.context = context;
    }
public void setData(List<user> users){
    this.users = users;

    notifyDataSetChanged();

}
    @NonNull
    @Override
    public AdapterHoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterHoriViewHolder(LayoutInflater.from(context).inflate(R.layout.recyview_hozi, null));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHoriViewHolder holder, int position) {

        user user = users.get(position);
        if (!user.isStatus()) {
            holder.imgStatus.setVisibility(View.GONE);
        } else {
            holder.imgStatus.setVisibility(View.VISIBLE);

        }
        try {
            byte b[] = dao_sqlite.getImg(user.getUID());
            if (b != null) {
                holder.avatar_horizontal.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
            } else {
                Picasso.get().load(user.getAvata()).into(holder.avatar_horizontal);
            }
        } catch (Exception e) {}
        if (user.getUID().equals(FirebaseAuth.getInstance().getUid())) {
            user.setName("Tôi");
        }
        holder.name_horizontal.setText(user.getName());

        holder.mLinearLayout.setOnClickListener(view -> openChat.openChat(user));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class AdapterHoriViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatar_horizontal ;
        private TextView name_horizontal;
        private LinearLayout mLinearLayout;
        CardView imgStatus;

        public AdapterHoriViewHolder(@NonNull View itemView) {
            super(itemView);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            avatar_horizontal = itemView.findViewById(R.id.img_avatar_horizontal);
            name_horizontal = itemView.findViewById(R.id.name_horizontal);
            mLinearLayout = itemView.findViewById(R.id.mLinearLayout);

        }
    }

    public void removeItem(user i) {
        users.remove(i);
        notifyDataSetChanged();
    }

}


