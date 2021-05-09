package com.team.sonemo.data;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.team.sonemo.Model.UserModel;
import com.team.sonemo.activity.access.RegActivity;
import com.team.sonemo.activity.home.ProfileActivity;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class FirebaseHelper implements IDataHelper {
    private static FirebaseHelper instanse;
    private FirebaseAuth mAuth;
    private StorageReference storageRef;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    public static FirebaseHelper getInstance() {
        if (instanse == null) {
            instanse = new FirebaseHelper();
        }
        return instanse;
    }

    @Override
    public void createUser(Bitmap bitmap, UserModel usermodel, RegActivity activity) {
        if (bitmap == null) {
            createUser(usermodel,activity);
        } else {
            uploadImage(bitmap, usermodel,activity);
        }
    }

    private void createUser(UserModel usermodel, RegActivity activity) {
        mAuth.createUserWithEmailAndPassword(usermodel.getEmail(), usermodel.getPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String uid = mAuth.getCurrentUser().getUid();
                HashMap<Object, String> userInfo = new HashMap<>();
                userInfo.put("email", usermodel.getEmail());
                userInfo.put("password", usermodel.getPassword());
                userInfo.put("age", usermodel.getAge());
                userInfo.put("country", usermodel.getCountry());
                userInfo.put("image", usermodel.getImage());
                userInfo.put("cover", "");
                userInfo.put("username", usermodel.getUsername());
                userInfo.put("uid", uid);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Users");
                reference.child(uid).setValue(userInfo);
                activity.startActivity(new Intent(activity, ProfileActivity.class));
            }
        });
    }

    @Override
    public StorageReference getReference(String url) {
        return firebaseStorage.getReferenceFromUrl(url);
    }

    private void uploadImage(Bitmap bitmap, UserModel usermodel, RegActivity activity) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        storageRef = firebaseStorage.getReference().child("Posts/" + usermodel.getUsername() + ".jpg");

        final UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return storageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downUri = task.getResult();
                usermodel.setImage(storageRef.toString());
                createUser(usermodel,activity);
                Log.d("Final URL", "onComplete: Url: " + downUri.toString());
            }
        })).addOnFailureListener(e -> {
            e.printStackTrace();
        });
    }
}
