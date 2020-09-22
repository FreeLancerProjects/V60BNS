package com.v60BNS.models;

import java.io.Serializable;

public class MessageModel implements Serializable {

    private int id;
    private int chat_room_id;
    private int from_user_id;
    private int to_user_id;
    private String message_kind;
    private String message;
    private String file_link;
    private long date;
    private String is_read;

    public MessageModel(int id, int chat_room_id, int from_user_id, int to_user_id, String message_kind, String message, String file_link, long date, String is_read) {
        this.id = id;
        this.chat_room_id = chat_room_id;
        this.from_user_id = from_user_id;
        this.to_user_id = to_user_id;
        this.message_kind = message_kind;
        this.message = message;
        this.file_link = file_link;
        this.date = date;
        this.is_read = is_read;
    }

    public int getId() {
        return id;
    }

    public int getChat_room_id() {
        return chat_room_id;
    }

    public int getFrom_user_id() {
        return from_user_id;
    }

    public int getTo_user_id() {
        return to_user_id;
    }

    public String getMessage_kind() {
        return message_kind;
    }

    public String getMessage() {
        return message;
    }

    public String getFile_link() {
        return file_link;
    }

    public long getDate() {
        return date;
    }

    public String getIs_read() {
        return is_read;
    }
}