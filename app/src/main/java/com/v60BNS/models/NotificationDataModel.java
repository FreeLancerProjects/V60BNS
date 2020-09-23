package com.v60BNS.models;

import java.io.Serializable;
import java.util.List;

public class NotificationDataModel implements Serializable {

    private List<NotificationModel> data;
    private int current_page;

    public List<NotificationModel> getData() {
        return data;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public class NotificationModel implements Serializable {
        private String title;
        private String desc;
        private String notification_date;

        public String getTitle() {
            return title;
        }

        public String getDesc() {
            return desc;
        }

        public String getNotification_date() {
            return notification_date;
        }
    }
}
