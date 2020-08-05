package com.example.chatgrount.MainActyvity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.chatgrount.MainActyvity.ActiVity.ChatsActivity;
import com.example.chatgrount.MainActyvity.Model.User;
import com.example.chatgrount.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHoder> {

    private ArrayList<User> arrayList;
    private Context context;


    public FriendsAdapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_friends,null);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHoder holder, int position) {

        final User user = arrayList.get(position);
       Picasso.with(context).load(user.getIcon()).into(holder.iconHoatDong);
        if(user.getImage().toString().equals("null")){
            holder.imageView.setImageResource(R.drawable.icon_dangnhap);
        }else{
            //Picasso.with(context).load(user.getImage()).into(holder.imageView);
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
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser().getUid().equals(user.getNguoigui())){
            holder.textViewtinnhan.setText("Bạn: "+user.getTinnhan()+" "+user.getTime());
        }else{
            holder.textViewtinnhan.setText(user.getTinnhan()+" "+user.getTime());
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTen,textViewtinnhan;
        ImageView iconHoatDong;
        RelativeLayout relativeLayout;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagedongfriend);
            textViewTen = itemView.findViewById(R.id.textViewTendongfriend);
            textViewtinnhan = itemView.findViewById(R.id.textViewtinNhandongfriend);
            iconHoatDong= itemView.findViewById(R.id.iconhoatdong);
           relativeLayout = itemView.findViewById(R.id.dong_fiends);

           relativeLayout.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(context, ChatsActivity.class);
                   intent.putExtra("data",arrayList.get(getPosition()).getKey()+"");
                   intent.putExtra("name",arrayList.get(getPosition()).getName()+"");
                   intent.putExtra("image",arrayList.get(getPosition()).getImage());
                   context.startActivity(intent);
               }
           });

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, ChatsActivity.class);
//                    intent.putExtra("data",arrayList.get(getPosition()).getKey()+"");
//                    intent.putExtra("name",arrayList.get(getPosition()).getName()+"");
//                    context.startActivity(intent);
//                }
//            });
        }
    }
}
