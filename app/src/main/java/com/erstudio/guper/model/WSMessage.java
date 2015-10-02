package com.erstudio.guper.model;

/**
 * Created by Евгений on 20.09.2015.
 */
public class WSMessage {
    private WSMessageType type;
    private Object message;

    public WSMessage(WSMessageType type, Object message) {
        this.type = type;
        this.message = message;
    }

    public WSMessageType getType() {
        return type;
    }

    public void setType(WSMessageType type) {
        this.type = type;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
