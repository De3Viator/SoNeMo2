package com.team.sonemo.access;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.team.sonemo.MainActivity;
import com.team.sonemo.R;

public class UserAccessActivity extends AppCompatActivity {
    private EditText etEmail,etPassword;
    private FirebaseAuth mAuth;
    private ImageButton imgbtnSignIn;
    private TextView goToReg;

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



        imgbtnSignIn.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(UserAccessActivity.this,"Log Successfully",Toast.LENGTH_SHORT);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(UserAccessActivity.this,"Error "+task.getException().getMessage(),Toast.LENGTH_SHORT);
                }
            });
        });// вход в аккаунт

        goToReg.setOnClickListener(v ->{
            Intent intent = new Intent(this, UserRegistrationActivity.class);
            startActivity(intent);
        });// Переход на экран регистрации
    }
}