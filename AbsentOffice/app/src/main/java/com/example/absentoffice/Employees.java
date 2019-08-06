package com.example.absentoffice;

public class Employees {
    private String id;
    private String name;
    private String occup;
    private String email;
    private String password;
    private String profileImageURL;
    private String IMEINum;

    public Employees(String name, String occup, String email, String password, String profileImageURL, String IMEINum) {
        this.setId(id);
        this.setname(name);
        this.setOccup(occup);
        this.setEmail(email);
        this.setPassword(password);
        this.setProfileImageURL(profileImageURL);
        this.setIMEINum(IMEINum);
    }

    public Employees(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getOccup() {
        return occup;
    }

    public void setOccup(String occup) {
        this.occup = occup;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getIMEI() {
        return IMEINum;
    }

    public void setIMEINum(String IMEINum) {
        this.IMEINum = IMEINum;
    }
}
