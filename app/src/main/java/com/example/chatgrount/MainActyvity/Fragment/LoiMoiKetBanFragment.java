package com.example.chatgrount.MainActyvity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatgrount.MainActyvity.Adapter.AllUsersAdapterloimoi;
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

public class LoiMoiKetBanFragment extends Fragment {
    private RecyclerView recyclerView;
    private AllUsersAdapterloimoi adapter;
    private ArrayList<AllUsers1loimoi> arrayList;
    private DatabaseReference databaseReference,mDatabaseReference;
    private FirebaseAuth mAth;
    private View view;
    private TextView textView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_loi_moi_ket_ban,container,false);
        khoiTao();
        textView = view.findViewById(R.id.textviewloimoi);
        textView.setVisibility(View.VISIBLE);

      if(mAth != null){
          databaseReference.addChildEventListener(new ChildEventListener() {
              @Override
              public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                 databaseReference.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         String laAi = dataSnapshot.child("là ai").getValue().toString();
                         if(laAi.equals("người nhận")){
                            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(dataSnapshot.getKey());
                            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    AllUsers1loimoi allUsers = dataSnapshot.getValue(AllUsers1loimoi.class);
                                    textView.setVisibility(View.GONE);
                                    arrayList.add(allUsers);
                                    adapter.notifyDataSetChanged();
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

        recyclerView = view.findViewById(R.id.recyclerviewloimoiketban);
        arrayList = new ArrayList<>();
        adapter = new AllUsersAdapterloimoi(arrayList,getContext());
        recyclerView.setLayoutManager(new  LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        mAth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("YeuCauKetBan").child(mAth.getCurrentUser().getUid());






    }
}
