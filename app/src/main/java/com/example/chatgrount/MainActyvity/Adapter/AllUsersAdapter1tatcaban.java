package com.example.chatgrount.MainActyvity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatgrount.MainActyvity.ActiVity.ThongTinNGuoiDungActivity;
import com.example.chatgrount.MainActyvity.Model.AllUsers1loimoi;
import com.example.chatgrount.MainActyvity.Model.AllUsers2tatcaban;
import com.example.chatgrount.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllUsersAdapter1tatcaban extends RecyclerView.Adapter<AllUsersAdapter1tatcaban.ViewHoder> {

    private ArrayList<AllUsers2tatcaban> arrayList;
    private Context context;




    public AllUsersAdapter1tatcaban(ArrayList<AllUsers2tatcaban> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;


    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_userlayout,null);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHoder holder, int position) {
         final AllUsers2tatcaban user = arrayList.get(position);

        if(user.getImage().equals("null")){
            holder.imageView.setImageResource(R.drawable.icon_dangnhap);
        }else{
                 // Picasso.with(context).load(user.getImage()).into(holder.imageView);
                 Picasso.with(context).load(user.getImage()).networkPolicy(NetworkPolicy.OFFLINE).
                         placeholder(R.drawable.icon_dangnhap).into(holder.imageView, new Callback() {
                     @Override
                     public void onSuccess() {

                     }

                     @Override
                     public void onError() {
                         Picasso.with(context).load(user.getImage()).into(holder.imageView);

                     }
                 });
}




        holder.textViewTen.setText(user.getName());
        holder.textViewtinnhan.setText(user.getStatus());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTen,textViewtinnhan;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagedonguser);
            textViewTen = itemView.findViewById(R.id.textViewTendonguser);
            textViewtinnhan = itemView.findViewById(R.id.textViewtinNhandonguser);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ThongTinNGuoiDungActivity.class);
                    intent.putExtra("key",arrayList.get(getPosition()).getKey()+"");
                    intent.putExtra("name",arrayList.get(getPosition()).getName());
                    intent.putExtra("status",arrayList.get(getPosition()).getStatus());
                    intent.putExtra("image",arrayList.get(getPosition()).getImage());
                    context.startActivity(intent);
                }
            });
        }
    }



}
