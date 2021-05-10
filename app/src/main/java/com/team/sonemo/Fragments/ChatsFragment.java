package com.team.sonemo.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.team.sonemo.Adapter.UserAdapter;
import com.team.sonemo.Model.Chat;
import com.team.sonemo.Model.ChatList;
import com.team.sonemo.Model.UserModel;
import com.team.sonemo.Model.Users;
import com.team.sonemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private UserAdapter userAdapter;
    private List<UserModel> mUsers;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private List<ChatList> usersList;

    RecyclerView recyclerView;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chats
                ,container
                ,false);

        recyclerView = view.findViewById(R.id.recycler_view_chats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();


        reference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usersList.clear();

                //Loop for all users
                for (DataSnapshot snapshot  : dataSnapshot.getChildren()){

                    ChatList chatList = snapshot.getValue(ChatList.class);
                    usersList.add(chatList);

                    chatlist();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

    }

    private void chatlist() {

        //Getting all chats
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUsers.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    UserModel user = snapshot.getValue(UserModel.class);

                    for (ChatList chatList : usersList){

                        if(user.getUid() != null && user.getUid().equals(chatList.getId())){
                            mUsers.add(user);
                        }

                    }
                }

                userAdapter = new UserAdapter(getContext(), mUsers, true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}










