package com.example.pcwh.models;

public class HackRisk {

    // Properties
    private String ip;
    private String location;
    private String date;
    private String time;

    // Constructors
    public HackRisk() {
    }

    public HackRisk(String ip, String location, String date, String time) {
        this.ip = ip;
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    @Override
    public String toString() {
        return  "IP: " + ip + "\n" +
                "Location: " + location + "\n" +
                "Date: " + date + "\n" +
                "Time: " + time;
    }
}
