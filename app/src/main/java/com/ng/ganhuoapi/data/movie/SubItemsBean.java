package com.ng.ganhuoapi.data.movie;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/26.
 */

public class SubItemsBean implements MultiItemEntity {
    private RateBean rating;
    private List<String> author;
    private String title;
    private List<CastsBean> casts;
    private int collect_count;
    private String original_title;
    private String subtype;
    private List<CastsBean> directors;
    private String year;
    private ImagesBean images;
    private String alt;
    private String id;
    private String [] genres;


    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    /**
     * 普通布局 一行 正在上映  北美票房
     */
    public static final int MOVIE_VIEW_NORMAL = 1;
    /**
     * 双列布局  即将上映 Top250
     */
    public static final int MOVIE_VIEW_CUSTOM = 2;

    public int itemType = 1;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public RateBean getRating() {
        return rating;
    }

    public void setRating(RateBean rating) {
        this.rating = rating;
    }

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CastsBean> getCasts() {
        return casts;
    }

    public void setCasts(List<CastsBean> casts) {
        this.casts = casts;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public List<CastsBean> getDirectors() {
        return directors;
    }

    public void setDirectors(List<CastsBean> directors) {
        this.directors = directors;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public ImagesBean getImages() {
        return images;
    }

    public void setImages(ImagesBean images) {
        this.images = images;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public static class ImagesBean {
        private String small;
        private String large;
        private String medium;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }
    }

    public static class RateBean {
        private int max;
        private String average;
        private String stars;
        private int min;

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public String getAverage() {
            return average;
        }

        public void setAverage(String average) {
            this.average = average;
        }

        public String getStars() {
            return stars;
        }

        public void setStars(String stars) {
            this.stars = stars;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }
    }
}
