package com.example.chatgrount.MainActyvity.Model;

public class User {
    private String name;
    private String image;
    private String icon;
    private String key;
    private String tinnhan;
    private String nguoigui;
    private String time;



    public User(String name, String image, String icon, String key, String tinnhan, String nguoigui, String time) {
        this.name = name;
        this.image = image;
        this.icon = icon;
        this.key = key;
        this.tinnhan = tinnhan;
        this.nguoigui = nguoigui;
        this.time = time;
    }

    public String getTinnhan() {
        return tinnhan;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTinnhan(String tinnhan) {
        this.tinnhan = tinnhan;
    }

    public String getNguoigui() {
        return nguoigui;
    }

    public void setNguoigui(String nguoigui) {
        this.nguoigui = nguoigui;
    }

    public User() {
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
