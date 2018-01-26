package com.ng.ganhuoapi.activity;

import android.util.Base64;
import android.util.Log;

import com.ng.ganhuoapi.base.IBaseVideoPresenter;
import com.ng.ganhuoapi.base.IBaseView;
import com.ng.ganhuoapi.constant.Constant;
import com.ng.ganhuoapi.data.video.CommentBean;
import com.ng.ganhuoapi.data.video.VideoContentBean;
import com.ng.ganhuoapi.network.IApi;
import com.ng.ganhuoapi.network.NetManager;
import com.ng.ganhuoapi.network.OkHttpCreateHelper;
import com.ng.ganhuoapi.util.Listener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/28.
 */

public class VideoDetialsPresenter implements IBaseVideoPresenter<String> {
    private IBaseView iBaseView;
    private String videoid;
    private String group_id;
    private String formVideoTAG;
    private List<CommentBean.BeanData>data;
    private List<String> list;
    public VideoDetialsPresenter(IBaseView iBaseView) {
        this.iBaseView = iBaseView;
    }

    @Override
    public void doLoadData(final String... t) {
        iBaseView.onShowLoading();
        videoid = t[0];
        group_id = t[1];
        formVideoTAG=t[2];
        doLoadData(false, 0, 0);
        if (formVideoTAG.equals(Constant.KEY_NO_FROM_HOMEVIDEO)){

            NetManager.getInstance().getVideoResouCall(videoid, new Listener<Disposable, VideoContentBean.DataBean>() {


                @Override
                public void onCallBack(Disposable disposable, VideoContentBean.DataBean reply) {
                    iBaseView.disPosable(disposable);
                }

                @Override
                public void onSuccess(VideoContentBean.DataBean reply) {
                    if (reply != null) {
                        list = new ArrayList<>();
                        list.add(reply.getPoster_url());
                        VideoContentBean.DataBean.VideoListBean videoListBean = reply.getVideo_list();

                        if (videoListBean.getVideo_3() != null) {
                            String base64 = videoListBean.getVideo_3().getMain_url();
                            String url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
                            Log.e("信息","getVideo_3:"+url);
                            list.add(url);
                        }
                        if (videoListBean.getVideo_2() != null) {
                            String base64 = videoListBean.getVideo_2().getMain_url();
                            String url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
                            Log.e("信息","getVideo_2:"+url);
                            list.add(url);
                        }

                        if (videoListBean.getVideo_1() != null) {
                            String base64 = videoListBean.getVideo_1().getMain_url();
                            String url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
                            Log.e("信息","getVideo_1:"+url);
                            list.add(url);
                        }
                    }

                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    iBaseView.showData(list);

                }

                @Override
                public void onFailed(String reply) {

                    iBaseView.onShowNetError(reply);
                }
            });
        }

    }


    /**
     * 获取评论数据
     * @param isLoadMore
     * @param limit
     * @param start
     */
    @Override
    public void doLoadData(boolean isLoadMore, int limit, int start) {

        Observable<CommentBean> observable = OkHttpCreateHelper.createApi(IApi.class, Constant.HOMECONTENT_HOSTURL).getCommentBeanCall(group_id, limit);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommentBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CommentBean commentBean) {
                        if (commentBean != null) {
                           data=  commentBean.getData();

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("信息", "onError" + e.toString());
                        listListener.onCallBack(false,null);
                    }
                    @Override
                    public void onComplete() {
                        try{
                            if (!data.isEmpty()&&data.size()>0){
                                listListener.onCallBack(true,data);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }


    private Listener<Boolean,List<CommentBean.BeanData>> listListener;

    public void addCommentListener(Listener<Boolean, List<CommentBean.BeanData>> listListener) {
        this.listListener = listListener;
    }


    @Override
    public void doRefresh() {

    }


}
