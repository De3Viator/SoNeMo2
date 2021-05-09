package com.team.sonemo.Model;

public class UserModel {
    String username,email,password,uid,image,search,age,country;

    public UserModel(String username, String email, String password, String age, String country) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public UserModel(){
    }

    public UserModel(String username, String email, String password, String uid, String image, String search, String age, String country) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.uid = uid;
        this.image = image;
        this.search = search;
        this.age = age;
        this.country = country;
    }

}
