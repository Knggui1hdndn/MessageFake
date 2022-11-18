package com.example.messagefake.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagefake.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Adapter_Chat extends RecyclerView.Adapter<Adapter_Chat.Adapter_ChaViewHolde>{
    private List<String>strings;
    private final Context context;
    public Adapter_Chat(List<String>strings,Context context) {
        this.strings = strings;
        this.context = context;
    }
    public  void setData(List<String> strings){
        this.strings = strings;

    }
    @NonNull
    @Override
    public Adapter_ChaViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Adapter_ChaViewHolde(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_chat, null));
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull Adapter_ChaViewHolde holder, int position) {
        holder.txtGui.setVisibility(View.VISIBLE);
        holder.s.setVisibility(View.VISIBLE);
        holder.txtNhan.setVisibility(View.VISIBLE);
        holder.s1.setVisibility(View.VISIBLE);
        holder.s.setText( new SimpleDateFormat("hh:mm").format(new Date()));
try {

    if(strings.get(position).endsWith("dHiKQUqtRuqs") ){
        String clip[]=strings.get(position).split("dHiKQUqtRuqs");
        holder.txtNhan.setText(TextUtils.substring(clip[0],0,clip[0].length()-5 ));
        holder.txtGui.setVisibility(View.GONE);
        holder.s1.setText( TextUtils.substring(clip[0],clip[0].length()-5,clip[0].length() ) );
        holder.s.setVisibility(View.GONE);
    }else{
        String clip[]=strings.get(position).split("70sWUEEum");
        holder.txtGui.setText(TextUtils.substring(clip[0],0,clip[0].length()-5 ));
        holder.txtNhan.setVisibility(View.GONE);
        holder.s.setText( TextUtils.substring(clip[0],clip[0].length()-5,clip[0].length()  ));
        holder.s1.setVisibility(View.GONE);


    }
}catch (Exception e){
    holder.txtNhan.setVisibility(View.GONE);
    holder.txtGui.setVisibility(View.GONE);
    holder.s1.setVisibility(View.GONE);
    holder.s.setVisibility(View.GONE);
}
    }

    @Override
    public int getItemCount() {
        if(strings.size()>0){
            return strings.size();

        }
        return 0;
    }

    static class Adapter_ChaViewHolde extends RecyclerView.ViewHolder {
        private TextView txtGui;
        private TextView txtNhan,s,s1;

        public Adapter_ChaViewHolde(@NonNull View itemView) {
            super(itemView);
            txtGui = itemView.findViewById(R.id.txtGui);
            txtNhan = itemView.findViewById(R.id.txtNhan);
            s = itemView.findViewById(R.id.s);
            s1 = itemView.findViewById(R.id.s1);
        }
    }

}
