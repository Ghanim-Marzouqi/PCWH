package com.example.pcwh.models;

public class HackRisk {

    // Properties
    private String id;
    private String source;
    private String date;
    private String time;

    // Constructors
    public HackRisk() {
    }

    public HackRisk(String id, String source, String date, String time) {
        this.id = id;
        this.source = source;
        this.date = date;
        this.time = time;
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
