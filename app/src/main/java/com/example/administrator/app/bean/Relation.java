package com.example.administrator.app.bean;

import cn.bmob.v3.BmobObject;

public class Relation extends BmobObject {
    private String like;
    private String fan;

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getFan() {
        return fan;
    }

    public void setFan(String fan) {
        this.fan = fan;
    }
}
