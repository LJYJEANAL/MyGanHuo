package com.ng.ganhuoapi.data.book;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Horrarndoo on 2017/10/21.
 * <p>
 */

public class BookListBean implements Serializable {


    @SerializedName("count")
    private int count;
    @SerializedName("start")
    private int start;
    @SerializedName("total")
    private int total;
    @SerializedName("books")
    private List<BookItemBean> books;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;

    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<BookItemBean> getBooks() {
        return books;
    }

    public void setBooks(List<BookItemBean> books) {
        this.books = books;
    }
}
