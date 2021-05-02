package com.team.sonemo.access;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.team.sonemo.MainActivity;
import com.team.sonemo.R;

import java.util.HashMap;

public class UserRegistrationActivity extends AppCompatActivity {
    private EditText edtEmailReg,edtUserNameReg, edtPasswordReg, edtPasswordConfirmReg, edtAgeReg, edtCountryReg;
    private ImageButton imgbtnCreateAcc;
    private TextView GoToSignIn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userid;
    private static final String TAG = "TAG";
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        edtUserNameReg = findViewById(R.id.edtUserNameReg);
        edtEmailReg = findViewById(R.id.edtEmailReg);
        edtAgeReg = findViewById(R.id.edtAgeReg);
        edtCountryReg = findViewById(R.id.edtCountryReg);
        edtPasswordReg = findViewById(R.id.edtPasswordReg);
        edtPasswordConfirmReg = findViewById(R.id.edtPasswordConfrimReg);
        imgbtnCreateAcc = findViewById(R.id.imgbtnCreateAcc);
        GoToSignIn = findViewById(R.id.txtGoToSignIn);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        GoToSignIn.setOnClickListener(v ->{
            Intent intent = new Intent(this, UserAccessActivity.class);
            startActivity(intent);
        });

        imgbtnCreateAcc.setOnClickListener(v -> {

            String email = edtEmailReg.getText().toString().trim();
            String password = edtPasswordReg.getText().toString().trim();
            String age = edtAgeReg.getText().toString();
            String country = edtCountryReg.getText().toString();
            String confirmPassword = edtPasswordConfirmReg.getText().toString();
            String username = edtUserNameReg.getText().toString();


            if (email.isEmpty()) {
                edtEmailReg.setError("Email is required");
                return;
            }

            if (password.isEmpty()) {
                edtPasswordReg.setError("Password is required");
                return;
            }

            if (password.length() < 6) {
                edtPasswordReg.setError("Short Password");
                return;
            }

            if (!confirmPassword.contains(password)) {
                edtPasswordConfirmReg.setError("Password is not valid");
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

                    //HashMaps
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", username);
                    hashMap.put("age", age);
                    hashMap.put("country", country);
                    hashMap.put("imageURL", "default");
                    hashMap.put("status", "offline");


                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Intent i = new Intent(UserRegistrationActivity.this, MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
                }else {
                    Toast.makeText(UserRegistrationActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                }
            });
                });



        /*imgbtnCreateAcc.setOnClickListener(v -> {
                    String email = edtEmailReg.getText().toString().trim();
                    String password = edtPasswordReg.getText().toString().trim();
                    String age = edtAgeReg.getText().toString();
                    String country = edtCountryReg.getText().toString();
                    String confirmPassword = edtPasswordConfirmReg.getText().toString();
                    String username = edtUserNameReg.getText().toString();


                    if (email.isEmpty()) {
                        edtEmailReg.setError("Email is required");
                        return;
                    }

                    if (password.isEmpty()) {
                        edtPasswordReg.setError("Password is required");
                        return;
                    }

                    if (password.length() < 6) {
                        edtPasswordReg.setError("Short Password");
                        return;
                    }

                    if (!confirmPassword.contains(password)) {
                        edtPasswordConfirmReg.setError("Password is not valid");
                        return;
                    }
                }

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(UserRegistrationActivity.this, "Account created",Toast.LENGTH_SHORT);
                    userID = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("Users").document(userID);
                    Map<String,Object> user = new HashMap<>();
                    //Данные пользователя в профиль
                    user.put("id",userID);
                    user.put("username",username);
                    user.put("email",email);
                    user.put("password",password);
                    user.put("country",country);
                    user.put("age",age);
                    documentReference.set(user).addOnSuccessListener(aVoid -> {
                        Log.d(TAG,"Success: user profile created for " + userID);

                    }).addOnFailureListener(e -> {
                        Log.d(TAG, "Failure: " + e.toString());
                    });


                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                } else {
                    Toast.makeText(UserRegistrationActivity.this,"Error"+task.getException().getMessage(),Toast.LENGTH_SHORT);
                }

            });
        });

        GoToSignIn.setOnClickListener(v ->{
            Intent intent = new Intent(this, UserAccessActivity.class);
            startActivity(intent);
        });


          if (user.getImageUrl() != null && user.getImageURL().equals("default"))*/
    }

}