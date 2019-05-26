package com.example.administrator.app.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class TestImage extends BmobObject {
    private String Name;
    private String Id;
    private BmobFile imageIc;
    private String Message;
    private String Location;
    private String Time;
    private BmobFile image1;
    private BmobFile image2;
    private BmobFile image3;
    private BmobFile image4;
    private BmobFile image5;
    private BmobFile image6;
    private BmobFile image7;
    private BmobFile image8;
    private BmobFile image9;

    public BmobFile getImageIc() {
        return imageIc;
    }

    public TestImage()
    {}

    public void setImageIc(BmobFile imageIc) {
        this.imageIc = imageIc;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public BmobFile getImage1() {
        return image1;
    }

    public void setImage1(BmobFile image1) {
        this.image1 = image1;
    }

    public BmobFile getImage2() {
        return image2;
    }

    public void setImage2(BmobFile image2) {
        this.image2 = image2;
    }

    public BmobFile getImage3() {
        return image3;
    }

    public void setImage3(BmobFile image3) {
        this.image3 = image3;
    }

    public BmobFile getImage4() {
        return image4;
    }

    public void setImage4(BmobFile image4) {
        this.image4 = image4;
    }

    public BmobFile getImage5() {
        return image5;
    }

    public void setImage5(BmobFile image5) {
        this.image5 = image5;
    }

    public BmobFile getImage6() {
        return image6;
    }

    public void setImage6(BmobFile image6) {
        this.image6 = image6;
    }

    public BmobFile getImage7() {
        return image7;
    }

    public void setImage7(BmobFile image7) {
        this.image7 = image7;
    }

    public BmobFile getImage8() {
        return image8;
    }

    public void setImage8(BmobFile image8) {
        this.image8 = image8;
    }

    public BmobFile getImage9() {
        return image9;
    }

    public void setImage9(BmobFile image9) {
        this.image9 = image9;
    }
}
