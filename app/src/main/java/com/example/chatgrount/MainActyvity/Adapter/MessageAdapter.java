package com.example.chatgrount.MainActyvity.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatgrount.MainActyvity.Model.Massages;
import com.example.chatgrount.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHoder> {
    private Context context;
    private ArrayList<Massages> arrayList;
    private FirebaseAuth mAuth;

    public MessageAdapter(Context context, ArrayList<Massages> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_chat,parent,false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        mAuth = FirebaseAuth.getInstance();

        Massages massages = arrayList.get(position);

        if(arrayList.size() != 0){
            if(massages.getFrom().equals(mAuth.getCurrentUser().getUid())){

                holder.textViewminh.setText(massages.getMessage());
                holder.ban.setVisibility(View.GONE);
                holder.minh.setVisibility(View.VISIBLE);

            }else {

              holder.textViewBan.setText(massages.getMessage());
                holder.ban.setVisibility(View.VISIBLE);
                holder.minh.setVisibility(View.GONE);


            }
        }



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class  ViewHoder extends RecyclerView.ViewHolder {
        TextView textViewminh,textViewBan;
        RelativeLayout minh,ban;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            textViewminh = itemView.findViewById(R.id.textviewdongchatcuaminh);
            textViewBan = itemView.findViewById(R.id.textviewdongchatcuaban);
            minh = itemView.findViewById(R.id.relativelayoutdongchatcuaminh);
            ban = itemView.findViewById(R.id.relatilayouytdongchatcuaban);
        }
    }
}
