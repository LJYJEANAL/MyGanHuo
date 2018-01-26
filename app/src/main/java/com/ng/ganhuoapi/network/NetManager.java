package com.ng.ganhuoapi.network;

import android.util.Log;

import com.google.gson.Gson;
import com.ng.ganhuoapi.constant.Constant;
import com.ng.ganhuoapi.data.book.BookItemBean;
import com.ng.ganhuoapi.data.book.BookListBean;
import com.ng.ganhuoapi.data.gankio.Gankio;
import com.ng.ganhuoapi.data.gankio.GankioInfo;
import com.ng.ganhuoapi.data.home.HomeContentBean;
import com.ng.ganhuoapi.data.home.HomeContentDataBean;
import com.ng.ganhuoapi.data.movie.MovieContentBean;
import com.ng.ganhuoapi.data.movie.SubItemsBean;
import com.ng.ganhuoapi.data.movie.usboxmovie.SubjectsBean;
import com.ng.ganhuoapi.data.movie.usboxmovie.UsBoxMoveBean;
import com.ng.ganhuoapi.data.video.VideoContentBean;
import com.ng.ganhuoapi.util.Listener;
import com.ng.ganhuoapi.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.CRC32;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/20.
 */

public class NetManager {
    private static NetManager netManager;
    private static Gson gson;
    private String time;

    /**
     * 双重检查锁模式
     *
     * @return
     */
    public static NetManager getInstance() {
        if (netManager == null) {
            synchronized (NetManager.class) {
                if (netManager == null) {
                    netManager = new NetManager();
                    gson = new Gson();
                }
            }

        }
        return netManager;
    }

    /**
     * 干货数据获取
     *
     * @param type
     * @param limit
     * @param start
     * @param listener
     */
    public void getGankIoCall(String type, int limit, int start, final Listener<Disposable, List<GankioInfo>> listener) {
        Observable<Gankio> observable = OkHttpCreateHelper.createApi(IApi.class, Constant.GANKIO_HOSTURL).getGanKioCall(type, limit, start);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//主线程运行
                .subscribe(new Observer<Gankio>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.onCallBack(d, null);
                    }

                    @Override
                    public void onNext(Gankio gankio) {
                        if (gankio != null) {
                            List<GankioInfo> results = gankio.getResults();
                            listener.onSuccess(results);
                        } else {
                            listener.onFailed("服务器故障");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFailed(e.toString());
                        Log.e("信息", "onError:" + e.toString());

                    }

                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });
    }

    private Random random = new Random();

    private Observable<HomeContentBean> getHomeContentData(String category, int limit,String time) {

        int i = random.nextInt(10);
        if (i % 2 == 0) {
            Observable<HomeContentBean> ob1 = OkHttpCreateHelper.createApi(IApi.class, Constant.HOMECONTENT_HOSTURL).getHometContentCall(category, time, limit);
            return ob1;
        } else {
            Observable<HomeContentBean> ob2 = OkHttpCreateHelper.createApi(IApi.class, Constant.HOMECONTENT_HOSTURL2).getHometContentCall2(category, time, limit);
            return ob2;
        }
    }

    /**
     * 首页数据获取
     *
     * @param category
     * @param limit
     * @param listener
     */

    public void getHometContentCall(String category, int limit, String time,final Listener<Disposable, List<HomeContentDataBean>> listener) {
        getHomeContentData(category, limit,time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//主线程运行

                .subscribe(new Observer<HomeContentBean>() {

                    private List<HomeContentDataBean> oldDataList;

                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.onCallBack(d, null);
                    }

                    @Override
                    public void onNext(HomeContentBean homeContentBean) {
                        if (homeContentBean != null) {
                            List<HomeContentDataBean> dataBeanList = new ArrayList<>();

                            for (HomeContentBean.DataBean dataBean : homeContentBean.getData()) {
                                dataBeanList.add(gson.fromJson(dataBean.getContent(), HomeContentDataBean.class));

                            }
                            if (dataBeanList.size() > 0) {
                                listener.onSuccess(dataBeanList);
                            } else {
                                listener.onFailed("服务器故障");
                            }

                        } else {
                            listener.onFailed("服务器故障");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFailed(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                })
        ;

    }

    public void getHometContentCall2(String category, int limit,final String time,final Listener<Disposable, List<HomeContentDataBean>> listener) {
      /*  final List<HomeContentDataBean> dataList = new ArrayList<>();
        getHomeContentData(category, limit,time)
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
                .filter(new Predicate<HomeContentDataBean>() {
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
//                            ErrorAction.print(e);
                            listener.onFailed(e.toString());
                        }
                        // 过滤重复新闻(与上次刷新的数据比较)
                        for (HomeContentDataBean bean : dataList) {
                            if (bean.getTitle().equals(dataBean.getTitle())) {
                                return false;
                            }
                        }
                        return true;
                    }
                })
                .toList()

                .subscribe(new Consumer<List<HomeContentDataBean>>() {
                    @Override
                    public void accept(@NonNull List<HomeContentDataBean> list) throws Exception {
                        for (int i = 0; i < list.size(); i++) {
                            Log.i("信息",list.get(i).getAbstractX());
                        }
                        if (list.size() > 0 & list != null) {
                            listener.onSuccess(list);
                            listener.onComplete();
                        } else {
                            listener.onFailed("服务器故障");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        doShowNetError();
//                        ErrorAction.print(throwable);
                        Log.i("信息", "-accept--》" + throwable.toString());
                        listener.onFailed(throwable.toString());
                    }
                });
*/

    }


    private String getVideoContentApi(String videoid) {
        String VIDEO_HOST = "http://ib.365yg.com";
        String VIDEO_URL = "/video/urls/v/1/toutiao/mp4/%s?r=%s";
        String r = getRandom();
        String s = String.format(VIDEO_URL, videoid, r);
        // 将/video/urls/v/1/toutiao/mp4/{videoid}?r={Math.random()} 进行crc32加密
        CRC32 crc32 = new CRC32();
        crc32.update(s.getBytes());
        String crcString = crc32.getValue() + "";
        String url = VIDEO_HOST + s + "&s=" + crcString;
        return url;
    }

    private static String getRandom() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }
    /**
     * 根据videoId 获取视频源
     *
     * @param videoId
     */
    public void getVideoResouCall(String videoId, final Listener<Disposable, VideoContentBean.DataBean> listener) {

        String url = getVideoContentApi(videoId);
        OkHttpCreateHelper.createApi(IApi.class, Constant.TOUTIAO_HOSTURL)
                .getVideoResouCall(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoContentBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.onCallBack(d, null);
                    }

                    @Override
                    public void onNext(VideoContentBean videoContentBean) {
                        if (videoContentBean != null) {
                            VideoContentBean.DataBean dataBean = videoContentBean.getData();
                            if (dataBean != null) {
                                listener.onSuccess(dataBean);
                            } else {
                                listener.onFailed("服务器故障");
                            }
                        } else {
                            listener.onFailed("服务器故障");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("信息", "onError");
                        listener.onFailed(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });

    }

    public void getBookListCall(String tag, int start, int count, final Listener<Disposable, List<BookItemBean>> listener) {
        OkHttpCreateHelper.createApi(IApi.class, Constant.DOUBAN_HOSTURL)
                .getBookListCall(tag, start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.onCallBack(d, null);
                    }

                    @Override
                    public void onNext(BookListBean bookListBean) {
                        if (bookListBean != null) {
                            List<BookItemBean> dataList = new ArrayList<>();
                            for (BookItemBean bookitemBean : bookListBean.getBooks()) {
                                dataList.add(bookitemBean);
                            }

                            if (dataList.size() > 0) {
                                listener.onSuccess(dataList);

                            } else {
                                listener.onFailed("服务器故障");
                            }

                        } else {
                            listener.onFailed("服务器故障");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFailed(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });

    }

    public synchronized void getMovieContentData(final int position, int start, int count, final Listener<Disposable, List<SubItemsBean>> listener) {
        IApi iApi = OkHttpCreateHelper.createApi(IApi.class, Constant.DOUBAN_HOSTURL);
        if (position == 3) {
            getUSBoxMovieCall(listener);
        } else {
            getMovieContentCall(iApi, position, start, count, listener);
        }
    }

    private synchronized void getMovieContentCall(IApi iApi, final int position, int start, int count, final Listener<Disposable, List<SubItemsBean>> listener) {


        if (iApi != null) {
            Observable<MovieContentBean>[] observableArray = new Observable[]{
                    iApi.getHotMovieCall(),
                    iApi.getCommingSoonMovieCall(),
                    iApi.getTop250MovieCall(start, count)
            };
            Observable<MovieContentBean> observables = observableArray[position];
            observables.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<MovieContentBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(MovieContentBean movieContentBean) {
                            if (movieContentBean != null) {
                                List<SubItemsBean> dataList = new ArrayList<>();

                                for (SubItemsBean subItemsBean : movieContentBean.getSubjects()) {

                                    if (position == 0) {
                                        subItemsBean.setItemType(1);
                                    } else {
                                        subItemsBean.setItemType(2);
                                    }
                                    dataList.add(subItemsBean);

                                }
                                listener.onSuccess(dataList);

                            } else {
                                listener.onFailed("服务器故障");
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            listener.onFailed(e.toString());
                        }

                        @Override
                        public void onComplete() {
                            listener.onComplete();
                        }
                    });


        }

    }

    private synchronized void getUSBoxMovieCall(final Listener<Disposable, List<SubItemsBean>> listener) {
        Observable<UsBoxMoveBean> observable = OkHttpCreateHelper.createApi(IApi.class, Constant.DOUBAN_HOSTURL).getUsBoxMovieCall();
        if (observable != null) {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<UsBoxMoveBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            listener.onCallBack(d, null);
                        }

                        @Override
                        public void onNext(UsBoxMoveBean usBoxMoveBean) {
                            if (usBoxMoveBean != null) {
                                List<SubItemsBean> dataList = new ArrayList<>();
                                for (SubjectsBean subjectsBean : usBoxMoveBean.getSubjects()) {
                                    SubItemsBean subItemsBean = subjectsBean.getSubject();
                                    subItemsBean.setItemType(1);
                                    dataList.add(subItemsBean);
                                }
                                listener.onSuccess(dataList);


                            } else {
                                listener.onFailed("服务器故障");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtils.e("onError：" + e.toString());
                            listener.onFailed(e.toString());
                        }

                        @Override
                        public void onComplete() {
                            listener.onComplete();
                        }
                    });
        }
    }

}
