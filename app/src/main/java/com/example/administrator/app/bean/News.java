package com.example.administrator.app.bean;

public class News {
    String postid;
    String title;
    String digest;
    String ptime;
    String imgsrc;
    String source;

    public News(String postid, String title, String digest, String ptime, String imgsrc, String source) {
        this.postid = postid;
        this.title = title;
        this.digest = digest;
        this.ptime = ptime;
        this.imgsrc = imgsrc;
        this.source = source;
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

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
