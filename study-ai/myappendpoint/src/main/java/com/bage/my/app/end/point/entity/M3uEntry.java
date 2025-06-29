package com.bage.my.app.end.point.entity;

public class M3uEntry {
    private int id;
    private String logo;
    private String title;
    private String url;
    private String name;

    public M3uEntry(int id, String logo, String title, String url, String name) {
        this.id = id;
        this.logo = logo;
        this.title = title;
        this.url = url;
        this.name = name;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getLogo() {
        return logo;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}