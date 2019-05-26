package com.example.administrator.app.bean;

import cn.bmob.v3.BmobObject;

public class CollectNews extends BmobObject {
    public String username;
    public String postid;
    public String title;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

