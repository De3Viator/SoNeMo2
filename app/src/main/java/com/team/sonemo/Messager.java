package com.team.sonemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.sonemo.Model.Chat;
import com.team.sonemo.Adapter.MessageAdapter;
import com.team.sonemo.Model.Users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Messager extends AppCompatActivity {

    TextView username;
    ImageView imageView;

    RecyclerView recyclerView;
    EditText text_send;
    ImageButton btn_send;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    String userid;

    ValueEventListener seenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messager);

        //Widgets
        imageView = findViewById(R.id.imageView_profile);
        username = findViewById(R.id.username_profile);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        //RecyclerView

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Toolbar

       /* Toolbar toolbar = findViewById(R.id.toolbar);
        getSupportActionBar(toolbar);
        getSupportActionBar().setTitle('');
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        intent = getIntent();
        userid = intent.getStringExtra("id");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                assert user != null;
                username.setText(user.getUsername());

                if (user.getImageURL() != null && user.getImageURL().equals("default")){
                    imageView.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Glide.with(Messager.this)
                            .load(user.getImageURL())
                            .into(imageView);
                }

                readMessages(firebaseUser.getUid(),userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_send.setOnClickListener(v -> {
            String message = text_send.getText().toString();
            if (!message.equals("")){
                sendMessage(firebaseUser.getUid(), userid, message);
            }else{
                Toast.makeText(Messager.this, "Please send a non empty message",Toast.LENGTH_SHORT).show();
            }

            text_send.setText("");

        });

        seenMessage(userid);

    }

    private void seenMessage(String userid){

        reference = FirebaseDatabase.getInstance().getReference("Chats");

        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chat chat = snapshot.getValue(Chat.class);

                    assert chat != null;
                    if ((chat.getReceiver() != null) && chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)){

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);

        reference.child("Chats").push().setValue(hashMap);

        //Adding User to Chat
        final DatabaseReference chatRef = FirebaseDatabase.getInstance()
                .getReference("ChatList")
                .child(firebaseUser.getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    private void readMessages(String myid, String userid, String imageurl){

        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getReceiver() != null && chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver() != null && chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){

                        mChat.add(chat);

                    }

                    messageAdapter = new MessageAdapter(Messager.this, mChat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void CheckStatus(String status){

        reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        CheckStatus("offline");
    }



}