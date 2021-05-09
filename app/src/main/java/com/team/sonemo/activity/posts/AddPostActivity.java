package com.team.sonemo.activity.posts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.team.sonemo.R;


import java.util.HashMap;

public class AddPostActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private EditText etTitle,etDescription;
    private ImageView ivPost;
    private Button btnUpload;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_CAMERA_PICK_CODE = 300;
    private static final int IMAGE_GALLERY_PICK_CODE = 400;
    private String[] cameraPermissions;
    private String[] storagePermissions;
    Uri imageUri = null;
    private String name,email,uid,img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        ivPost = findViewById(R.id.ivPost);
        btnUpload = findViewById(R.id.btnUpload);
        mAuth = FirebaseAuth.getInstance();

        dbRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query =  dbRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    name = "" + ds.child("name").getValue();
                    email = "" + ds.child("email").getValue();
                    img = ""+ ds.child("image").getValue();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        ivPost.setOnClickListener(v->{
            imagePicDialgo();
        });



        btnUpload.setOnClickListener(v->{
            String title = etTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            if (title.isEmpty()){
                Toast.makeText(AddPostActivity.this,"Enter title",Toast.LENGTH_SHORT);
                return;
            }

            if(description.isEmpty()){
                Toast.makeText(AddPostActivity.this,"Enter description",Toast.LENGTH_SHORT);
                return;
            }

            if (imageUri==null){
                dataUpload(title,description,"noImage");
            } else{
                dataUpload(title,description,String.valueOf(imageUri));
            }
        });

    }

    private void imagePicDialgo() {
        String img [] = {"Camera","Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(img, (dialog, which) -> {
            if(which == 0){
                if (!checkCamPerm()){
                    reqCamPerm();
                }
                else{
                    pickCamera();

                }
            }

            else if(which == 1){
                if(!checkStorPerm()){
                    reqStorPerm();
                } else {
                    pickGallery();
                }
            }

        });
        builder.create().show();
    }

    private void dataUpload(String title, String description,String uri) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Posts/" + "post_" + timeStamp;

        if(!uri.equals("noImage")){
            StorageReference stRef  = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            stRef.putFile(Uri.parse(uri)).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                String downloadUri = uriTask.getResult().toString();

                if(uriTask.isSuccessful()){
                    HashMap<Object , String> uriMap = new HashMap<>();
                    uriMap.put("userId",uid);
                    uriMap.put("userImage",img);
                    uriMap.put("postId",timeStamp);
                    uriMap.put("postTitle",title);
                    uriMap.put("postDescription",description);
                    uriMap.put("postImage",downloadUri);
                    uriMap.put("postTime",timeStamp);

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                    ref.child(timeStamp).setValue(uriMap)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(AddPostActivity.this,"Post Added",Toast.LENGTH_SHORT).show();
                                etTitle.setText("");
                                etDescription.setText("");
                                ivPost.setImageURI(null);
                                imageUri = null;
                            })
                            .addOnFailureListener(e -> Toast.makeText(AddPostActivity.this,"" + e.getMessage(),Toast.LENGTH_SHORT));
                }


            }).addOnFailureListener(e -> Toast.makeText(AddPostActivity.this,"" + e.getMessage(),Toast.LENGTH_SHORT).show());

        } else {HashMap<Object , String> uriMap = new HashMap<>();
        uriMap.put("userId",uid);
        uriMap.put("userImage",img);
        uriMap.put("postId",timeStamp);
        uriMap.put("postTitle",title);
        uriMap.put("postDescription",description);
        uriMap.put("postImage","noImage");
        uriMap.put("postTime",timeStamp);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.child(timeStamp).setValue(uriMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddPostActivity.this,"Post Added",Toast.LENGTH_SHORT).show();
                    etTitle.setText("");
                    etDescription.setText("");
                    ivPost.setImageURI(null);
                    imageUri = null;
                })
                .addOnFailureListener(e -> Toast.makeText(AddPostActivity.this,"" + e.getMessage(),Toast.LENGTH_SHORT));

    }


    }

    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_GALLERY_PICK_CODE);
    }

    private void pickCamera() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp Title");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Descr");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent, IMAGE_CAMERA_PICK_CODE);
    }

    private boolean checkStorPerm(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    public void reqStorPerm(){
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
    }

    private boolean checkCamPerm(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==(PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) ==(PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    public void reqCamPerm(){
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean acceptCamera = grantResults [0] == PackageManager.PERMISSION_GRANTED;
                    boolean acceptStorage = grantResults [1] == PackageManager.PERMISSION_GRANTED;
                    if(acceptCamera && acceptStorage){
                        pickCamera();
                    } else{
                        Toast.makeText(AddPostActivity.this,"Permissions not granted",Toast.LENGTH_SHORT);
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean acceptStorage = grantResults [0] == PackageManager.PERMISSION_GRANTED;
                    if(acceptStorage){
                        pickGallery();
                    } else{
                        Toast.makeText(AddPostActivity.this,"Permissions not granted",Toast.LENGTH_SHORT);
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK ){
            if(requestCode == IMAGE_CAMERA_PICK_CODE){
                ivPost.setImageURI(imageUri);
            }

            if (requestCode == IMAGE_GALLERY_PICK_CODE){
                imageUri = data.getData();
                ivPost.setImageURI(imageUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}