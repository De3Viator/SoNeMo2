package com.team.sonemo.data;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private final FirebaseAuth mAuth;
    private StorageReference storageRef;
    private final FirebaseStorage firebaseStorage;
    DatabaseReference reference;

    private FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
    }

    public static FirebaseHelper getInstance() {
        if (instanse == null) {
            instanse = new FirebaseHelper();
        }
        return instanse;
    }

    @Override
    public void createUser(Bitmap bitmap, UserModel usermodel, RegActivity activity) {
        if (bitmap != null) {
            uploadImage(bitmap, usermodel,activity);
        } else {
            createUser(usermodel,activity);
        }
    }

    private void createUser(UserModel usermodel, RegActivity activity) {
        mAuth.createUserWithEmailAndPassword(usermodel.getEmail(), usermodel.getPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                assert mAuth != null;
                String uid = mAuth.getCurrentUser().getUid();
                reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

                HashMap<Object, String> userInfo = new HashMap<>();
                userInfo.put("email", usermodel.getEmail());
                userInfo.put("password", usermodel.getPassword());
                userInfo.put("age", usermodel.getAge());
                userInfo.put("country", usermodel.getCountry());
                userInfo.put("image", usermodel.getImage());
                userInfo.put("cover", "");
                userInfo.put("username", usermodel.getUsername());
                userInfo.put("uid", uid);

                reference.setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            activity.startActivity(new Intent(activity, ProfileActivity.class));
                        }
                    }
                });

            }else {

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
        })).addOnFailureListener(Throwable::printStackTrace);
    }
}
