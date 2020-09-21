package com.v60BNS.models;

import java.io.Serializable;
import java.util.List;

public class StoryModel implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class Data implements Serializable {
        private int id;
        private int user_id;
        private String ar_title;
        private String en_title;
        private String ar_desc;
        private String en_desc;
        private String image;
        private UserModel user;
        private String logo;
        private String phone_code;

        public int getId() {
            return id;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getAr_desc() {
            return ar_desc;
        }

        public String getEn_desc() {
            return en_desc;
        }

        public String getImage() {
            return image;
        }

        public String getLogo() {
            return logo;
        }

        public UserModel getUser() {
            return user;
        }

        public String getPhone_code() {
            return phone_code;
        }
    }
}