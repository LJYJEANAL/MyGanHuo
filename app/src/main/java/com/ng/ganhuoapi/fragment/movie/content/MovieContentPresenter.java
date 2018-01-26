package com.ng.ganhuoapi.fragment.movie.content;

import com.ng.ganhuoapi.base.IBasePresenter;
import com.ng.ganhuoapi.base.IBaseView;
import com.ng.ganhuoapi.constant.Constant;
import com.ng.ganhuoapi.data.movie.SubItemsBean;
import com.ng.ganhuoapi.network.NetManager;
import com.ng.ganhuoapi.util.Listener;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/26.
 */

class MovieContentPresenter implements IBasePresenter {
    private IBaseView iBaseView;
    private int position;

    public MovieContentPresenter(IBaseView iBaseView, int position) {
        this.iBaseView = iBaseView;
        this.position = position;
    }

    @Override
    public void doLoadData(boolean isLoadMore, int limit, int start) {
        if (!isLoadMore){
            iBaseView.onShowLoading();
        }
        NetManager.getInstance().getMovieContentData(position,start,limit, new Listener<Disposable, List<SubItemsBean>>() {
            @Override
            public void onCallBack(Disposable disposable, List<SubItemsBean> reply) {
                iBaseView.disPosable(disposable);
            }

            @Override
            public void onSuccess(List<SubItemsBean> reply) {
                if (reply!=null){
                    iBaseView.showData(reply);
                }

            }

            @Override
            public void onComplete() {
                iBaseView.onHideLoading();

            }

            @Override
            public void onFailed(String reply) {
                iBaseView.onShowNetError(reply);
            }
        });
    }

    @Override
    public void doRefresh() {
        doLoadData(false, Constant.limit, 0);

    }
}
