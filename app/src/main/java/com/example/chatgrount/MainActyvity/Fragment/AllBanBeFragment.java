package com.example.chatgrount.MainActyvity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatgrount.MainActyvity.Adapter.AllUsersAdapter1tatcaban;
import com.example.chatgrount.MainActyvity.Model.AllUsers1loimoi;
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

public class AllBanBeFragment extends FriendsFragment {
    private RecyclerView recyclerView;
    private AllUsersAdapter1tatcaban adapter;
    private ArrayList<AllUsers2tatcaban> arrayList;
    private DatabaseReference databaseReference,mDatabaseReference;
    private FirebaseAuth mAth;
    private View view;
    private TextView textView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tat_ca_ban_be,container,false);

        textView = view.findViewById(R.id.textviewtatcabanbefgf);
        textView.setVisibility(View.VISIBLE);
         khoiTao();
        if(mAth != null) {
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    AllUsers2tatcaban allUsers = dataSnapshot.getValue(AllUsers2tatcaban.class);
                    arrayList.add(allUsers);
                    adapter.notifyDataSetChanged();
                    textView.setVisibility(View.GONE);
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


        return view;
    }

    private void khoiTao() {

        recyclerView = view.findViewById(R.id.recyclervieưtatcabanbe);
        arrayList = new ArrayList<>();
        adapter = new AllUsersAdapter1tatcaban(arrayList,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        mAth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Fiends").child(mAth.getCurrentUser().getUid());






    }
}
