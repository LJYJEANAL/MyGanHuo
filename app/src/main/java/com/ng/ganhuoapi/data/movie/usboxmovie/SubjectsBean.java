package com.ng.ganhuoapi.data.movie.usboxmovie;

import com.ng.ganhuoapi.data.movie.SubItemsBean;

/**
 * Created by Administrator on 2017/12/27.
 */

public class SubjectsBean {
    private int box;
    private int rank;
    private SubItemsBean subject;

    public int getBox() {
        return box;
    }

    public void setBox(int box) {
        this.box = box;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public SubItemsBean getSubject() {
        return subject;
    }

    public void setSubject(SubItemsBean subject) {
        this.subject = subject;
    }
}
