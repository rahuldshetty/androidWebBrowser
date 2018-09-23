package com.webb.ajp.webbrowser;

public class HistoryDate {
    String timestamp,url,title;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HistoryDate(String timestamp, String url, String title) {

        this.timestamp = timestamp;
        this.url = url;
        this.title = title;
    }

    public HistoryDate(){

    }
}
