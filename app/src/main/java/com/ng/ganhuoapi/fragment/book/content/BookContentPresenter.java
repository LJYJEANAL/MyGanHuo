package com.ng.ganhuoapi.fragment.book.content;

import com.ng.ganhuoapi.base.IBasePresenter;
import com.ng.ganhuoapi.base.IBaseView;
import com.ng.ganhuoapi.constant.Constant;
import com.ng.ganhuoapi.data.book.BookItemBean;
import com.ng.ganhuoapi.network.NetManager;
import com.ng.ganhuoapi.util.Listener;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/25.
 */

public class BookContentPresenter implements IBasePresenter {
    private IBaseView iBaseView;
    private String type;
    public BookContentPresenter(IBaseView iBaseView,String type) {
        this.iBaseView=iBaseView;
        this.type=type;
    }

    @Override
    public void doLoadData(boolean isLoadMore, int limit, int start) {
        if (!isLoadMore){
            iBaseView.onShowLoading();
        }

        NetManager.getInstance().getBookListCall(type,start, limit, new Listener<Disposable, List<BookItemBean>>() {
            @Override
            public void onSuccess(List<BookItemBean> reply) {
                iBaseView.showData(reply);
            }

            @Override
            public void onComplete() {
                iBaseView.onHideLoading();
            }

            @Override
            public void onCallBack(Disposable disposable, List<BookItemBean> reply) {


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
        doLoadData(false, Constant.limit,0);

    }
}
