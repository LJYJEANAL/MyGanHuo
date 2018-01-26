package com.ng.ganhuoapi.fragment.home.video_content;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.ng.ganhuoapi.base.IBasePresenter;
import com.ng.ganhuoapi.base.IBaseView;
import com.ng.ganhuoapi.constant.Constant;
import com.ng.ganhuoapi.data.home.HomeContentBean;
import com.ng.ganhuoapi.data.home.HomeContentDataBean;
import com.ng.ganhuoapi.network.IApi;
import com.ng.ganhuoapi.network.OkHttpCreateHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/22.
 */

public class HomeVideoContentPresenter implements IBasePresenter {
    private String CatcogeId;
    private IBaseView iBaseView;
    private Gson gson;
    private String time;

    public HomeVideoContentPresenter(IBaseView iBaseView, String catcogeId) {
        this.iBaseView = iBaseView;
        this.CatcogeId = catcogeId;
        this.gson = new Gson();
        this.time = String.valueOf(System.currentTimeMillis() / 1000);
    }

    private Random random = new Random();

    private Observable<HomeContentBean> getHomeContentData(String category, int limit, String time) {

        int i = random.nextInt(10);
        if (i % 2 == 0) {
            Observable<HomeContentBean> ob1 = OkHttpCreateHelper.createApi(IApi.class, Constant.HOMECONTENT_HOSTURL).getHometContentCall(category, time, limit);
            return ob1;
        } else {
            Observable<HomeContentBean> ob2 = OkHttpCreateHelper.createApi(IApi.class, Constant.HOMECONTENT_HOSTURL2).getHometContentCall2(category, time, limit);
            return ob2;
        }
    }

    @Override
    public void doLoadData(boolean isLoadMore, int limit, int start) {
        if (!isLoadMore) {
            iBaseView.onShowLoading();
        }
        final List<HomeContentDataBean> dataList = new ArrayList<>();
        getHomeContentData(CatcogeId, 8, time)
                .throttleFirst(2, TimeUnit.SECONDS)  // g规定时间内只会响应第一次操作
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//主线程运行
                .switchMap(new Function<HomeContentBean, Observable<HomeContentDataBean>>() {
                    @Override
                    public Observable<HomeContentDataBean> apply(@NonNull HomeContentBean multiNewsArticleBean) throws Exception {
                        List<HomeContentDataBean> dataList = new ArrayList<>();
                        for (HomeContentBean.DataBean dataBean : multiNewsArticleBean.getData()) {
                            dataList.add(gson.fromJson(dataBean.getContent(), HomeContentDataBean.class));
                        }
                        return Observable.fromIterable(dataList);
                    }
                })
                .filter(new Predicate<HomeContentDataBean>() {//过滤
                    @Override
                    public boolean test(@NonNull HomeContentDataBean dataBean) throws Exception {
                        time = dataBean.getBehot_time();
                        if (TextUtils.isEmpty(dataBean.getSource())) {
                            return false;
                        }
                        try {
                            // 过滤头条问答新闻
                            if (dataBean.getSource().contains("头条问答")
                                    || dataBean.getTag().contains("ad")
                                    || dataBean.getSource().contains("话题")) {
                                return false;
                            }
                        } catch (NullPointerException e) {
//                            iBaseView.onShowNetError(e.toString());
                            Log.e("信息", "NullPointerException:" + e.toString());
                        }
                        // 过滤重复新闻(与上次刷新的数据比较)
                        for (HomeContentDataBean bean : dataList) {
                            Log.e("信息", "filter:" + bean.getTitle()+"--->"+dataBean.getTitle()+"----------->"+bean.getTitle().equals(dataBean.getTitle()));
                            if (bean.getTitle().equals(dataBean.getTitle())) {
                                return false;
                            }
                        }
                        return true;
                    }
                })
                .toList()
                .subscribe(new Consumer<List<HomeContentDataBean>>() {//订阅
                    @Override
                    public void accept(@NonNull List<HomeContentDataBean> list) throws Exception {
                        if (list.size() > 0 & list != null) {
                            iBaseView.showData(list);
                            iBaseView.onHideLoading();
                        } else {
                            iBaseView.onShowNetError("服务器故障");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        iBaseView.onShowNetError(throwable.toString());
                    }
                });


    }




    @Override
    public void doRefresh() {
        doLoadData(false, Constant.limit, Constant.start);
    }



}
