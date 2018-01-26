package com.ng.ganhuoapi.data.book;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Horrarndoo on 2017/10/21.
 *
 */

public class BookItemBean implements Serializable {

    @SerializedName("rating")
    private RatingBean rating;
    @SerializedName("subtitle")
    private String subtitle;
    @SerializedName("pubdate")
    private String pubdate;
    @SerializedName("origin_title")
    private String origin_title;
    @SerializedName("image")
    private String image;
    @SerializedName("binding")
    private String binding;
    @SerializedName("catalog")
    private String catalog;
    @SerializedName("pages")
    private String pages;
    @SerializedName("images")
    private ImagesBean images;
    @SerializedName("alt")
    private String alt;
    @SerializedName("id")
    private String id;
    @SerializedName("publisher")
    private String publisher;
    @SerializedName("isbn10")
    private String isbn10;
    @SerializedName("isbn13")
    private String isbn13;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;
    @SerializedName("alt_title")
    private String alt_title;
    @SerializedName("author_intro")
    private String author_intro;
    @SerializedName("summary")
    private String summary;
    @SerializedName("price")
    private String price;
    @SerializedName("author")
    private List<String> author;
    @SerializedName("tags")
    private List<TagsBean> tags;
    @SerializedName("translator")
    private List<String> translator;// 翻译


    public static class RatingBean implements Serializable {
        /**
         * max : 10
         * numRaters : 116375
         * average : 7.9
         * min : 0
         */
        @SerializedName("max")
        private int max;
        @SerializedName("numRaters")
        private int numRaters;
        @SerializedName("average")
        private String average;
        @SerializedName("min")
        private int min;

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getNumRaters() {
            return numRaters;
        }

        public void setNumRaters(int numRaters) {
            this.numRaters = numRaters;
        }

        public String getAverage() {
            return average;
        }

        public void setAverage(String average) {
            this.average = average;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }
    }


    public static class TagsBean implements Serializable {
        /**
         * count : 38955
         * name : 韩寒
         * title : 韩寒
         */
        @SerializedName("count")
        private int count;
        @SerializedName("name")
        private String name;
        @SerializedName("title")
        private String title;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public RatingBean getRating() {
        return rating;
    }

    public void setRating(RatingBean rating) {
        this.rating = rating;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getOrigin_title() {
        return origin_title;
    }

    public void setOrigin_title(String origin_title) {
        this.origin_title = origin_title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlt_title() {
        return alt_title;
    }

    public void setAlt_title(String alt_title) {
        this.alt_title = alt_title;
    }

    public String getAuthor_intro() {
        return author_intro;
    }

    public void setAuthor_intro(String author_intro) {
        this.author_intro = author_intro;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public List<String> getTranslator() {
        return translator;
    }

    public void setTranslator(List<String> translator) {
        this.translator = translator;
    }

    /**
     * 获取Author字符串
     *
     * @return Author字符串 A/B/C..
     */
    public String getAuthorsString() {
        return sListToString(getAuthor());
    }

    /**
     * 格式化list为字符串
     *
     * @param list 类型list
     * @return 字符串 A/B/C..
     */
    private String sListToString(List<String> list) {
        String str = "";
        if (list.size() == 0)
            return str;
        for (int i = 0; i < list.size(); i++) {
            str = str + list.get(i);
            if (i < list.size() - 1)
                str += " / ";
        }
        return str;
    }

    /**
     * 格式化list为字符串
     *
     * @param list 类型list
     * @return 字符串 A/B/C..
     */
    private String pListToString(List<PersonBean> list) {
        String str = "";
        if (list.size() == 0)
            return str;
        for (int i = 0; i < list.size(); i++) {
            str = str + list.get(i).getName();
            if (i < list.size() - 1)
                str += " / ";
        }
        return str;
    }
}
