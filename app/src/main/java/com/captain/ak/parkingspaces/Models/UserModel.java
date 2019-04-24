package com.captain.ak.parkingspaces.Models;

import com.google.firebase.database.IgnoreExtraProperties;

public class UserModel {
    private String image,name;

    public UserModel(){

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
