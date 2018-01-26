package com.ng.ganhuoapi.base;

/**
 * Presenter父类 可以继承抗展
 */

public interface IBasePresenter {
    /**
     * 加载数据
     */
    void doLoadData(boolean isLoadMore, int limit, int start);

    /**
     * 刷新数据
     */
    void doRefresh();


}
