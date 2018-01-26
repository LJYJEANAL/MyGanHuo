package com.ng.ganhuoapi.base;

import java.util.Collection;

import io.reactivex.disposables.Disposable;

/**
 * View 的基类
 * @param <T>
 */

public interface IBaseView<T,V>{
    /**
     * 显示数据
     */
    void showData(Collection<? extends V> data);
    /**
     * 显示加载动画
     */
    void onShowLoading();

    /**
     * 隐藏加载
     */
    void onHideLoading();

    /**
     * 显示网络错误
     */
    void onShowNetError(String err);

    /**
     * 设置 presenter
     */
    void setPresenter(T presenter);
    void disPosable(Disposable disposable);


}
