package com.example.chatgrount.MainActyvity.ActiVity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatgrount.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Random;

public class SettingActivity extends AppCompatActivity {
    private DatabaseReference mUserDatabase;
    private FirebaseAuth mCurrenUser;
    private TextView ten, trangthai;
    private Button buttontangthai, buttonImage;
    private final int GALLERY_PICK = 123;
    private StorageReference mStorageRef;
    private FirebaseStorage firebaseStorage;
    private ProgressDialog progressDialog;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        anhXa();
        sukien();

        mCurrenUser = FirebaseAuth.getInstance();
        String curent_uid = mCurrenUser.getCurrentUser().getUid();


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(curent_uid);

        mUserDatabase.keepSynced(true);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();


                ten.setText(name);
                trangthai.setText(status);
                if (image.equals("null")) {
                    imageView.setImageResource(R.drawable.icon_dangnhap);
                } else {
                    //Picasso.with(getApplicationContext()).load(image).into(imageView);
                    Picasso.with(getApplicationContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE).
                            placeholder(R.drawable.icon_dangnhap).into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(getApplicationContext()).load(image).into(imageView);

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sukien() {
        buttontangthai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
                intent.putExtra("data", trangthai.getText().toString());
                startActivity(intent);
            }
        });
        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "SELECT_IMAGE"), GALLERY_PICK);

                /*
                // start picker to get image for cropping and then use the image in cropping activity
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingActivity.this);

                 */
            }
        });
    }

    private void anhXa() {
        imageView = findViewById(R.id.imagesetting);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        buttontangthai = findViewById(R.id.buttontrangthaa);
        buttonImage = findViewById(R.id.buttonchonanh);
        ten = findViewById(R.id.texsettingTen);
        trangthai = findViewById(R.id.textTrangthai);
        firebaseStorage = FirebaseStorage.getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).setAspectRatio(1, 1)
                    .start(SettingActivity.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                progressDialog = new ProgressDialog(SettingActivity.this);
                progressDialog.setTitle("Đang tải ảnh lên");
                progressDialog.setMessage("Vui lòng chờ trong khi chúng tôi tải ảnh lên");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                final String current_user_id = mCurrenUser.getUid();

                Uri resultUri = result.getUri();

                final StorageReference storageReference = mStorageRef.child("Image").child(current_user_id + ".jpg");
                storageReference.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {


                                    mUserDatabase.child("image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                            Toast.makeText(SettingActivity.this, "lưu thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                        } else {
                            Toast.makeText(SettingActivity.this, "lỗi!!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}