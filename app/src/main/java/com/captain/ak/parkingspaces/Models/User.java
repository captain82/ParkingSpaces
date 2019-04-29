package com.captain.ak.parkingspaces.Models;

import android.databinding.BaseObservable;
import android.text.TextUtils;
import android.util.Patterns;

public class User extends BaseObservable {

        public String email,password;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
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

    public int isValidData()
    {
        if (TextUtils.isEmpty(getEmail()))
            return 0;
        else if (!Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches())
            return 1;
        else if (TextUtils.isEmpty(getPassword())|| getPassword().length()<6)
            return 2;
        else
            return -1;

    }

}
