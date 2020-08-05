package com.example.chatgrount.MainActyvity.ActiVity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.chatgrount.MainActyvity.Adapter.AllUsersAdapter1tatcaban;
import com.example.chatgrount.MainActyvity.Model.AllUsers2tatcaban;
import com.example.chatgrount.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference mUserdatabase;
    private ArrayList<AllUsers2tatcaban> arrayList;
    private AllUsersAdapter1tatcaban adapter;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        anhXa();
        arrayList = new ArrayList<>();
        adapter = new AllUsersAdapter1tatcaban(arrayList,this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        mUserdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView.setAdapter(adapter);
       adapter.notifyDataSetChanged();
       mUserdatabase.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
              mUserdatabase.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      AllUsers2tatcaban users = dataSnapshot.getValue(AllUsers2tatcaban.class);

                          arrayList.add(users);
                          adapter.notifyDataSetChanged();


                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {

                  }
              });
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
       }) ;}



    private void anhXa() {
        recyclerView = findViewById(R.id.recyleuser);
    }


}