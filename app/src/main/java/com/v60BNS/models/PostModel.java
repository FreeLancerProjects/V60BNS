package com.v60BNS.models;

import java.io.Serializable;
import java.util.List;

public class PostModel implements Serializable {
    private List<Data> data;
    private int current_page;

    public List<Data> getData() {
        return data;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public static class Data implements Serializable {
        private int id;
        private int user_id;
        private String ar_title;
        private String en_title;
        private String ar_desc;
        private String en_desc;
        private String link_for_share;
        private String image;
        private String place_id;
        private String address;
        private double latitude;
        private double longitude;
        private String is_shown;
        private int loves_count;
        private int shares_count;
        private boolean love_check;
        private UserModel user;

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

        public String getLink_for_share() {
            return link_for_share;
        }

        public String getImage() {
            return image;
        }

        public String getPlace_id() {
            return place_id;
        }

        public String getAddress() {
            return address;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getIs_shown() {
            return is_shown;
        }

        public int getLoves_count() {
            return loves_count;
        }

        public int getShares_count() {
            return shares_count;
        }

        public boolean isLove_check() {
            return love_check;
        }

        public UserModel getUser() {
            return user;
        }
    }
}