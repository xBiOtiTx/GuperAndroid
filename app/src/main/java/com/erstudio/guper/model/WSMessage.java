package com.erstudio.guper.model;

/**
 * Created by Евгений on 20.09.2015.
 */
public class WSMessage {
    private String type;
    private Object message;

    public WSMessage(String type, Object message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
