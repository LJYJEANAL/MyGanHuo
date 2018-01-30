package com.ng.ganhuoapi.video;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ng.ganhuoapi.R;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.io.File;
import java.util.List;

/**
 * 带封面
 * Created by guoshuyu on 2017/9/3.
 */

public class ListVideoPlayer extends StandardGSYVideoPlayer {

    ImageView mCoverImage;

    String mCoverOriginUrl;

    int mDefaultRes;
    private int mSourcePosition = 0;
    private List<VideoResources> mUrlList;
    private ChangeClarityDialog mClarityDialog;
    private TextView clarity;
    private Context context;
    public ListVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        this.context=context;
        initView();
    }

    public ListVideoPlayer(Context context) {
        super(context);
        this.context=context;
        initView();
    }

    public ListVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        initView();
    }
    private void initView(){
        mCoverImage = (ImageView) findViewById(R.id.thumbImage);
        setShrinkImageRes(R.drawable.ic_fullscreen_exit_black_24dp);
        setEnlargeImageRes(R.drawable.ic_fullscreen_black_24dp);

        if (mIfCurrentIsFullscreen) {
            clarity = (TextView) findViewById(R.id.clarity);
            clarity.setVisibility(VISIBLE);
            //切换视频清晰度
            clarity.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSwitchDialog();
                }
            });
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.video_layout_cover;
    }

    public void loadCoverImage(String url, int res) {
        mCoverOriginUrl = url;
        mDefaultRes = res;
        Glide.with(getContext().getApplicationContext())
                .load(url)
                .into(mCoverImage);
    }

    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        ListVideoPlayer listVideoPlayer = (ListVideoPlayer) super.startWindowFullscreen(context, actionBar, statusBar);
        listVideoPlayer.loadCoverImage(mCoverOriginUrl, mDefaultRes);
        listVideoPlayer.mSourcePosition = mSourcePosition;
        listVideoPlayer.mUrlList = mUrlList;
        listVideoPlayer.clarity.setVisibility(VISIBLE);
        listVideoPlayer.clarity.setText(mUrlList.get(mSourcePosition).getName());
        return listVideoPlayer;
    }

    @Override
    protected void clearFullscreenLayout() {
        super.clearFullscreenLayout();
    }

    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param title         title
     * @return
     */


    public boolean setUp(List<VideoResources> url, boolean cacheWithPlay, String title) {
        mUrlList = url;
        return setUp(url.get(mSourcePosition).getUrl(), cacheWithPlay, title);
    }


    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
     * @param title         title
     * @return
     */
    public boolean setUp(List<VideoResources> url, boolean cacheWithPlay, File cachePath, String title) {
        mUrlList = url;
        return setUp(url.get(mSourcePosition).getUrl(), cacheWithPlay, cachePath, title);
    }
    /**
     * 弹出切换清晰度
     */
    private void showSwitchDialog() {
        if (!mHadPlay) {
            return;
        }
        mClarityDialog = new ChangeClarityDialog(getContext());
        mClarityDialog.setClarityGrade(mUrlList, mSourcePosition);
        mClarityDialog.setOnClarityCheckedListener(new ChangeClarityDialog.OnClarityChangedListener() {
            @Override
            public void onClarityChanged(int clarityIndex) {
                if (mSourcePosition != clarityIndex) {
                    if ((mCurrentState == GSYVideoPlayer.CURRENT_STATE_PLAYING
                            || mCurrentState == GSYVideoPlayer.CURRENT_STATE_PAUSE)
                            && GSYVideoManager.instance().getMediaPlayer() != null) {
                        final String url = mUrlList.get(clarityIndex).getUrl();
                        final String name = mUrlList.get(clarityIndex).getName();
                        onVideoPause();
                        final long currentPosition = getCurrentPositionWhenPlaying();
                        GSYVideoManager.instance().releaseMediaPlayer();
                        cancelProgressTimer();
                        hideAllWidget();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setUp(url, mCache, mCachePath, mTitle);
                                setSeekOnStart(currentPosition);
                                startPlayLogic();
                                cancelProgressTimer();
                                hideAllWidget();
                                clarity.setText(name);
                            }
                        }, 500);
                        mSourcePosition = clarityIndex;
                    }
                }
            }

            @Override
            public void onClarityNotChanged() {
                if (mClarityDialog != null)
                    mClarityDialog.dismiss();
            }
        });
        mClarityDialog.show();
    }
    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
        Toast.makeText(context,"资源读取失败，自动更换资源",Toast.LENGTH_SHORT).show();
        if (mUrlList!=null){
            if (mSourcePosition==0){
                mSourcePosition=1;
            }
            final String url = mUrlList.get(mSourcePosition).getUrl();
            final String name = mUrlList.get(mSourcePosition).getName();
            onVideoPause();
            final long currentPosition = getCurrentPositionWhenPlaying();
            GSYVideoManager.instance().releaseMediaPlayer();
            cancelProgressTimer();
            hideAllWidget();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setUp(url, mCache, mCachePath, mTitle);
                    setSeekOnStart(currentPosition);
                    startPlayLogic();
                    cancelProgressTimer();
                    hideAllWidget();
                    clarity.setText(name);
                }
            }, 500);
        }
    }

}
