package com.example.absentoffice;

public class SubmitAbsent {
        private String id;
        private String name;
        private String date;
        private String time;
        private String lat;
        private String lng;

    public SubmitAbsent(){}

    public SubmitAbsent(String name, String date, String time, String lat, String lng) {
        this.setId(id);
        this.setName(name);
        this.setDate(date);
        this.setTime(time);
        this.setLat(lat);
        this.setLng(lng);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
