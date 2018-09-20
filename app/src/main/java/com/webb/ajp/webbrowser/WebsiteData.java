package com.webb.ajp.webbrowser;

public class WebsiteData {
    public String url;
    public int img;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public WebsiteData(String url, int img, String title) {
        this.url = url;
        this.img = img;
        this.title = title;
    }

    public String title;
    public WebsiteData(){

    }
}
