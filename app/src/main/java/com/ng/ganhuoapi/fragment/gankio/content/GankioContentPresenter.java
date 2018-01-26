package com.ng.ganhuoapi.fragment.gankio.content;

import com.ng.ganhuoapi.base.IBasePresenter;
import com.ng.ganhuoapi.base.IBaseView;
import com.ng.ganhuoapi.constant.Constant;
import com.ng.ganhuoapi.data.gankio.GankioInfo;
import com.ng.ganhuoapi.network.NetManager;
import com.ng.ganhuoapi.util.Listener;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/20.
 */

public class GankioContentPresenter implements IBasePresenter {
    private IBaseView iBaseView;
    private String type;
    public GankioContentPresenter(IBaseView iBaseView,String type) {
        this.iBaseView=iBaseView;
        this.type=type;
    }

    @Override
    public void doLoadData(boolean isLoadMore,int limit,int start) {
        if (!isLoadMore){
            iBaseView.onShowLoading();
        }

        //获取数据
        NetManager.getInstance().getGankIoCall( type, limit, start, new Listener<Disposable, List<GankioInfo>>() {
            @Override
            public void onSuccess(List<GankioInfo> reply) {
                iBaseView.showData(reply);

            }

            @Override
            public void onComplete() {
                iBaseView.onHideLoading();
            }

            @Override
            public void onCallBack(Disposable disposable, List<GankioInfo> reply) {

                iBaseView.disPosable(disposable);
            }

            @Override
            public void onFailed(String reply) {
                iBaseView.onShowNetError(reply);

            }
        });

    }

    @Override
    public void doRefresh() {
        doLoadData(false,Constant.limit,Constant. start);
    }

}
