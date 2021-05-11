package com.team.sonemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.team.sonemo.Model.RelisList;
//import com.team.sonemo.adapters.RecipeViewHolder;
//import com.team.sonemo.adapters.RelisListAdapter;

import java.util.ArrayList;

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
    public WebView trailer;
    public String trailerURL;


    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
        RelisList item = (RelisList) getIntent().getSerializableExtra(ITEM_TYPE);

        idFilmName = findViewById(R.id.idFilmName);
        idDataRelis = findViewById(R.id.idDataRelis);
        poster = findViewById(R.id.posterId);
        trailer =  findViewById(R.id.trailerId);
        idCast = findViewById(R.id.idCast);
        idFilmDescription = findViewById(R.id.idFilmDescription);
 //       trailer.getSettings().setJavaScriptEnabled(true);
 //       trailer.loadUrl(item.getTrailerURL());
 //       trailer.setWebChromeClient(new WebChromeClient());


        Picasso.get().load(item.getPosterId()).into(poster);
        idFilmName.setText(item.getFilmName()) ;
        idDataRelis.setText(item.getFilmData());
        idCast.setText(item.getCast());
        idFilmDescription.setText(item.getFilmData());


    }

    }




