package com.team.sonemo.activity.access;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.team.sonemo.Model.UserModel;
import com.team.sonemo.R;
import com.team.sonemo.data.FirebaseHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class RegActivity extends AppCompatActivity {
    private Button btnRegReg;
    private EditText etRegCountry, etRegAge, etRegEmail, etRegPassword, etRegUsername;

    private Button btnAddImage;
    private ImageView ivRegAvatar;
    private Bitmap avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        btnRegReg = findViewById(R.id.btnRegReg);
        etRegUsername = findViewById(R.id.etRegUsername);
        etRegCountry = findViewById(R.id.etRegCountry);
        etRegAge = findViewById(R.id.etRegAge);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegEmail = findViewById(R.id.etRegEmail);
        btnAddImage = findViewById(R.id.btnAddImage);
        ivRegAvatar = findViewById(R.id.ivRegAvatar);

        btnAddImage.setOnClickListener(v -> {
            openGallery();
        });

        btnRegReg.setOnClickListener(v -> {
            String email = etRegEmail.getText().toString().trim();
            String password = etRegPassword.getText().toString().trim();
            String age = etRegAge.getText().toString();
            String country = etRegCountry.getText().toString();
            String username = etRegUsername.getText().toString();


            if (email.isEmpty()) {
                etRegEmail.setError("Email is required");
                return;
            }

            if (password.isEmpty()) {
                etRegPassword.setError("Password is required");
                return;
            }

            if (password.length() < 6) {
                etRegPassword.setError("Short Password");
                return;
            }

            UserModel userModel = new UserModel(username,email,password,age,country);

            FirebaseHelper.getInstance().createUser(avatar,userModel,this);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {

            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                avatar = BitmapFactory.decodeStream(imageStream);

                ivRegAvatar.setImageBitmap(avatar);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

}

