package com.team.sonemo.Model;

public class CommentModel {
    private String commentDescription, commentUsername, commentText, uid;
    public CommentModel (){

    }

    public CommentModel(String commentDescription, String commentUsername, String commentText) {
        this.commentDescription = commentDescription;
        this.commentUsername = commentUsername;
        this.commentText = commentText;
    }

    public String getCommentDescription() {
        return commentDescription;
    }

    public void setCommentDescription(String commentDescription) {
        this.commentDescription = commentDescription;
    }

    public String getCommentUsername() {
        return commentUsername;
    }

    public void setCommentUsername(String commentUsername) {
        this.commentUsername = commentUsername;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

}

