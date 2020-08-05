package com.example.chatgrount.MainActyvity.ActiVity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatgrount.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActiVity extends AppCompatActivity {
    private Button buttonSignIn;
    private TextView buttonSign;
    private EditText email,password;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acti_vity);
        buttonSign = findViewById(R.id.buttonSignLogin);
        anhXa();
        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);

            }
        });
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emai1 = email.getText().toString().trim();
                String paswod = password.getText().toString().trim();
                if(emai1.length() ==0 || paswod.length() == 0){
                    Toast.makeText(LoginActiVity.this, "Vui lòng điền dầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    if(paswod.length()<8){
                        Toast.makeText(LoginActiVity.this, "Mật khẩu sai", Toast.LENGTH_SHORT).show();
                    }else {
                        if(!TextUtils.isEmpty(emai1)|| !TextUtils.isEmpty(paswod)){
                            progressDialog.setTitle("Đăng nhập");
                            progressDialog.setMessage("vui lòng chờ trong khi chúng tôi kiểm tra thông tin đăng nhập của bạn");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            loginUrese(emai1,paswod);
                        }
                    }
                }

            }
        });
    }

    private void loginUrese(String emai1, String paswod) {
        mAuth.signInWithEmailAndPassword(emai1, paswod)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();




                        } else {
                            progressDialog.hide();
                            Toast.makeText(LoginActiVity.this, "lỗi!!!", Toast.LENGTH_SHORT).show();

                        }

                    }
                });



    }

    private void anhXa() {
        mAuth = FirebaseAuth.getInstance();
        buttonSignIn = findViewById(R.id.buttonSignIn);
        email = findViewById(R.id.editTextTextPersonName2);
        password = findViewById(R.id.editTextTextPassword2);
        progressDialog = new ProgressDialog(this);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }
}