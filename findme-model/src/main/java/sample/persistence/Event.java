package sample.persistence;

import java.io.Serializable;

class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String data;

    public Event(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}