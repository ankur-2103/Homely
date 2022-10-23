package com.example.android.homely;

import android.net.Uri;

public class User {
    public String fname;
    public String phone;
    public String email;
    private Boolean isAdmin;
    public String photo;
    public User(String fname, String phoneno, String email){
        this.fname = fname;
        this.phone = phoneno;
        this.email = email;
        this.isAdmin = false;
        this.photo = null;
    }
}
