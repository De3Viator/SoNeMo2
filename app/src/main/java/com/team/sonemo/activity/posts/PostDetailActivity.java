package com.team.sonemo.activity.posts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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
import com.google.firebase.database.ValueEventListener;
import com.team.sonemo.Adapter.CommentAdapter;
import com.team.sonemo.DBHelper;
import com.team.sonemo.Model.CommentModel;
import com.team.sonemo.Model.PostModel;
import com.team.sonemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private FirebaseUser user;
    private RecyclerView rvComment;
    private List<CommentModel> commentList;
    private CommentAdapter commentAdapter;
    private PostModel model;
    private TextView txtCommentDescription, txtCommentUsername;
    private DatabaseReference userRef;
    private EditText etComment;
    String cUsername, uid;
    private ImageView ivCommentPictureUser;
    private ImageButton ibtnSend;
    private DatabaseReference postRef = DBHelper.getInstance().getChildReference("Posts");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        txtCommentDescription = findViewById(R.id.txtCommentDesciption);
        txtCommentUsername = findViewById(R.id.txtCommentUserName);
        ivCommentPictureUser = findViewById(R.id.ivCommentPictureUser);
        etComment = findViewById(R.id.etComment);
        ibtnSend = findViewById(R.id.ibtnSend);
        rvComment = findViewById(R.id.rvComment);
        rvComment.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter((type, model) -> {
            etComment.setFocusable(true);
            etComment.requestFocus();
            this.model = model;
        });

        rvComment.setAdapter(commentAdapter);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();
        db = FirebaseDatabase.getInstance();
        userRef = db.getReference("Users");
        postRef = db.getReference("Posts");
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                ArrayList<PostModel> tmpList = new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    PostModel post = postSnapshot.getValue(PostModel.class);
                    tmpList.add(post);
                }
                commentAdapter.setCommentList(tmpList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ibtnSend.setOnClickListener(v -> postComment(model));

    }

    private void postComment(PostModel model) {

        String comment = etComment.getText().toString().trim();
        String timeStape = String.valueOf(System.currentTimeMillis());

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Posts").child("comRef");

        HashMap<String, Object> commentMap = new HashMap<>();
        commentMap.put("cId", timeStape);
        commentMap.put("timestamp", timeStape);
        commentMap.put("comment", comment);
        commentMap.put("username", cUsername);
        commentMap.put("uid", uid);

        dbRef.child(timeStape).setValue(commentMap).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Comment was added", Toast.LENGTH_SHORT).show();
            etComment.setText("");
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Comment was not loaded" + e.getMessage(), Toast.LENGTH_SHORT).show();

        });
    }

}
