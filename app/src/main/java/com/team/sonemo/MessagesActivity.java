package com.team.sonemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class MessagesActivity extends AppCompatActivity {
    private ImageButton btnHome;
    private ImageButton btnCinemas;
    private ImageButton btnMessages;
    private ImageButton btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        btnProfile = findViewById(R.id.btnProfile);
        btnHome = findViewById(R.id.btnHome);
        btnCinemas = findViewById(R.id.btnCinemas);

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        btnCinemas.setOnClickListener(v -> {
            Intent intent = new Intent(this, CinemasActivity.class);
            startActivity(intent);
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });


    }
}