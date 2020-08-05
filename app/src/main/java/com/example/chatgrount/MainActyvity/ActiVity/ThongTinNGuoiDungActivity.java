package com.example.chatgrount.MainActyvity.ActiVity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatgrount.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ThongTinNGuoiDungActivity extends AppCompatActivity {
    private Button buttonDaNang,buttonXoaYeuCau;
    private TextView textViewTen,textViewTrangThai;
    private ImageView imageViewHinhNGuoiDUng;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private String ten,trangThai;
    private String key;
    private String image;
    private String xac_nhan_da_la_ban_chua;
    private DatabaseReference mDataChapNhanHayKhong;
    private String currentData= DateFormat.getTimeInstance().format(new Date());
    private String daLaBan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_n_guoi_dung);

        anhXa();
        nhanDuLieu();
        KhoiTao();
        suKien();




        buttonDaNang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(xac_nhan_da_la_ban_chua.equals("chưa")){
                    mDatabaseReference.child("YeuCauKetBan").child(mAuth.getCurrentUser().getUid()).child(key).child("là ai").setValue("người gửi").
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        mDatabaseReference.child("YeuCauKetBan").child(key).child(mAuth.getCurrentUser().getUid()).child("là ai").setValue("người nhận");
                                     buttonDaNang.setText("hủy yêu cầu kết bạn");
                                     xac_nhan_da_la_ban_chua = "chờ đồng ý";
                                    }
                                }
                            });
                }
                if(xac_nhan_da_la_ban_chua.equals("phê duyệt hay không")){

                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                    databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final Map<String,String> map1= new HashMap<>();
                            map1.put("image",dataSnapshot.child("image").getValue().toString());
                            map1.put("name",dataSnapshot.child("name").getValue().toString());
                            map1.put("key",dataSnapshot.child("key").getValue().toString());
                            map1.put("online",dataSnapshot.child("online").getValue().toString());
                            map1.put("time",currentData);
                            map1.put("tinnhancuoi"," ");
                            map1.put("keynuoigui"," ");
                            map1.put("status",dataSnapshot.child("status").getValue().toString());
                            DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
                            databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final Map<String,String> map2= new HashMap<>();
                                    map2.put("image",dataSnapshot.child("image").getValue().toString());
                                    map2.put("name",dataSnapshot.child("name").getValue().toString());
                                    map2.put("key",dataSnapshot.child("key").getValue().toString());
                                    map2.put("online",dataSnapshot.child("online").getValue().toString());
                                    map2.put("time",currentData);
                                    map2.put("tinnhancuoi"," ");
                                    map2.put("keynuoigui"," ");
                                    map2.put("status",dataSnapshot.child("status").getValue().toString());
                                    mDatabaseReference.child("YeuCauKetBan").child(mAuth.getCurrentUser().getUid()).child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){

                                                mDatabaseReference.child("YeuCauKetBan").child(key).child(mAuth.getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        mDatabaseReference.child("Fiends").child(mAuth.getCurrentUser().getUid()).child(key).setValue(map2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){

                                                                    mDatabaseReference.child("Fiends").child(key).child(mAuth.getCurrentUser().getUid()).setValue(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){

                                                                                buttonDaNang.setText("Xóa khỏi bạn bè");
                                                                                xac_nhan_da_la_ban_chua = "đã là bạn";

                                                                                buttonXoaYeuCau.setVisibility(View.GONE);

                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });

                                                    }
                                                });

                                            }


                                        }
                                    });


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });







                }
                if(xac_nhan_da_la_ban_chua.equals("chờ đồng ý")){
                    mDatabaseReference.child("YeuCauKetBan").child(mAuth.getCurrentUser().getUid()).child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                mDatabaseReference.child("YeuCauKetBan").child(key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                xac_nhan_da_la_ban_chua = "chưa";
                                buttonDaNang.setText("Gửi yêu cầu kết bạn");
                                buttonXoaYeuCau.setVisibility(View.GONE);
                            }

                        }
                    });


                }
                if(xac_nhan_da_la_ban_chua.equals("đã là bạn")){
                    mDatabaseReference.child("Fiends").child(mAuth.getCurrentUser().getUid()).child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                mDatabaseReference.child("Fiends").child(key).child(mAuth.getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            mDatabaseReference.child("Chat").child(mAuth.getCurrentUser().getUid()).child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    mDatabaseReference.child("Chat").child(key).child(mAuth.getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            mDatabaseReference.child("TinNhanCuoi").child(mAuth.getCurrentUser().getUid()).child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    mDatabaseReference.child("TinNhanCuoi").child(key).child(mAuth.getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            mDatabaseReference.child("messages").child(mAuth.getCurrentUser().getUid()).child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {

                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        mDatabaseReference.child("messages").child(key).child(mAuth.getCurrentUser().getUid()).removeValue();

                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                                xac_nhan_da_la_ban_chua = "chưa";
                                buttonDaNang.setText("Gửi yêu cầu kết bạn");
                                buttonXoaYeuCau.setVisibility(View.GONE);
                            }

                        }
                    });
                }


            }
        });


        buttonXoaYeuCau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabaseReference.child("YeuCauKetBan").child(mAuth.getCurrentUser().getUid()).child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            mDatabaseReference.child("YeuCauKetBan").child(key).child(mAuth.getCurrentUser().getUid()).removeValue();
                            xac_nhan_da_la_ban_chua = "chưa";
                            buttonDaNang.setText("Gửi yêu cầu kết bạn");
                            buttonXoaYeuCau.setVisibility(View.GONE);
                        }

                    }
                });



            }
        });



    }



    private void nhanDuLieu() {
        ten = getIntent().getStringExtra("name");
        trangThai = getIntent().getStringExtra("status");
        key = getIntent().getStringExtra("key");
        image = getIntent().getStringExtra("image");

        textViewTen.setText(ten);
        textViewTrangThai.setText(trangThai);
        if(image.equals("null")){
            imageViewHinhNGuoiDUng.setImageResource(R.drawable.icon_dangnhap);
        }else{
            Picasso.with(this).load(image).networkPolicy(NetworkPolicy.OFFLINE).
                    placeholder(R.drawable.icon_dangnhap).into(imageViewHinhNGuoiDUng, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(getApplicationContext()).load(image).into(imageViewHinhNGuoiDUng);

                }
            });
        }}



    private void KhoiTao() {
        xac_nhan_da_la_ban_chua = "chưa";
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mDataChapNhanHayKhong = FirebaseDatabase.getInstance().getReference().child("YeuCauKetBan");

    }

    private void anhXa() {
        buttonXoaYeuCau = findViewById(R.id.buttonxoayeucau);
        buttonXoaYeuCau.setVisibility(View.GONE);
        buttonDaNang = findViewById(R.id.buttonketbannguoidung);
        textViewTen = findViewById(R.id.texttennuoidung);
        textViewTrangThai = findViewById(R.id.texttrangthainguoidung);
        imageViewHinhNGuoiDUng = findViewById(R.id.imagehinhthongtinnguoidung);



    }
    private void suKien() {

        mDataChapNhanHayKhong.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(key)){

                    String laAi = dataSnapshot.child(key).child("là ai").getValue().toString();

                    if(laAi.equals("người gửi")){
                        buttonDaNang.setText("hủy yêu cầu kết bạn");
                        xac_nhan_da_la_ban_chua = "chờ đồng ý";
                        buttonXoaYeuCau.setVisibility(View.GONE);
                    }
                    if(laAi.equals("người nhận")){
                        buttonDaNang.setText("Chấp Nhận kết bạn");
                        buttonXoaYeuCau.setVisibility(View.VISIBLE);
                        xac_nhan_da_la_ban_chua = "phê duyệt hay không";
                        buttonXoaYeuCau.setVisibility(View.VISIBLE);


                    }



                }else{
                    mDatabaseReference.child("Fiends").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.hasChild(key)){
                                xac_nhan_da_la_ban_chua = "đã là bạn";
                                buttonDaNang.setText("Xóa khỏi bạn bè");
                            }else{
                                xac_nhan_da_la_ban_chua = "chưa";
                                buttonDaNang.setText("Gửi lời mời kết bạn");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}