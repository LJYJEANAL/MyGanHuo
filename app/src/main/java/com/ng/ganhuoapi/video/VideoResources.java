package com.ng.ganhuoapi.video;

/**
 * Created by Administrator on 2018/1/25.
 */

public class VideoResources {
    private String url;
    private String name;

    public VideoResources(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
