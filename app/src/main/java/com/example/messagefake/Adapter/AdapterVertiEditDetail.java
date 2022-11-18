package com.example.messagefake.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagefake.R;
import com.example.messagefake.dataFirebase.DAO_sqlite;

import java.util.List;

public class AdapterVertiEditDetail extends RecyclerView.Adapter<AdapterVertiEditDetail.AdapterVertiViewHolder> {
    private List<String> users;
    DAO_sqlite dao_sqlite;

    public AdapterVertiEditDetail(  ) {

    }

    public void setData(List<String> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterVertiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        dao_sqlite = new DAO_sqlite(parent.getContext());
        return new AdapterVertiViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_detail, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterVertiViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String user = users.get(position);
        holder.checkBox.setText(user);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.checkBox.setText(user);
                } else {
                    String s = holder.checkBox.getText().toString() + "\nSẽ không hiển thị trong phần giới thiệu\nnhưng vẫn sẽ công khai";
                    holder.checkBox.setText(spannableString(s));
                }
            }
        });


    }

    private SpannableString spannableString(String s) {
        SpannableString string = new SpannableString(s);
        string.setSpan(new AbsoluteSizeSpan(40), TextUtils.indexOf(s, '\n'), s.length(), 0);
        string.setSpan(new ForegroundColorSpan(Color.GRAY), TextUtils.indexOf(s, '\n'), s.length(), 0);
        return string;
    }

    @Override
    public int getItemCount() {
if(users.get(0).trim().length()==0){
    return 0 ;

}
        return users.size();
    }

    public static class AdapterVertiViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public AdapterVertiViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void removeItem(int i) {
        users.remove(i);
        notifyDataSetChanged();
    }

}
