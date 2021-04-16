package com.team.sonemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private ImageButton btnHome;
    private ImageButton btnCinemas;
    private ImageButton btnMessages;
    private ImageButton btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//
        btnHome = findViewById(R.id.btnHome);
        btnCinemas = findViewById(R.id.btnCinemas);
        btnMessages = findViewById(R.id.btnMessages);


        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        btnMessages.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MessagesActivity.class);
            startActivity(intent);
        });

        btnCinemas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CinemasActivity.class);
            startActivity(intent);
        });

    }
}