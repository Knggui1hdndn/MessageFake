package com.example.messagefake.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagefake.I.EditDetail;
import com.example.messagefake.R;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.example.messagefake.detail;

import java.util.List;

public class AdapterVertiEditInfo extends RecyclerView.Adapter<AdapterVertiEditInfo.AdapterVertiViewHolder> {
    private List<detail> users;
    private openProfile openProfile;
     DAO_sqlite dao_sqlite;

    public AdapterVertiEditInfo(openProfile openChat ) {
         this.openProfile = openChat;
      }
    public void setData(List<detail> users ) {
        this.users = users;
       notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterVertiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        dao_sqlite = new DAO_sqlite(parent.getContext());
        return new AdapterVertiViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false));
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull AdapterVertiViewHolder holder, @SuppressLint("RecyclerView") int position) {

        detail user = users.get(position);
        Log.d("onBindViewHolder", "onBindViewHolder: "+user.getTxt());
        if(user.getTxt().endsWith("null")){
            String s[]=user.getTxt().split("null");
            holder.btnInfo.setText(s[0]);
            holder.btnInfo.setTextColor(androidx.browser.R.color.browser_actions_bg_grey);
        }else{
            holder.btnInfo.setText(user.getTxt());

        }
        holder.btnInfo.setCompoundDrawablesWithIntrinsicBounds(user.getImg(), 0, 0, 0);
        holder.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
v.getContext().startActivity(new Intent(v.getContext(), EditDetail.class));            }
        });
    }

    @Override
    public int getItemCount() {

            return users.size();


    }

    public static class AdapterVertiViewHolder extends RecyclerView.ViewHolder {
        private Button btnInfo;
        public AdapterVertiViewHolder(@NonNull View itemView) {
            super(itemView);
            btnInfo = itemView.findViewById(R.id.btnInfo);

        }
    }


}
