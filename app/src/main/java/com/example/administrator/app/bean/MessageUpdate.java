package com.example.administrator.app.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class MessageUpdate extends BmobObject {
    private String name;
    private String message;
    private String location;
    private BmobFile pic;
    private BmobGeoPoint address;

    public BmobGeoPoint getAddress()
    {
        return address;
    }
    public void setAddress(BmobGeoPoint address)
    {
        this.address = address;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getMessage()
    {
        return message;
    }
    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getLocation()
    {
        return location;
    }
    public void setLocation(String location)
    {
        this.location = location;
    }

    public BmobFile getPic()
    {
        return pic;
    }
    public void setPic(BmobFile pic)
    {
        this.pic = pic;
    }

}
