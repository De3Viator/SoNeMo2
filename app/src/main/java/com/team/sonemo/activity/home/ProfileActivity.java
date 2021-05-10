package com.team.sonemo.activity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.team.sonemo.Adapter.PostAdapter;
import com.team.sonemo.CinemasActivity;
import com.team.sonemo.HomeActivity;
import com.team.sonemo.MessagesActivity;
import com.team.sonemo.data.DBHelper;
import com.team.sonemo.Model.PostModel;
import com.team.sonemo.R;
import com.team.sonemo.activity.access.MainActivity;
import com.team.sonemo.activity.posts.AddPostActivity;
import com.team.sonemo.activity.posts.PostDetailActivity;
import com.team.sonemo.data.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private RecyclerView rvPost;
    private List<PostModel> postList;
    private PostAdapter postAdapter;
    private ImageView ivAvatar, ivCover;
    private ImageButton btnCinemas, btnMessages, btnHome;
    private TextView txtUsername, txtCountry, txtAge;
    private Button btnUpdProfile, btnUsers, btnChatUserChat, btnAddPost;
    private DatabaseReference likeRef = DBHelper.getInstance().getChildReference("likeRef");
    private DatabaseReference postRef = DBHelper.getInstance().getChildReference("Posts");

    public ProfileActivity() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ivAvatar = findViewById(R.id.ivUserAvatar);
        txtUsername = findViewById(R.id.txtRVUserName);
        btnCinemas = findViewById(R.id.btnCinemas);
        btnMessages = findViewById(R.id.btnMessages);
        btnHome = findViewById(R.id.btnHome);
        btnAddPost = findViewById(R.id.btnAddPost);
        rvPost = findViewById(R.id.rvPost);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        rvPost.setLayoutManager(layoutManager);

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        btnMessages.setOnClickListener(v -> {
            Intent intent = new Intent(this, MessagesActivity.class);
            startActivity(intent);
        });

        btnCinemas.setOnClickListener(v -> {
            Intent intent = new Intent(this, CinemasActivity.class);
            startActivity(intent);
        });

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("Users");

        postList = new ArrayList<>();
        loadPost();

        btnAddPost.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, AddPostActivity.class)));


        btnUsers.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, PostDetailActivity.class));

        });


        Query query = dbRef.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String username = "" + ds.child("username").getValue();
                    String country = "" + ds.child("country").getValue();
                    String image = "" + ds.child("image").getValue();
                    String age = "" + ds.child("age").getValue();

                    txtUsername.setText(username);
                    txtCountry.setText(country);
                    txtAge.setText(age);

                   if (image != null) FirebaseHelper.getInstance()
                            .getReference(image)
                            .getDownloadUrl()
                            .addOnSuccessListener(uri ->
                                    Picasso.get().load(uri).placeholder(R.drawable.ic_deafult_img).into(ivAvatar))
                            .addOnFailureListener(e -> Log.e("Firebase storage:",e.getLocalizedMessage()));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

        } else {
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        }
    }

    private void loadPost() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Posts");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    PostModel postModel = ds.getValue(PostModel.class);
                    postList.add(postModel);
                }
                postAdapter = new PostAdapter(postList, (event, model) -> {
                    if (event.equals(PostAdapter.LIKE)) {
                        PostModel tmpModel = model;
                        ArrayList<String> pValueLikes = tmpModel.getpLikes();
                        String postIde = model.getPostId();
                        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        if (isLiked(id, tmpModel.getpLikes())) {
                            pValueLikes.remove(id);
                        } else {
                            pValueLikes.add(id);
                        }
                        postRef.child(postIde).child("likeRef").setValue(pValueLikes);
                    }
                    Toast.makeText(getBaseContext(), event, Toast.LENGTH_SHORT).show();
                });
                rvPost.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private boolean isLiked(String id, ArrayList<String> likeRef) {
        for (String likeId : likeRef) {
            if (likeId.equals(id)) return true;
        }
        return false;

    }
}

