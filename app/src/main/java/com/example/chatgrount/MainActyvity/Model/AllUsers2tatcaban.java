package com.example.chatgrount.MainActyvity.Model;

public class AllUsers2tatcaban {
    private String image;
    private String name;
    private String key;
    private String status;

    public AllUsers2tatcaban(String image, String name, String key, String status) {
        this.image = image;
        this.name = name;
        this.key = key;
        this.status = status;
    }

    public AllUsers2tatcaban() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
