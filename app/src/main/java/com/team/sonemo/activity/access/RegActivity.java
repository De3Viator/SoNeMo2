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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.team.sonemo.Model.UserModel;
import com.team.sonemo.R;
import com.team.sonemo.data.FirebaseHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class RegActivity extends AppCompatActivity {
    private ImageButton btnRegReg;
    private EditText etRegCountry, etRegAge, etRegEmail, etRegPassword, etRegUsername, edtPasswordConfrimReg;

    private Button btnAddImage;
    private ImageView ivRegAvatar;
    private Bitmap avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        btnRegReg = findViewById(R.id.imgbtnCreateAcc);
        etRegUsername = findViewById(R.id.edtUserNameReg);
        etRegCountry = findViewById(R.id.edtCountryReg);
        etRegAge = findViewById(R.id.edtAgeReg);
        edtPasswordConfrimReg = findViewById(R.id.edtPasswordConfrimReg);
        etRegPassword = findViewById(R.id.edtPasswordReg);
        etRegEmail = findViewById(R.id.edtEmailReg);
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
            String confirmPassword = edtPasswordConfrimReg.getText().toString();


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

            if (!confirmPassword.contains(password)) {
                edtPasswordConfrimReg.setError("Password is not valid");
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

