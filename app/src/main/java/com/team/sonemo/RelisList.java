package com.team.sonemo;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

public class RelisList implements Serializable {


    private String filmName;
    private String filmData;
    private String posterId;
    private String cast;
    private String filmDescription;
    private String trailerURL;
    public RelisList(String filmName, String filmData, String posterId, String cast, String filmDescription, String trailerURL) {

        this.filmName=filmName;
        this.filmData=filmData;
        this.posterId=posterId;
        this.cast=cast;
        this.filmDescription=filmDescription;
        this.trailerURL=trailerURL;

    }
    public String getTrailerURL() {
        return trailerURL;
    }

    public void setTrailerURL(String trailerURL) {
        this.trailerURL = trailerURL;
    }

    public RelisList() {
    }

    public String getFilmName() {
        return this.filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getFilmData() {
        return this.filmData;
    }

    public void setFilmData(String filmData) {
        this.filmData = filmData;
    }

    public String getPosterId() {
        return this.posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    public String getCast() {
        return this.cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getFilmDescription() {
        return this.filmDescription;
    }

    public void setFilmDescription(String filmDescription) { this.filmDescription = filmDescription;
    }

}
