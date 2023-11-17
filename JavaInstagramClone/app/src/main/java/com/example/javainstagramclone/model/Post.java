package com.example.javainstagramclone.model;

public class Post {

    String email;
    String comment;
    String downloadUrl;

    public Post(String email, String comment, String downloadUrl) {
        this.email = email;
        this.comment = comment;
        this.downloadUrl = downloadUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
