package com.v60BNS.models;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable {
    private List<SingleProductModel> data;
    private int current_page;

    public List<SingleProductModel> getData() {
        return data;
    }

    public int getCurrent_page() {
        return current_page;
    }

}
