package com.team.sonemo.activity.access;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.team.sonemo.R;

public class RecoverActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnRecovery;
    private EditText etRecEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);
        mAuth = FirebaseAuth.getInstance();
        btnRecovery = findViewById(R.id.btnRecover);
        etRecEmail = findViewById(R.id.etRecEmail);
        btnRecovery.setOnClickListener( v->{
            String email = etRecEmail.getText().toString().trim();
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(RecoverActivity.this,"Letter is go",Toast.LENGTH_SHORT).show();
                }

            }).addOnFailureListener(e -> Toast.makeText(RecoverActivity.this,"" + e.getMessage(),Toast.LENGTH_SHORT).show());
        });
    }
}