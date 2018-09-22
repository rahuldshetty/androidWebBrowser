package com.webb.ajp.webbrowser;

import android.graphics.Bitmap;

public class TabData {
    public TabData(){


    }

    public Bitmap image;
    public String title,desc;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public TabData(Bitmap image, String title, String desc) {
        this.image = image;
        this.title = title;
        this.desc = desc;
    }
}
