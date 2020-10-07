package com.v60BNS.models;

import java.io.Serializable;
import java.util.List;

public class Comments_Model implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable {
        private int id;
        private int user_id;
        private int post_id;
        private String comment;
        private UserModel user;

        public int getId() {
            return id;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getPost_id() {
            return post_id;
        }

        public String getComment() {
            return comment;
        }

        public UserModel getUser() {
            return user;
        }
    }
}
