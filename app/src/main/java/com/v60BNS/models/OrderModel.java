package com.v60BNS.models;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {
    private int id;
    private int user_id;
    private double longitude;
    private double latitude;
    private String address;
    private String other_phone;
    private String status;
    private double total_cost;
    private String start_shipping_date;
    private String start_shipping_time;
    private String end_shipping_date;
    private String end_shipping_time;
    private double rating;
    private String currency;
    private String payment_type;
    private String shipping_method;
    private String payPal_payment_id;
    private String PayerID;
    private String payPal_token;
    private String payPal_payment_status;
    private List<OrderDetails> order_details;

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getAddress() {
        return address;
    }

    public String getOther_phone() {
        return other_phone;
    }

    public String getStatus() {
        return status;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public String getStart_shipping_date() {
        return start_shipping_date;
    }

    public String getStart_shipping_time() {
        return start_shipping_time;
    }

    public String getEnd_shipping_date() {
        return end_shipping_date;
    }

    public String getEnd_shipping_time() {
        return end_shipping_time;
    }

    public double getRating() {
        return rating;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public String getShipping_method() {
        return shipping_method;
    }

    public String getPayPal_payment_id() {
        return payPal_payment_id;
    }

    public String getPayerID() {
        return PayerID;
    }

    public String getPayPal_token() {
        return payPal_token;
    }

    public String getPayPal_payment_status() {
        return payPal_payment_status;
    }

    public List<OrderDetails> getOrderDetails() {
        return order_details;
    }

    public class OrderDetails implements Serializable {
        private SingleProductModel product;
        private int id;
        private int order_id;
        private int product_id;
        private int amount;
        private double total_cost;

        public SingleProductModel getProduct() {
            return product;
        }

        public int getId() {
            return id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public int getProduct_id() {
            return product_id;
        }

        public int getAmount() {
            return amount;
        }

        public double getTotal_cost() {
            return total_cost;
        }



    }


}
