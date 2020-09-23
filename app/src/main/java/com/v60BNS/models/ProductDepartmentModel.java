package com.v60BNS.models;

import java.io.Serializable;
import java.util.List;

public class ProductDepartmentModel implements Serializable {
    private List<Data> data;
    private int current_page;

    public List<Data> getData() {
        return data;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public class Data implements Serializable {
        private int id;
        private String department_id;
        private String product_id;
        private Department department;

        public int getId() {
            return id;
        }

        public String getDepartment_id() {
            return department_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public Department getDepartment() {
            return department;
        }

        public class Department implements Serializable {
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
    }
}
