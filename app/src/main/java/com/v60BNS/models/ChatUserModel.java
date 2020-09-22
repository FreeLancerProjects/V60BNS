package com.v60BNS.models;

import java.io.Serializable;

public class ChatUserModel implements Serializable {

    private String name;
    private String image;
    private int id;
    private int room_id;



    public ChatUserModel(String name, String image, int id, int room_id) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.room_id = room_id;

    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public int getRoom_id() {
        return room_id;
    }


}
