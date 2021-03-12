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
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.team.sonemo.Fragments.ChatsFragment;
import com.team.sonemo.Fragments.UsersFragment;

import java.util.ArrayList;

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
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager view_item = findViewById(R.id.view_item);

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

     class ViewPagerAdapter extends FragmentPagerAdapter{
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

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






















}