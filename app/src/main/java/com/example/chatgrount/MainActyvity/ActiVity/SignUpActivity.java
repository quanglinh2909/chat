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
import android.widget.Toast;

import com.example.chatgrount.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private EditText ussName,email,password;
    private FirebaseAuth mAuth;
    private Button buttonSignUp;
    private ProgressDialog progressDialog;
    private DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        anhXa();
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ussName.getText().toString().trim();
                String email1 = email.getText().toString().trim();
                String pas = password.getText().toString().trim();
                if(name.length() == 0 || email1.length() == 0|| pas.length() == 0){
                    Toast.makeText(SignUpActivity.this, "Vui lòng điền đẩy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    if(pas.length() < 8){
                        Toast.makeText(SignUpActivity.this, "Mật khẩu phải dài hơn 8 ký tự", Toast.LENGTH_SHORT).show();

                    }else {
                        if(!TextUtils.isEmpty(name)||!TextUtils.isEmpty(email1)||!TextUtils.isEmpty(pas)){
                            progressDialog.setTitle("Đăng ký người dùng");
                            progressDialog.setMessage("Vui lòng chờ trong khi chúng tôi tạo tài khoản của bạn!");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            SignUp(name,email1,pas);
                        }
                    }

                }


            }
        });


    }

    private void SignUp(final String name, String email1, String pas) {
        mAuth.createUserWithEmailAndPassword(email1, pas)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser urser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = urser.getUid();
                            mdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            HashMap<String,String> userMap = new HashMap<>();
                            userMap.put("name",name);
                            userMap.put("status","tôi là "+name+" rất vui được làm quen với bạn");
                            userMap.put("image","null");
                            userMap.put("key",mAuth.getCurrentUser().getUid());

                            mdatabase.setValue(userMap);
                            progressDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();


                        } else {
                            progressDialog.hide();
                            Toast.makeText(SignUpActivity.this, "Email sai", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private void anhXa() {
        progressDialog = new ProgressDialog(this);
        ussName = findViewById(R.id.editTextTextPersonName);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        buttonSignUp = findViewById(R.id.buttonsignup);
    }
}