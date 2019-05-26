package com.example.administrator.app.bean;

import java.io.File;

import cn.bmob.v3.BmobObject;

public class BallCourt extends BmobObject {
    private String latitude;
    private String longitude;
    private String court_name;
    private String court_addr;
    private String court_img;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCourt_name() {
        return court_name;
    }

    public void setCourt_name(String court_name) {
        this.court_name = court_name;
    }

    public String getCourt_addr() {
        return court_addr;
    }

    public void setCourt_addr(String court_addr) {
        this.court_addr = court_addr;
    }

    public String getCourt_img() {
        return court_img;
    }

    public void setCourt_img(String court_img) {
        this.court_img = court_img;
    }
}
