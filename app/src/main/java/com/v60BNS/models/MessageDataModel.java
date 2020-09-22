package com.v60BNS.models;

import java.io.Serializable;
import java.util.List;

public class MessageDataModel implements Serializable {
    private Room room;

    public Room getRoom() {
        return room;
    }

    public class  Room implements Serializable {

    }

    private Messages messages;


    public Messages getMessages() {
        return messages;
    }


    public class Messages implements Serializable
    {
        private int current_page;
        private List<MessageModel> data;

        public int getCurrent_page() {
            return current_page;
        }

        public List<MessageModel> getData() {
            return data;
        }
    }
}
