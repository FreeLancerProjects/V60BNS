package com.v60BNS.models;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class SingleProductModel implements Serializable {

    private int id;

    private String ar_title;
    private String ar_desc;
    private String ar_components;
    private String weight;
    private String color;
    private int category_id;
    private String main_image;
    private String price;
    private String rating_value;
    private String stock;
    private String amount;
    //private List<Category> category;
    private List<ProductImage> product_images;



    public int getId() {
        return id;
    }

    public String getAr_title() {
        return ar_title;
    }

    public String getAr_desc() {
        return ar_desc;
    }

    public String getAr_components() {
        return ar_components;
    }

    public String getWeight() {
        return weight;
    }

    public String getColor() {
        return color;
    }

    public int getCategory_id() {
        return category_id;
    }

    public String getMain_image() {
        return main_image;
    }

    public String getPrice() {
        return price;
    }

    public String getRating_value() {
        return rating_value;
    }

    public String getStock() {
        return stock;
    }

    public String getAmount() {
        return amount;
    }

    public List<ProductImage> getProduct_images() {
        return product_images;
    }
    /*

    public List<Category> getCategory() {
        return category;
    }

    public class Category implements Serializable {
        private int id;
        private String ar_title;
        private String image;


        public int getId() {
            return id;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getImage() {
            return image;
        }
    }
*/

    public class ProductImage implements Serializable {
        private int id;
        private String image;


        public int getId() {
            return id;
        }

        public String getImage() {
            return image;
        }
    }

}


