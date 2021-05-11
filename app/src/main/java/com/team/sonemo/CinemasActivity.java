package com.team.sonemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
//import com.team.sonemo.adapters.RelisListAdapter;

import java.util.ArrayList;
import java.util.Map;

import static com.team.sonemo.FilmActivity.ITEM_TYPE;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.team.sonemo.Adapter.RelisListAdapter;
import com.team.sonemo.Model.RelisList;
import com.team.sonemo.activity.home.ProfileActivity;


import java.util.ArrayList;

import static com.team.sonemo.FilmActivity.ITEM_TYPE;

public class CinemasActivity extends AppCompatActivity {
    private ImageButton btnHome;
    private ImageButton btnCinemas;
    private ImageButton btnMessages;
    private ImageButton btnProfile;
    private SQLiteDatabase sqLiteDatabase;
    public static String TAG = "cinema_activity";



    private RecyclerView rvRelis;

    private RelisListAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RelisListAdapter.OnItemTypeClickListener listener = item -> {
        Intent intent= new Intent(this,FilmActivity.class).putExtra(ITEM_TYPE, item);
        startActivity(intent);
        intent= new Intent(this,FilmActivity.class).putExtra(ITEM_TYPE, item);
        startActivity(intent);
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinemas);
        btnProfile = findViewById(R.id.btnProfile);
        btnHome = findViewById(R.id.btnHome);
        btnMessages = findViewById(R.id.btnMessages);
        rvRelis = findViewById(R.id.rvRelis);
        rvRelis.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RelisListAdapter(listener);

        rvRelis.setAdapter(adapter);

        readData();

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        btnMessages.setOnClickListener(v -> {
            Intent intent = new Intent(this, MessagesActivity.class);
            startActivity(intent);
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });


    }

   private void readData() {

        ArrayList<RelisList> tmp= new ArrayList<>();


    db.collection("relisList")
           .get()
        .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + " => " + document.getData());
                    RelisList model= new RelisList();
                    model.setFilmData(document.getString("relis_date"));
                    model.setFilmName(document.getString("film_name"));
                    model.setPosterId(document.getString("poster"));
                    model.setFilmDescription(document.getString("description"));
                    model.setTrailerURL(document.getString("trailerURL"));
                    model.setCast(document.getString("cast"));

                    tmp.add(model);
                }
                adapter.setList(tmp);

            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
   }
}