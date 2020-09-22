package com.v60BNS.models;

import java.io.Serializable;
import java.util.List;

public class ExpertModel implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class Data implements Serializable {
        private int id;
        private String user_type;
        private String name;
        private String email;
        private String phone_code;
        private String phone;
        private String logo;
        private String banner;
        private String chat_available;
        private String balance;
        private double rating;

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

        public String getBalance() {
            return balance;
        }

        public double getRating() {
            return rating;
        }
    }
}