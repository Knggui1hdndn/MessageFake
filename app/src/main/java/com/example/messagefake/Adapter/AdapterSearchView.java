package com.example.messagefake.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagefake.R;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.example.messagefake.user;

import java.util.ArrayList;
import java.util.Locale;

public class AdapterSearchView extends RecyclerView.Adapter<AdapterSearchView.AdapterSearchViewHolder> implements Filterable {
    private ArrayList<user> users;
    private ArrayList<user> usersS;
    private com.example.messagefake.openChat openChat;
    private Context context;
DAO_sqlite dao_sqlite;
    @SuppressLint("NotifyDataSetChanged")
    public AdapterSearchView(ArrayList<user> users, com.example.messagefake.openChat openChat, Context context) {
        this.users = users;
        this.usersS = users;
        this.context = context;
        this.openChat = openChat;
        dao_sqlite=new DAO_sqlite(context) ;notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterSearchViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_search_view, null));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSearchViewHolder holder, int position) {
        user user = users.get(position);
        if(!user.isStatus()){
            holder.status.setVisibility(View.GONE);
        }else{
            holder.status.setVisibility(View.VISIBLE);

        }
        holder.edt_searchView.setText(user.getName());
        holder.layout_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChat.openChat(user);
            }
        });

        try {
            byte b[] = dao_sqlite.getImg(user.getUID());
            if (b != null) {
                holder.img_searchView.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));

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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                if (charSequence.toString().isEmpty()) {
                    users = usersS;
                } else {
                    ArrayList<user> users1 = new ArrayList<>();
                    for (user user : usersS) {
                        if (user.getName().toLowerCase(Locale.ROOT).contains(charSequence)) {
                            users1.add(user);
                        }
                    }
                    users = users1;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = users;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                users= (ArrayList<user>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class AdapterSearchViewHolder extends RecyclerView.ViewHolder {
        ImageView img_searchView  ;
        TextView edt_searchView;
        RelativeLayout layout_adapter;
        CardView status;
        public AdapterSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.imgStatus);
            img_searchView = itemView.findViewById(R.id.img_searchView);
            layout_adapter = itemView.findViewById(R.id.layout_adapter);
            edt_searchView = itemView.findViewById(R.id.edt_searchView);
        }
    }
}
