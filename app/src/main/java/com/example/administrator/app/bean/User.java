package com.example.administrator.app.bean;

import cn.bmob.v3.BmobObject;

public class User extends BmobObject {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
