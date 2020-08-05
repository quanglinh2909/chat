package com.example.chatgrount.MainActyvity.ActiVity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatgrount.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {
    private TextInputLayout editText;
    private DatabaseReference mdata;
    private FirebaseUser mFirebaseUser;
    private Button button;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        editText = findViewById(R.id.tetsatus);
        button = findViewById(R.id.buttonCapnhatSatus);
        final Intent intent = getIntent();
        String name = intent.getStringExtra("data");
        editText.getEditText().setText(name);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mFirebaseUser.getUid();

        mdata = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(StatusActivity.this);
                progressDialog.setTitle("Saving Changes");
                progressDialog.setMessage("vui lòng chờ trong khi chúng tôi đang cập nhật");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                String status = editText.getEditText().getText().toString().trim();
                mdata.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Intent intent1 = new Intent(getApplicationContext(),SettingActivity.class);
                            startActivity(intent1);
                            finish();
                        }else{
                            Toast.makeText(StatusActivity.this, "Lỗi!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });

    }
}