package com.ng.ganhuoapi.data.movie.usboxmovie;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

public class UsBoxMoveBean {
    private String data;
    private List<SubjectsBean> subjects;
    private String title;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<SubjectsBean> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectsBean> subjects) {
        this.subjects = subjects;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
