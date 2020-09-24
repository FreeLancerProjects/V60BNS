package com.v60BNS.models;

import java.io.Serializable;
import java.util.List;

public class Add_Order_Model implements Serializable {

    private double longitude;
    private double latitude;
    private String address;
    private double total_cost;
    private String payment_type;
    private List<OrderDetails> order_details;
    private List<ProductDetails> productDetails;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public List<OrderDetails> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(List<OrderDetails> order_details) {
        this.order_details = order_details;
    }

    public static class OrderDetails implements Serializable {
        private int product_id;
        private int amount;
        private double total_cost;

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public double getTotal_cost() {
            return total_cost;
        }

        public void setTotal_cost(double total_cost) {
            this.total_cost = total_cost;
        }
    }

    public List<ProductDetails> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ProductDetails> productDetails) {
        this.productDetails = productDetails;
    }

    public static class ProductDetails implements Serializable {
        private int amount;
        private double total_cost;
        private String image;
        private String name;


        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public double getTotal_cost() {
            return total_cost;
        }

        public void setTotal_cost(double total_cost) {
            this.total_cost = total_cost;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
