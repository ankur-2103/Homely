package com.example.android.homely.Data;

public class UserData {
    public String fname;
    public String phone;
    public String email;
    public Boolean isAdmin;
    public String photo;
    public UserData(String fname, String phoneno, String email){
        this.fname = fname;
        this.phone = phoneno;
        this.email = email;
        this.isAdmin = false;
        this.photo = null;
    }
}
