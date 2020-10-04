package com.v60BNS.models;

import java.io.Serializable;
import java.util.List;

public class UserRoomModelData implements Serializable {

    private List<UserRoomModel> data;
    private int current_page;


    public int getCurrent_page() {
        return current_page;
    }

    public List<UserRoomModel> getData() {
        return data;
    }


    public class UserRoomModel implements Serializable {
        private int id;
        private int first_user_id;
        private int second_user_id;
        private int is_approved;

        private int chatRooms_count;
        private int my_message_unread_count;
        private String other_user_name;
        private String other_user_email;
        private String other_user_phone_code;
        private String other_user_phone;
        private String other_user_avatar;
        private String note;

        public void setId(int id) {
            this.id = id;
        }

        public void setFirst_user_id(int first_user_id) {
            this.first_user_id = first_user_id;
        }

        public void setSecond_user_id(int second_user_id) {
            this.second_user_id = second_user_id;
        }

        public void setIs_approved(int is_approved) {
            this.is_approved = is_approved;
        }

        public void setChatRooms_count(int chatRooms_count) {
            this.chatRooms_count = chatRooms_count;
        }

        public void setMy_message_unread_count(int my_message_unread_count) {
            this.my_message_unread_count = my_message_unread_count;
        }

        public void setOther_user_name(String other_user_name) {
            this.other_user_name = other_user_name;
        }

        public void setOther_user_email(String other_user_email) {
            this.other_user_email = other_user_email;
        }

        public void setOther_user_phone_code(String other_user_phone_code) {
            this.other_user_phone_code = other_user_phone_code;
        }

        public void setOther_user_phone(String other_user_phone) {
            this.other_user_phone = other_user_phone;
        }

        public void setOther_user_avatar(String other_user_avatar) {
            this.other_user_avatar = other_user_avatar;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public int getId() {
            return id;
        }

        public int getFirst_user_id() {
            return first_user_id;
        }

        public int getSecond_user_id() {
            return second_user_id;
        }

        public int getIs_approved() {
            return is_approved;
        }

        public int getChatRooms_count() {
            return chatRooms_count;
        }

        public int getMy_message_unread_count() {
            return my_message_unread_count;
        }

        public String getOther_user_name() {
            return other_user_name;
        }

        public String getOther_user_email() {
            return other_user_email;
        }

        public String getOther_user_phone_code() {
            return other_user_phone_code;
        }

        public String getOther_user_phone() {
            return other_user_phone;
        }

        public String getOther_user_avatar() {
            return other_user_avatar;
        }

        public String getNote() {
            return note;
        }
    }
}
