package com.erstudio.guper.model;

/**
 * Created by Евгений on 20.09.2015.
 */
public class Message {
    private Location location;
    private String text;

    public Message() {
    }

    public Message(Location location, String text) {
        this.location = location;
        this.text = text;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
