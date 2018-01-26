package com.ng.ganhuoapi.fragment.home.video_content;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.data.home.HomeContentDataBean;
import com.ng.ganhuoapi.data.video.VideoContentBean;
import com.ng.ganhuoapi.network.NetManager;
import com.ng.ganhuoapi.util.Listener;
import com.ng.ganhuoapi.util.Public;
import com.ng.ganhuoapi.util.TimeUtil;
import com.ng.ganhuoapi.video.ListVideoUtil;
import com.ng.ganhuoapi.video.VideoListener;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/1/11.
 */

public class VideoContentRecyAdapter extends RecyclerView.Adapter<VideoContentRecyAdapter.ViewHolder> implements View.OnClickListener {
    private List<HomeContentDataBean> data;
    private Context context;
    private  ListVideoUtil listVideoUtil;
    public final static String TAG = "VideoContentRecyAdapter";
    private  GSYVideoOptionBuilder gsyVideoOptionBuilder;
    private StandardGSYVideoPlayer gsyVideoPlayer;
    private String CurrentPositionWhenPlaying;
    public VideoContentRecyAdapter(Context context, List<HomeContentDataBean> data) {
        this.data = data;
        this.context = context;
        gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
    }

    public String getCurrentPositionWhenPlaying() {
        return CurrentPositionWhenPlaying;
    }

    public void setCurrentPositionWhenPlaying(String currentPositionWhenPlaying) {
        CurrentPositionWhenPlaying = currentPositionWhenPlaying;
    }

    public StandardGSYVideoPlayer getGsyVideoPlayer() {
        return gsyVideoPlayer;
    }

    public void setGsyVideoPlayer(StandardGSYVideoPlayer gsyVideoPlayer) {
        this.gsyVideoPlayer = gsyVideoPlayer;
    }
    public HomeContentDataBean getItem(int position) {
        return data.get(position);
    }
    public void setListVideoUtil(ListVideoUtil listVideoUtil) {
        this.listVideoUtil = listVideoUtil;
    }
    public void setData(List<HomeContentDataBean> data) {
        if (data != null) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    public void addData(List<HomeContentDataBean> data) {
        if (data != null) {
            data.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public VideoContentRecyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_home_video_recyview, parent, false);
        itemView.setOnClickListener(this);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoContentRecyAdapter.ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        final HomeContentDataBean dataBean = data.get(position);
        if (dataBean.getVideo_detail_info() != null) {
            String video_image = dataBean.getVideo_detail_info().getDetail_video_large_image().getUrl();

            if (!TextUtils.isEmpty(video_image))
                holder.imageView.setImageResource(Public.getNodataLoadingId(context));
            Glide.with(context).load(video_image)
                    .asBitmap()
                    .centerCrop()
                    .into(holder.imageView);
        }
        if (null != dataBean.getUser_info()) {
            String avatar_url = dataBean.getUser_info().getAvatar_url();
            if (!TextUtils.isEmpty(avatar_url)) {
                Glide.with(context).load(avatar_url)
                        .asBitmap()
                        .centerCrop()
                        .into(holder.avatarIcon);
            }
        }
        holder.titleName.setText(dataBean.getTitle());
        String tv_source = dataBean.getSource();
        String tv_comment_count = dataBean.getComment_count() + "评论";
        String tv_datetime = dataBean.getBehot_time() + "";
        if (!TextUtils.isEmpty(tv_datetime)) {
            tv_datetime = TimeUtil.getTimeStampAgo(tv_datetime);
        }
        int video_duration = dataBean.getVideo_duration();
        String min = String.valueOf(video_duration / 60);
        String second = String.valueOf(Math.round(video_duration % 10));
        if (Integer.parseInt(second) < 10) {
            second = "0" + second;
        }
        String tv_video_time = min + ":" + second;
        holder.userName.setText(tv_source + " - " + tv_comment_count + " - " + tv_datetime);
        holder.videoTime.setText(tv_video_time);


//        String url = getVideoResoue(dataBean.getGroup_id());
//        gsyVideoOptionBuilder
//                .setIsTouchWiget(false)
//                .setThumbImageView(holder.imageView)
//                .setUrl(url)
//                .setVideoTitle(dataBean.getTitle())
//                .setCacheWithPlay(true)
//                .setRotateViewAuto(true)
//                .setLockLand(true)
//                .setPlayTag(TAG)
//                .setShowFullAnimation(true)
//                .setNeedLockFull(true)
//                .setPlayPosition(position)
//                .setStandardVideoAllCallBack(new VideoListener() {
//                    @Override
//                    public void onPrepared(String url, Object... objects) {
//                        super.onPrepared(url, objects);
//                        if (!holder.gsyVideoPlayer.isIfCurrentIsFullscreen()) {
//                            holder.videoTime.setVisibility(View.GONE);
//                        }
//
//                    }
//
//                    @Override
//                    public void onQuitFullscreen(String url, Object... objects) {
//                        super.onQuitFullscreen(url, objects);
//                    }
//
//                    @Override
//                    public void onEnterFullscreen(String url, Object... objects) {
//                        super.onEnterFullscreen(url, objects);
//                    }
//                }).build(holder.gsyVideoPlayer);

        holder.gsyVideoPlayer.setIsTouchWiget(false);
        holder.gsyVideoPlayer.setThumbImageView(holder.imageView);
        holder.gsyVideoPlayer.setRotateViewAuto(true);
        holder.gsyVideoPlayer.setLockLand(true);
        holder.gsyVideoPlayer.setPlayTag(TAG);
        holder.gsyVideoPlayer.setShowFullAnimation(true);
        holder.gsyVideoPlayer.setNeedLockFull(true);
        holder.gsyVideoPlayer.setPlayPosition(position);
        //增加title
        holder.gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);

        //设置返回键
        holder.gsyVideoPlayer.getBackButton().setVisibility(View.GONE);

        //设置全屏按键功能
        holder.gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(holder.gsyVideoPlayer);
            }
        });
        getVideoResoue(dataBean.getVideo_id(), new Listener<Boolean, String>() {
            @Override
            public void onCallBack(Boolean aBoolean, String reply) {
                if (aBoolean) {
                    Log.e("信息","*---"+reply);
                    dataBean.setVideoUrl(reply);

                    holder.gsyVideoPlayer.setUp(reply, false, dataBean.getTitle());
                    holder.gsyVideoPlayer.setTag(reply);
                    setGsyVideoPlayer( holder.gsyVideoPlayer);
                }

            }
        });
        holder.gsyVideoPlayer.setStandardVideoAllCallBack(new VideoListener(){
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                holder.videoTime.setVisibility(View.GONE);
            }

            @Override
            public void onClickStartIcon(String url, Object... objects) {
                super.onClickStartIcon(url, objects);
//                StandardGSYVideoPlayer standardGSYVideoPlayers= (StandardGSYVideoPlayer) objects[1];
                setGsyVideoPlayer((StandardGSYVideoPlayer) objects[1]);
            }
        });
//        holder.gsyVideoPlayer.setTag(holder.gsyVideoPlayer.getCurrentPositionWhenPlaying());
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(context, true, true);
    }

    @Override
    public int getItemCount() {

        if (data != null) {
            return data.size();
        }
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private OnItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, this, (int) v.getTag());
        }
    }


    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, VideoContentRecyAdapter adapter, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.video_player)
        StandardGSYVideoPlayer gsyVideoPlayer;
        @BindView(R.id.title_tv)
        TextView titleName;
        @BindView(R.id.avatar_iv)
        CircleImageView avatarIcon;
        @BindView(R.id.user_name)
        TextView userName;
        @BindView(R.id.tv_video_time)
        TextView videoTime;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageView = new ImageView(context);
        }
    }

    //    private List<String> list;


    private void getVideoResoue(final String video_id, final Listener<Boolean, String> listener) {

        NetManager.getInstance().getVideoResouCall(video_id, new Listener<Disposable, VideoContentBean.DataBean>() {
            @Override
            public void onCallBack(Disposable disposable, VideoContentBean.DataBean reply) {
            }

            @Override
            public void onSuccess(VideoContentBean.DataBean reply) {
                if (reply != null) {
                    VideoContentBean.DataBean.VideoListBean videoListBean = reply.getVideo_list();
                    String url = null;
                    if (videoListBean.getVideo_3() != null) {
                        String base64 = videoListBean.getVideo_3().getMain_url();
                        url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
                    } else if (videoListBean.getVideo_2() != null) {
                        String base64 = videoListBean.getVideo_2().getMain_url();
                        url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
                    } else if (videoListBean.getVideo_1() != null) {
                        String base64 = videoListBean.getVideo_1().getMain_url();
                        url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
                    }
                    listener.onCallBack(true, url);
                }
            }

            @Override
            public void onComplete() {
                super.onComplete();
            }

            @Override
            public void onFailed(String reply) {
            }
        });

    }


}
