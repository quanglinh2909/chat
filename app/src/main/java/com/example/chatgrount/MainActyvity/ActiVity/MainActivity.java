package com.example.chatgrount.MainActyvity.ActiVity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.chatgrount.MainActyvity.Adapter.AllUsersAdapter1tatcaban;
import com.example.chatgrount.MainActyvity.Adapter.AllUsersAdapterloimoi;
import com.example.chatgrount.MainActyvity.Adapter.SectionsPagerAdapter;
import com.example.chatgrount.MainActyvity.Model.AllUsers1loimoi;
import com.example.chatgrount.MainActyvity.Model.AllUsers2tatcaban;
import com.example.chatgrount.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private FirebaseAuth mAuth;
    public static ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private DatabaseReference mUserref;
    private Toolbar toolbar;
    private ImageView imageView;
    private DatabaseReference databaseReference;
    //tim kiem
    private DatabaseReference mUserdatabase;
    private ArrayList<AllUsers1loimoi> arrayList;
    private AllUsersAdapterloimoi adapter;
    public static RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        anhXa();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");



        khoiTao();
        TimKiem();


    }



    private void khoiTao() {
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);



    }

    private void anhXa() {
        recyclerView = findViewById(R.id.recylerViewmainactivity);
         tabLayout = findViewById(R.id.taplayoutmain);
        viewPager = findViewById(R.id.viewpagermain);
        toolbar = findViewById(R.id.to);
        imageView = findViewById(R.id.imagrmain);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);


    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            senToStar();
        }else{
            mUserref = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
            mUserref.child("online").setValue("true");
            Handler handler = new Handler();
           handler.postDelayed(new Runnable() {
               @Override
               public void run() {
                   databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                           if (dataSnapshot != null){
                               if(!dataSnapshot.child("image").getValue().toString().equals("null")){

                                   // Picasso.with(MainActivity.this).load(dataSnapshot.child("image").getValue().toString()).into(imageView);

                                   Picasso.with(MainActivity.this).load(dataSnapshot.child("image").getValue().toString()).networkPolicy(NetworkPolicy.OFFLINE).
                                           placeholder(R.drawable.icon_dangnhap).into(imageView, new Callback() {
                                       @Override
                                       public void onSuccess() {

                                       }

                                       @Override
                                       public void onError() {
                                           Picasso.with(MainActivity.this).load(dataSnapshot.child("image").getValue().toString()).into(imageView);

                                       }
                                   });
                               }else{
                                   imageView.setImageResource(R.drawable.icon_dangnhap);
                               }


                           }

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
               }
           },20000);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currenUse = mAuth.getCurrentUser();
        if(currenUse != null){
            mUserref.child("online").setValue(ServerValue.TIMESTAMP);

        }


    }

    private void senToStar() {
        Intent intent = new Intent(MainActivity.this, LoginActiVity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        SearchView searchView = (androidx.appcompat.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerView.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                adapter.filter(newText);
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_dangxuat:
                FirebaseAuth.getInstance().signOut();
                mUserref.child("online").setValue(ServerValue.TIMESTAMP);
                senToStar();
                break;
            case R.id.menu_caidat:
                Intent intent = new Intent(getApplicationContext(),SettingActivity.class);

                startActivity(intent);
                break;
            case R.id.menu_allUser:
                Intent intent2 = new Intent(getApplicationContext(),UsersActivity.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void TimKiem() {
        arrayList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mUserdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
         recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mUserdatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mUserdatabase.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AllUsers1loimoi users = dataSnapshot.getValue(AllUsers1loimoi.class);

                        arrayList.add(users);
                        adapter = new AllUsersAdapterloimoi(arrayList,MainActivity.this);
                        recyclerView.setAdapter(adapter);


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
    }
}