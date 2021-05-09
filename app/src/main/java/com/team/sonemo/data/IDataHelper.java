package com.team.sonemo.data;

import android.graphics.Bitmap;

import com.google.firebase.storage.StorageReference;
import com.team.sonemo.Model.UserModel;
import com.team.sonemo.activity.access.RegActivity;

public interface IDataHelper {
    StorageReference getReference(String url);
    void createUser(Bitmap bitmap, UserModel usermodel, RegActivity activity);
}
