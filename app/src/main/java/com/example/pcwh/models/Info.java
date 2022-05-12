package com.example.pcwh.models;

public class Info {

    // Properties
    private String id;
    private String text;

    // Constructors
    public Info() {
    }

    public Info(String id, String text) {
        this.id = id;
        this.text = text;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
