package com.captain.ak.parkingspaces.Models;

import com.google.firebase.database.IgnoreExtraProperties;

public class UserModel {
    private String count,name;

    public UserModel(){

    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
