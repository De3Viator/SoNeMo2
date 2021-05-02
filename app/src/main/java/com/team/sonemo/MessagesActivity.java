package com.team.sonemo;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.sonemo.Fragments.ChatsFragment;
import com.team.sonemo.Fragments.UsersFragment;
import com.team.sonemo.Model.Users;
import com.team.sonemo.access.UserAccessActivity;
import com.team.sonemo.access.UserRegistrationActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class  MessagesActivity extends AppCompatActivity {
     ImageButton btnHome;
     ImageButton btnCinemas;
     ImageButton btnMessages;
     ImageButton btnProfile;
     FirebaseUser firebaseUser;
     DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btnProfile = findViewById(R.id.btnProfile);
        btnHome = findViewById(R.id.btnHome);
        btnCinemas = findViewById(R.id.btnCinemas);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager view_item = findViewById(R.id.view_pager);


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





        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
        viewPagerAdapter.addFragment(new UsersFragment(),  "Users");

        view_item.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(view_item);



    }

    //class ViewPagerAdapter

     static class ViewPagerAdapter extends FragmentPagerAdapter{
        private final ArrayList<Fragment> fragments;
        private final ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

         @NonNull
         @Override
         public Fragment getItem(int position) {
             return fragments.get(position);
         }

         @Override
         public int getCount() {
             return fragments.size();
         }

         public void addFragment (Fragment fragment, String title ){
            fragments.add(fragment);
            titles.add(title);
         }

         @Nullable
         @Override
         public CharSequence getPageTitle(int position) {
             return titles.get(position);
         }


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
        CheckStatus("offline");
    }
    // Logout


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MessagesActivity.this, UserAccessActivity.class));
            finish();

            return true;
        }

        if (item.getItemId() == R.id.item_create_group) {
            startActivity(new Intent(MessagesActivity.this, CreateGroup.class));

            return true;
        }
        return false;
    }
}











