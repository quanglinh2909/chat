package com.example.chatgrount.MainActyvity.ActiVity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatgrount.MainActyvity.Model.GetTimeAgo;
import com.example.chatgrount.MainActyvity.Model.Massages;
import com.example.chatgrount.MainActyvity.Adapter.MessageAdapter;
import com.example.chatgrount.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatsActivity extends AppCompatActivity {
    private String mchatUser,mUserName;
    private DatabaseReference mRootref;
    private TextView name,onli;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    private ImageButton mChatSenBtn;
    private EditText mChatMessageView;
    private RecyclerView mMessageList;
    private ArrayList<Massages> arrayList = new ArrayList<>();
    private MessageAdapter adapter;
    private static  final  int TOTAL_ITEMS_TO_LOAD =10;
    private int mCurrentPage = 1;
    private SwipeRefreshLayout mRefreshLayout;
    private int itemPos = 0;
    private String mLastKey ="";
    private String mPrevKey ="";
    private int count = 10;
    private int sum = 1;
    private ImageButton chonanh;
    private static  final  int GALLERY_PICK = 1234;
    private StorageReference mStorageRef;
    private ImageView imageViewicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

         anhXa();


        mRootref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mCurrentUserId = mAuth.getCurrentUser().getUid();

        mUserName = getIntent().getStringExtra("name");

        mchatUser = getIntent().getStringExtra("data");

        final String ima = getIntent().getStringExtra("image");
        if(!ima.equals("null")){
            //Picasso.with(this).load(ima).into(imageViewicon);
            Picasso.with(this).load(ima).networkPolicy(NetworkPolicy.OFFLINE).
                    placeholder(R.drawable.icon_dangnhap).into(imageViewicon, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ChatsActivity.this).load(ima).into(imageViewicon);

                }
            });
        }

        name.setText(mUserName);
        mRootref.child("Users").child(mchatUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String online = dataSnapshot.child("online").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                if(online.equals("true")){

                    onli.setText("online");

                }else{

                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    long lastTime = Long.parseLong(online);
                    
                    String lastSeenTime =getTimeAgo.GetTimeAgo(lastTime,getApplicationContext());
                    onli.setText(lastSeenTime);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       mRootref.child("chat").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(!dataSnapshot.hasChild(mchatUser)){
                   Map chatAddMap = new HashMap();
                   chatAddMap.put("seen",false);
                   chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                   Map chatUserMap = new HashMap();
                   chatUserMap.put("Chat/"+mCurrentUserId+"/"+mchatUser,chatAddMap);
                   chatUserMap.put("Chat/"+mchatUser+"/"+mCurrentUserId,chatAddMap);



                   mRootref.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                       @Override
                       public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                           if(databaseError != null){

                           }

                       }
                   });

               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

       loadMessge();

       mChatSenBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               senMessage();




       }
       });


         mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 mCurrentPage++;
                 itemPos = 0;
                 arrayList.clear();

                     count = count*sum;
                     sum++;
                     loadMoreMessge();




             }
         });


    }


    private void loadMoreMessge() {
        final DatabaseReference messageRef  =   mRootref.child("messages").child(mCurrentUserId).child(mchatUser);
        Query messageQuery =  messageRef.orderByKey().limitToLast(count);


        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                Massages massages = dataSnapshot.getValue(Massages.class);
                String messageKey = dataSnapshot.getKey();
                if(!mPrevKey.equals(messageKey)){
                    arrayList.add(itemPos++,massages);

                }else{
                    mPrevKey = messageKey;

                }

                if(itemPos == 1){

                    mLastKey = messageKey;
                }
//                if(count > arrayList.size()){
//                    count =0;
//                }
                adapter.notifyDataSetChanged();

                mRefreshLayout.setRefreshing(false);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadMessge() {

        DatabaseReference messageRef  =   mRootref.child("messages").child(mCurrentUserId).child(mchatUser);
        Query messageQuery =  messageRef.limitToLast(mCurrentPage*TOTAL_ITEMS_TO_LOAD);
       messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Massages massages = dataSnapshot.getValue(Massages.class);

                itemPos++;

                if(itemPos == 1){
                    String messageKey = dataSnapshot.getKey();
                    mLastKey = messageKey;
                    mPrevKey = messageKey;
                }

                arrayList.add(massages);

                adapter.notifyDataSetChanged();

                mMessageList.scrollToPosition(arrayList.size()-1);
                mRefreshLayout.setRefreshing(false);



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void senMessage() {

        final String message  = mChatMessageView.getText().toString();
        if(!TextUtils.isEmpty(message)){
            String  current_user_ref = "messages/"+mCurrentUserId+"/"+mchatUser;
            String chat_user_ref = "messages/"+mchatUser+"/"+mCurrentUserId;
           final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            DatabaseReference user_message_push = mRootref.child("messages").child(mCurrentUserId).child(mchatUser).push();
            final DatabaseReference databaseReferencec = FirebaseDatabase.getInstance().getReference().child("Fiends").child(firebaseAuth.getCurrentUser().getUid()).child(mchatUser);

            databaseReferencec.child("tinnhancuoi").setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        final DatabaseReference databaseReferencec = FirebaseDatabase.getInstance().getReference().child("Fiends").child(firebaseAuth.getCurrentUser().getUid()).child(mchatUser);

                        databaseReferencec.child("keynuoigui").setValue(firebaseAuth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    final DatabaseReference databaseReferencect = FirebaseDatabase.getInstance().getReference().child("Fiends").child(mchatUser).child(firebaseAuth.getCurrentUser().getUid());
                                   databaseReferencect.child("tinnhancuoi").setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if(task.isSuccessful()){
                                               databaseReferencect.child("keynuoigui").setValue(firebaseAuth.getCurrentUser().getUid());
                                           }

                                       }
                                   });
                                }
                            }
                        });

                    }
                }
            });

            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message",message);
            messageMap.put("seend",false);
            messageMap.put("type","text");
            messageMap.put("Time",ServerValue.TIMESTAMP);
            messageMap.put("from",mCurrentUserId);



            final Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref +"/"+ push_id,messageMap);
            messageUserMap.put(chat_user_ref+"/"+push_id,messageMap);

            mChatMessageView.setText("");

            mRootref.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {



                    if(databaseError != null){

                    }

                }
            });
        }

    }

    private void anhXa() {
        imageViewicon = findViewById(R.id.imagedongchat);
        chonanh = findViewById(R.id.buttonimagechat);
        onli = findViewById(R.id.texdongchattrentime);
        name = findViewById(R.id.textdongchattrenten);
         mChatSenBtn = findViewById(R.id.buttonGuichat);
        mChatMessageView = findViewById(R.id.editTexChat);
        mRefreshLayout = findViewById(R.id.SwipeRefreshLayoutchat);
        mMessageList = findViewById(R.id.recyclerViewChat);
        adapter = new MessageAdapter(getApplicationContext(),arrayList);
        mMessageList.setHasFixedSize(true);
        mMessageList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mMessageList.setAdapter(adapter);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        chonanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"SELECT IMAGE"),GALLERY_PICK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null){
            Uri imageUri = data.getData();

            final  String current_user_ref = "messages/"+mCurrentUserId+"/"+mchatUser;
            final String chat_user_ref = "messages/"+mchatUser+"/"+mCurrentUserId;

            DatabaseReference user_message_push = mRootref.child("messages")
                    .child(mCurrentUserId).child(mchatUser).push();

            final String push_id = user_message_push.getKey();

            final StorageReference filepath  = mStorageRef.child("message_images").child(push_id+".jpg");

            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful()){
                      filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                          @Override
                          public void onSuccess(Uri uri) {
                              Map messageMap = new HashMap();

                              messageMap.put("message",uri.toString());
                              messageMap.put("seen",false);
                              messageMap.put("type","image");
                              messageMap.put("time",ServerValue.TIMESTAMP);
                              messageMap.put("from",mCurrentUserId);

                              Map messageUserMap = new HashMap();
                              messageUserMap.put(current_user_ref+"/"+push_id,messageMap);
                              messageUserMap.put(chat_user_ref+"/"+push_id,messageMap);

                              mChatMessageView.setText("");
                              mRootref.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                                  @Override
                                  public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                  }
                              });
                          }
                      });





                    }



                }
            });



        }

    }
}