package com.team.sonemo.activity.access;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.sonemo.R;
import com.team.sonemo.activity.home.ProfileActivity;


import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    GoogleSignInClient mGoogleSignInClient;
    private Button btnLogin,btnRegister;
    private TextView txtRecover;
    private EditText etEmail,etPassword;
    private FirebaseAuth mAuth;
    private SignInButton btnGoogleSign;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRegister = findViewById(R.id.btnLogRegister);
        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etLogEmail);
        etPassword = findViewById(R.id.etLogPassword);
        txtRecover = findViewById(R.id.txtRecover);
        btnGoogleSign = findViewById(R.id.btnGoogleSign);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               // .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        mAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(v->{
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Log is succesfull",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));

                } else {
                    Toast.makeText(MainActivity.this,"Log is Failed",Toast.LENGTH_SHORT).show();
                }

            });

            btnGoogleSign.setOnClickListener( v1 -> {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            });
        });

        txtRecover.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RecoverActivity.class)));
        btnRegister.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegActivity.class)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this,""+ e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    FirebaseUser user = mAuth.getCurrentUser();



                    if (task.isSuccessful()) {
                        String uid = user.getUid();
                        String email = user.getEmail();
                        if(task.getResult().getAdditionalUserInfo().isNewUser()){

                            HashMap<Object, String> userInfo = new HashMap<>();
                            userInfo.put("email",email);
                            userInfo.put("password","");
                            userInfo.put("age","");
                            userInfo.put("country","");
                            userInfo.put("image","");
                            userInfo.put("username","");
                            userInfo.put("cover","");
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference =database.getReference("Users");
                            reference.child(uid).setValue(userInfo);
                        }
                        // Sign in success, update UI with the signed-in user's information
                        mAuth.getCurrentUser();
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this,"Failed ",Toast.LENGTH_SHORT);
                    }
                }).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}