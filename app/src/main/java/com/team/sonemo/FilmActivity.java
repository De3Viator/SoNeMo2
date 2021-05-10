package com.team.sonemo;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.team.sonemo.Model.RelisList;

public class FilmActivity extends AppCompatActivity  {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static String TAG = "film_activity";
    public  String idFilm;
    public TextView idFilmName;
    public TextView idDataRelis;
    public TextView idCast;
    public TextView idFilmDescription;
    public ImageView poster;
    public static String ITEM_TYPE ="ITEM_TYPE";
    public VideoView trailer;
    public String trailerURL;


    protected void onCreate(Bundle savedInstanceState, RelisList recipe) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
        RelisList item = (RelisList) getIntent().getSerializableExtra(ITEM_TYPE);
        idFilmName = findViewById(R.id.idFilmName);
        idDataRelis = findViewById(R.id.idDataRelis);
        poster = findViewById(R.id.idPoster);
        String videoSource = trailerURL;
        trailer =  findViewById(R.id.trailerId);

        trailer.setVideoURI(Uri.parse(trailerURL));
        Picasso.get().load(recipe.getPosterId()).into(poster);
        idFilmName.setText(recipe.getFilmName()) ;
        idDataRelis.setText(recipe.getFilmData());
        idCast.setText(recipe.getCast());
        idFilmDescription.setText(recipe.getFilmData());

        trailer.setMediaController(new MediaController(this));
        trailer.requestFocus(0);
        trailer.start();
        trailer.setMediaController(new MediaController(this));
    }

    }




