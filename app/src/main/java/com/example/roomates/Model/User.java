package com.example.roomates.Model;

public class User {
    private  String id;
    private  String userName;
    private String email;
    private  String imageUrl;

    public User(String id, String userName, String fullName, String email, String imageUrl) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public  User(){
    }




    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
