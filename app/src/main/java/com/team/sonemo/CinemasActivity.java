package com.team.sonemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

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
    private Button btnReadMore;
    private SQLiteDatabase sqLiteDatabase;
    public static String TAG = "cinema_activity";



    private RecyclerView rvRelis;

    private RelisListAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RelisListAdapter.OnItemTypeClickListener listener = item -> {
        Intent intent= new Intent(this,FilmActivity.class).putExtra(ITEM_TYPE, item);
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
        btnReadMore = findViewById(R.id.btnReadMore);
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

        btnReadMore.setOnClickListener(v -> {
            Intent intent = new Intent(this,FilmActivity.class);
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