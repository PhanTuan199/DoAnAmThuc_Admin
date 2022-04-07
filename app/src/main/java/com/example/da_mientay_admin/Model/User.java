package com.example.da_mientay_admin.Model;

public class User {
    private String Uid;
    private String Name;
    private String Password;


    public User()
    {

    }

    public User( String name, String password,String uid) {
        Uid = uid;
        Name = name;
        Password = password;

    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
