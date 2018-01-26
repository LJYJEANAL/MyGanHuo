package com.ng.ganhuoapi.data.gankio;

import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */

public class Gankio  {
    private List<GankioInfo> results;
    public List<GankioInfo> getResults() {
        return results;
    }

    public void setResults(List<GankioInfo> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Gankio{" +
                "results=" + results +
                '}';
    }
}
