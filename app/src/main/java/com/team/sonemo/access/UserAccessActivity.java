package com.team.sonemo.access;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.team.sonemo.MainActivity;
import com.team.sonemo.R;

public class UserAccessActivity extends AppCompatActivity {
    private EditText etEmail,etPassword;
    private FirebaseAuth mAuth;
    private ImageButton imgbtnSignIn;
    private TextView goToReg;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Checking for users
        if (firebaseUser != null){
            Intent i = new Intent(UserAccessActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_access);
        etEmail = findViewById(R.id.etEmail);// Получение поля ввода почты
        etPassword = findViewById(R.id.etPassword);// Получение поля ввода пароля
        imgbtnSignIn = findViewById(R.id.imgbtnSignIn);// Получение кнопки входа
        goToReg = findViewById(R.id.txtCreateAcc);// Получение кнопки перехода на экран регистрации


        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Checking for users
        if (firebaseUser != null){
            Intent i = new Intent(UserAccessActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }



        imgbtnSignIn.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                Toast.makeText(UserAccessActivity.this, "Please fill the Fields", Toast.LENGTH_SHORT).show();
            }else {

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(UserAccessActivity.this, "Log Successfully", Toast.LENGTH_SHORT);
                        Intent i = new Intent(UserAccessActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(UserAccessActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT);
                    }
                });
            }
        });// вход в аккаунт

        goToReg.setOnClickListener(v ->{
            Intent intent = new Intent(this, UserRegistrationActivity.class);
            startActivity(intent);
            finish();
        });// Переход на экран регистрации
    }
}