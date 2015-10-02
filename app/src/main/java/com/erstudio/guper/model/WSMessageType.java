package com.erstudio.guper.model;

/**
 * Created by Евгений on 28.09.2015.
 */
public enum WSMessageType {
    AUTHORIZE("AUTHORIZE"), SEND_MESSAGE("SEND_MESSAGE"), UPDATE_LOCATION("UPDATE_LOCATION"), CONFIRM("CONFIRM");

    private final String text;

    /**
     * @param text
     */
    private WSMessageType(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
