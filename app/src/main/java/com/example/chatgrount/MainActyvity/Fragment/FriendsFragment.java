package com.example.chatgrount.MainActyvity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatgrount.MainActyvity.Adapter.AllUsersAdapterloimoi;
import com.example.chatgrount.MainActyvity.Adapter.AllUsersAdapter1tatcaban;
import com.example.chatgrount.MainActyvity.Adapter.FriendsAdapter;
import com.example.chatgrount.MainActyvity.Model.AllUsers1loimoi;
import com.example.chatgrount.MainActyvity.Model.AllUsers2tatcaban;
import com.example.chatgrount.MainActyvity.Model.User;
import com.example.chatgrount.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FriendsFragment extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserdatabase;
    private DatabaseReference mUserdatabase2;
    private String mCurrent_user_id;
    private View mMainView;
    private ArrayList<User> arrayList;
    private FriendsAdapter adapter;
    private String imageicon ="";


    //
    private RecyclerView recyclerViewGoiY;
    private RelativeLayout relativeLayoutGoiy;
    private ArrayList<AllUsers1loimoi> arrayListGoiy;
    private AllUsersAdapterloimoi adapterGoiY;
    private DatabaseReference mUserdatabase3;
    private String key;
    private String image;
    private String name;

    public FriendsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        mMainView = inflater.inflate(R.layout.freands_fragment, container, false);

        recyclerView = mMainView.findViewById(R.id.recyclerView_friends_frament);
        recyclerView.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();

       anhXa();
       //------------------------------------------------------------------------------------------------------//

        arrayListGoiy = new ArrayList<>();
        adapterGoiY = new AllUsersAdapterloimoi(arrayListGoiy,getContext());

        recyclerViewGoiY.setHasFixedSize(true);
        recyclerViewGoiY.setLayoutManager(new LinearLayoutManager(getContext()));
        mUserdatabase3 = FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerViewGoiY.setAdapter(adapterGoiY);

        mUserdatabase3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mUserdatabase3.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        AllUsers1loimoi users = dataSnapshot.getValue(AllUsers1loimoi.class);
                        if(!users.getKey().equals(mAuth.getCurrentUser().getUid())){
                            arrayListGoiy.add(users);
                            adapterGoiY.notifyDataSetChanged();
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
        }) ;

        //-------------------------------------------------------------------------------------------------------

        arrayList = new ArrayList<>();
        adapter = new FriendsAdapter(arrayList,getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUserdatabase = FirebaseDatabase.getInstance().getReference().child("Fiends").child(mCurrent_user_id);
        goiYKetBan();
        recyclerView.setAdapter(adapter);


        mUserdatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                relativeLayoutGoiy.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                name = dataSnapshot.child("name").getValue().toString();
                        image = dataSnapshot.child("image").getValue().toString();
                        String tinnhan = dataSnapshot.child("tinnhancuoi").getValue().toString();
                        String nguoigui = dataSnapshot.child("keynuoigui").getValue().toString();
                String time = dataSnapshot.child("time").getValue().toString();
                        if(dataSnapshot.hasChild("online")){
                            String on = dataSnapshot.child("online").getValue().toString();
                            if(on.equals("true")){

                                imageicon = "https://firebasestorage.googleapis.com/v0/b/logingooleapi-277500.appspot.com/o/Image%2Fonline_icon.png?alt=media&token=0371169c-4f5f-4301-a530-07d9fa865613";
                            }else{

                                imageicon = "https://firebasestorage.googleapis.com/v0/b/logingooleapi-277500.appspot.com/o/Image%2Ftrang_icon.png?alt=media&token=dacdef80-4da5-4a98-8eef-53548e17c1b6";
                            }

                        }
                              key= dataSnapshot.getKey();

                                User user = new User(name,image,imageicon,key,tinnhan,nguoigui,time);
                                arrayList.add(user);
                                adapter.notifyDataSetChanged();
                mUserdatabase2 = FirebaseDatabase.getInstance().getReference().child("Fiends");

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



        return mMainView;

    }

    private void goiYKetBan() {

    }

    private void anhXa() {
        recyclerViewGoiY = mMainView.findViewById(R.id.recyclerViewguiyketban);
        relativeLayoutGoiy = mMainView.findViewById(R.id.relatilayoutgoiyketban);
    }


}
