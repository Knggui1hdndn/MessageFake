package com.example.messagefake.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagefake.R;
import com.example.messagefake.detail;

import java.util.List;

public class AdapterDetail extends RecyclerView.Adapter<AdapterDetail.AdapterVertiViewHolder> {
    private List<detail> users;


    public AdapterDetail(List<detail> users) {
        this.users = users;

    }

    @NonNull
    @Override
    public AdapterVertiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new AdapterVertiViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterVertiViewHolder holder, @SuppressLint("RecyclerView") int position) {

        detail user = users.get(position);
        holder.button.setCompoundDrawablesWithIntrinsicBounds(user.getImg(), 0, 0, 0);
        if (user.getTxt() == null) {
            holder.button.setEnabled(false);

        } else {
            holder.button.setText(user.getTxt());

        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class AdapterVertiViewHolder extends RecyclerView.ViewHolder {
        Button button;

        public AdapterVertiViewHolder(@NonNull View itemView) {
            super(itemView);

            button = itemView.findViewById(R.id.btnInfo);
        }
    }


}
