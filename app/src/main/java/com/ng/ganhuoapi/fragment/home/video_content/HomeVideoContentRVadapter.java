package com.ng.ganhuoapi.fragment.home.video_content;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.custom.CustomAnimation;
import com.ng.ganhuoapi.data.home.HomeContentDataBean;
import com.ng.ganhuoapi.data.video.VideoContentBean;
import com.ng.ganhuoapi.network.NetManager;
import com.ng.ganhuoapi.util.Listener;
import com.ng.ganhuoapi.util.Public;
import com.ng.ganhuoapi.util.TimeUtil;
import com.ng.ganhuoapi.video.ListVideoPlayer;
import com.ng.ganhuoapi.video.VideoListener;
import com.ng.ganhuoapi.video.VideoResources;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/1/12.
 */

public class HomeVideoContentRVadapter extends BaseQuickAdapter<HomeContentDataBean, BaseViewHolder> {
    public final static String TAG = "VideoContentRecyAdapter";
    private ImageView imageView;
    private int width;

    public HomeVideoContentRVadapter(int width, int layoutResId, @Nullable List<HomeContentDataBean> data) {
        super(layoutResId, data);
        this.width = width;
        setEnableLoadMore(true);
        openLoadAnimation(new CustomAnimation());

    }

    @Override
    protected void convert(final BaseViewHolder helper, final HomeContentDataBean item) {
        if (item != null) {
            imageView = new ImageView(mContext);

            helper.setText(R.id.title_tv, item.getTitle());
            if (item.getVideo_detail_info() != null) {
                String video_image = item.getVideo_detail_info().getDetail_video_large_image().getUrl();
                imageView.setImageResource(Public.getNodataLoadingId(mContext));
                Glide.with(mContext).load(video_image)
                        .asBitmap()
                        .centerCrop()
                        .into(imageView);
            }
            if (null != item.getUser_info()) {
                String avatar_url = item.getUser_info().getAvatar_url();
                if (!TextUtils.isEmpty(avatar_url)) {
                    Glide.with(mContext).load(avatar_url)
                            .asBitmap()
                            .centerCrop()
                            .into((CircleImageView) helper.getView(R.id.avatar_iv));
                }
            }
            helper.setText(R.id.title_tv, item.getTitle());
            String tv_source = item.getSource();
            String tv_comment_count = item.getComment_count() + "评论";
            String tv_datetime = item.getBehot_time() + "";
            if (!TextUtils.isEmpty(tv_datetime)) {
                tv_datetime = TimeUtil.getTimeStampAgo(tv_datetime);
            }
            int video_duration = item.getVideo_duration();
            String min = String.valueOf(video_duration / 60);
            String second = String.valueOf(Math.round(video_duration % 10));
            if (Integer.parseInt(second) < 10) {
                second = "0" + second;
            }
            String tv_video_time = min + ":" + second;
            helper.setText(R.id.user_name, tv_source + " - " + tv_comment_count + " - " + tv_datetime);
            helper.setText(R.id.tv_video_time, tv_video_time);
            if (helper.getView(R.id.tv_video_time).getVisibility() == View.GONE) {
                helper.getView(R.id.tv_video_time).setVisibility(View.VISIBLE);
            }

            RelativeLayout videoLaytou = helper.getView(R.id.video_layout);
            videoLaytou.getLayoutParams().height = (int) (width * 0.56);
            final ListVideoPlayer gsyVideoPlayer = helper.getView(R.id.video_player);
            gsyVideoPlayer.setIsTouchWiget(false);
            gsyVideoPlayer.setThumbImageView(imageView);
            gsyVideoPlayer.setRotateViewAuto(true);
            gsyVideoPlayer.setLockLand(true);
            gsyVideoPlayer.setIsTouchWiget(true);
            gsyVideoPlayer.setThumbPlay(true);
            gsyVideoPlayer.setPlayTag(TAG);
            gsyVideoPlayer.setShowFullAnimation(false);
            gsyVideoPlayer.setNeedLockFull(true);
            gsyVideoPlayer.setPlayPosition(helper.getPosition());
            //增加title
            gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
            gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resolveFullBtn(gsyVideoPlayer);
                }
            });
            //设置返回键
            gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
            getVideoResoue(item.getVideo_id(), new Listener<Boolean, List<String>>() {
                @Override
                public void onCallBack(Boolean aBoolean, List<String> reply) {
                    if (aBoolean) {
//                        item.setVideoUrl(reply.get(1));
//                        gsyVideoPlayer.setUp(reply.get(1), false, item.getTitle());

                        List<VideoResources> videoResourcesList = new ArrayList<>();
                        String[] type = new String[]{"标清 270p", "高清 480p", "超清 720p", "蓝光 1080p",};
                        for (int i = 0; i < reply.size(); i++) {
                            videoResourcesList.add(new VideoResources(type[i], reply.get(i)));
                        }
                        gsyVideoPlayer.setUp(videoResourcesList, false, item.getTitle());
                    }

                }
            });
            gsyVideoPlayer.setStandardVideoAllCallBack(new VideoListener() {
                @Override
                public void onPrepared(String url, Object... objects) {
                    super.onPrepared(url, objects);
                }

                @Override
                public void onClickStartIcon(String url, Object... objects) {
                    super.onClickStartIcon(url, objects);
                    helper.getView(R.id.tv_video_time).setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(mContext, true, true);
    }

    private void getVideoResoue(final String video_id, final Listener<Boolean, List<String>> listener) {
        final List<String> list = new ArrayList<>();
        NetManager.getInstance().getVideoResouCall(video_id, new Listener<Disposable, VideoContentBean.DataBean>() {
            @Override
            public void onCallBack(Disposable disposable, VideoContentBean.DataBean reply) {
            }

            @Override
            public void onSuccess(VideoContentBean.DataBean reply) {
                if (reply != null) {
                    VideoContentBean.DataBean.VideoListBean videoListBean = reply.getVideo_list();
                    if (videoListBean.getVideo_3() != null) {
                        String base64 = videoListBean.getVideo_3().getMain_url();
                        String url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
                        list.add(url);
                    }
                    if (videoListBean.getVideo_2() != null) {
                        String base64 = videoListBean.getVideo_2().getMain_url();
                        String url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
                        list.add(url);
                    }
                    if (videoListBean.getVideo_1() != null) {
                        String base64 = videoListBean.getVideo_1().getMain_url();
                        String url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
                        list.add(url);
                    }

                }
            }

            @Override
            public void onComplete() {
                super.onComplete();
                listener.onCallBack(true, list);
            }

            @Override
            public void onFailed(String reply) {
            }
        });

    }

}