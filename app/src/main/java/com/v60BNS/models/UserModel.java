package com.v60BNS.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private int id;
    private String user_type;
    private String name;
    private String email;
    private String phone_code;
    private String phone;
    private String logo;
    private String banner;
    private String chat_available;
    private double balance;
    private double rating;
    private String email_verified_at;
    private String is_block;
    private String is_accepted;
    private String is_login;
    private long logout_time;
    private String token;

    public int getId() {
        return id;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getLogo() {
        return logo;
    }

    public String getBanner() {
        return banner;
    }

    public String getChat_available() {
        return chat_available;
    }

    public double getBalance() {
        return balance;
    }

    public double getRating() {
        return rating;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public String getIs_block() {
        return is_block;
    }

    public String getIs_accepted() {
        return is_accepted;
    }

    public String getIs_login() {
        return is_login;
    }

    public long getLogout_time() {
        return logout_time;
    }

    public String getToken() {
        return token;
    }
}
