package com.ng.ganhuoapi.data.movie;

import java.util.List;

/**
 * Created by Administrator on 2017/12/26.
 */

public class MovieContentBean {
    private int count;
    private int start;
    private int total;
    private List<SubItemsBean>subjects;

    private String title;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public List<SubItemsBean> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubItemsBean> subjects) {
        this.subjects = subjects;
    }

    public static class USBoxSubjects  {

        private List<SubItemsBean>subject;

        public List<SubItemsBean> getSubject() {
            return subject;
        }

        public void setSubject(List<SubItemsBean> subject) {
            this.subject = subject;
        }


    }

}
