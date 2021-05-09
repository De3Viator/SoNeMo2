package com.team.sonemo.Model;

import java.util.ArrayList;

public class PostModel {
    private String postId, postTitle, postDescription, postImage, postTime,  uId, puName, pTimeStape, puPicture,pComment;
    private ArrayList <String> pLikes = new ArrayList<>();
    private ArrayList<String> comments = new ArrayList<>();
    private boolean isLiked;



    public PostModel(){

    }




    public PostModel(String postId, String postTitle, String postDescription, String postImage, String postTime, String uId, String puName, String pTimeStape, String puPicture, ArrayList <String> pLikes, String pComment, ArrayList<String> comments) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.postImage = postImage;
        this.postTime = postTime;
        this.uId = uId;
        this.puName = puName;
        this.pTimeStape = pTimeStape;
        this.puPicture = puPicture;
        this.pLikes = pLikes;
        this.pComment = pComment;
        this.comments = comments;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public String getpComment() {
        return pComment;
    }

    public void setpComment(String pComment) {
        this.pComment = pComment;
    }

    public ArrayList<String> getpLikes() {
        return pLikes;
    }

    public void setpLikes(ArrayList<String> pLikes) {
        this.pLikes = pLikes;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getPuName() {
        return puName;
    }

    public void setPuName(String puName) {
        this.puName = puName;
    }

    public String getpTimeStape() {
        return pTimeStape;
    }

    public void setpTimeStape(String pTimeStape) {
        this.pTimeStape = pTimeStape;
    }

    public String getPuPicture() {
        return puPicture;
    }

    public void setPuPicture(String puPicture) {
        this.puPicture = puPicture;
    }



}
