package com.v60BNS.models;

import java.io.Serializable;
import java.util.List;

public class CategoryDataModel implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable {

        private int id;
        private String ar_title;
        private String background;
        private String image;
        private String icon;
        private String details;
        private String parent;
        private int level;

        public int getId() {
            return id;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getBackground() {
            return background;
        }

        public String getImage() {
            return image;
        }

        public String getIcon() {
            return icon;
        }

        public String getDetails() {
            return details;
        }

        public String getParent() {
            return parent;
        }

        public int getLevel() {
            return level;
        }

    }}