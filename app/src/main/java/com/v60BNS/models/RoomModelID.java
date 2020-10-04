package com.v60BNS.models;

import java.io.Serializable;

public class RoomModelID implements Serializable {
    private int room_id;

    public int getRoom_id() {
        return room_id;
    }

    public RoomModelID(int room_id) {
        this.room_id = room_id;
    }
}
