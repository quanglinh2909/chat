package com.example.chatgrount.MainActyvity.ActiVity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatgrount.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    private DatabaseReference mUsersDatabase;
    private TextView ten,trangthai;
    private Button buttonloimoi;
    private ImageView imageView;
    private ProgressDialog progressDialog;
    private String curent_state;
    private DatabaseReference mFriendsDatabase;
    private FirebaseUser mCurrent_user;
    private  String user_id;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mNotificationDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user_id = getIntent().getStringExtra("data");

        anhxa();

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String display_name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image  = dataSnapshot.child("image").getValue().toString();
                
                curent_state = "not friend";
                
                progressDialog.setTitle("Loading User data");
                progressDialog.setMessage(" chờ chút chúng tôi đang tải user");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                ten.setText(display_name);
                trangthai.setText(status);
                if(!image.equals("null")){
                    Picasso.with(getApplicationContext()).load(image).into(imageView);

                }

                //-----------------------------------friend list reques feature----------------------------
                mFriendsDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(user_id)){
                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();
                            if(req_type.equals("received")){

                                curent_state = "req_received";
                                buttonloimoi.setText("chấp nhận kết bạn");



                            }else if(req_type.equals("sent")){

                                curent_state = "req_sent";
                                buttonloimoi.setText("Hủy kết bạn");



                            }
                            progressDialog.dismiss();
                        }else{
                            mFriendDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.hasChild(user_id)){

                                        curent_state = "friends";

                                        buttonloimoi.setText("Xóa khỏi bạn bè");


                                    }
                                    progressDialog.dismiss();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    progressDialog.dismiss();

                                }
                            });
                        }



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
        suKien();
    }

    private void suKien() {
        buttonloimoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonloimoi.setEnabled(false);

                //------------------chưa là bạn-----------------

                if(curent_state.equals("not friend")){
                    mFriendsDatabase.child(mCurrent_user.getUid()+"").child(user_id).
                            child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull final Task<Void> task) {
                            if(task.isSuccessful()){
                                mFriendsDatabase.child(user_id).child(mCurrent_user.getUid()).child("request_type").
                                        setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        HashMap<String, String> notificationData = new HashMap<>();
                                        notificationData.put("from",mCurrent_user.getUid());
                                        notificationData.put("type","request");


                                        mNotificationDatabase.child(user_id).push().setValue(notificationData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                curent_state = "req_sent";

                                                buttonloimoi.setText("Huỷ kết bạn");


                                            }
                                        });



                                        Toast.makeText(ProfileActivity.this, "yêu cầu gửi thành công", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(ProfileActivity.this, "Không thực hiện được vui lòn thử lại", Toast.LENGTH_SHORT).show();
                            }
                            buttonloimoi.setEnabled(true);
                        }
                    });
                    
                }
                //------------------đã  là bạn-----------------
                if(curent_state.equals("req_sent")){
                    mFriendsDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFriendsDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    curent_state = "not friend ";
                                    buttonloimoi.setEnabled(true);
                                    buttonloimoi.setText("kết bạn");


                                }
                            });

                        }
                    });
                }
                // -------------------------------------------------

                if(curent_state.equals("req_received")){

                    final String currentData= DateFormat.getTimeInstance().format(new Date());

                    mFriendDatabase.child(mCurrent_user.getUid()).child(user_id).setValue(currentData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFriendDatabase.child(user_id).child(mCurrent_user.getUid()).setValue(currentData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    mFriendsDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            mFriendsDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    curent_state = "friends";
                                                    buttonloimoi.setEnabled(true);
                                                    buttonloimoi.setText("Xóa khỏi bạn bè");


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




    }

    private void anhxa() {
        progressDialog = new ProgressDialog(ProfileActivity.this);
        ten = findViewById(R.id.textprofileten);
        trangthai = findViewById(R.id.textprofilestatus);
        buttonloimoi = findViewById(R.id.buttonketban);
        imageView = findViewById(R.id.imageprofile);
    }
}