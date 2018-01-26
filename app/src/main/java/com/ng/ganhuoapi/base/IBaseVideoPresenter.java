package com.ng.ganhuoapi.base;

/**
 * Created by Administrator on 2017/12/28.
 */

public interface IBaseVideoPresenter <T>extends IBasePresenter {
    /**
     * 加载数据
     * t[0] 判断是否加载更多 用于加载评论数据
     * t[1]videoId
     * t[2]groupId
     */
    void  doLoadData(T... t);



    /**
     * 刷新数据
     */
    void doRefresh();
}
