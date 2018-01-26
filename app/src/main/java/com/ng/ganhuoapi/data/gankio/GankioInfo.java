package com.ng.ganhuoapi.data.gankio;

import android.graphics.Bitmap;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */

public class GankioInfo implements MultiItemEntity{
    private String _id;
    private String createdAt;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean userd;
    private String who;
    private List<String> images;
    private Bitmap bitmap;
    /**
     * 普通布局，带略缩图
     */
    public static final int GANK_IO_DAY_ITEM_CUSTOM_NORMAL = 1;
    /**
     * 图片布局，福利
     */
    public static final int GANK_IO_DAY_ITEM_CUSTOM_IMAGE = 2;
    /**
     * 无图布局
     */
    public static final int GANK_IO_DAY_ITEM_CUSTOM_NO_IMAGE = 3;
    public int itemType = 1;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUserd() {
        return userd;
    }

    public void setUserd(boolean userd) {
        this.userd = userd;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getImages() {
        return images;
    }
    @Override
    public int getItemType() {
        switch (getType()){
            case "福利":
                itemType=GANK_IO_DAY_ITEM_CUSTOM_IMAGE;
              break;
            case "Android":
            case "iOS":
            case "前端":
            case "休息视频":
            case "瞎推荐":
            case "拓展资源":
                if (getImages()!=null&&getImages().size()>0){
                    itemType=GANK_IO_DAY_ITEM_CUSTOM_NORMAL;
                }else{
                    itemType=GANK_IO_DAY_ITEM_CUSTOM_NO_IMAGE;
                }

        }
        return itemType;
    }
}
