package com.ng.ganhuoapi.video;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.util.Public;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.io.File;
import java.util.List;

public class VideoPlayer extends StandardGSYVideoPlayer {
    private int mSourcePosition = 1;
    private List<VideoResources> mUrlList;
    private ChangeClarityDialog mClarityDialog;
    private TextView clarity;

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public VideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        this.context = context;
        initView();
    }


    public VideoPlayer(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    //这个必须配置最上面的构造才能生效
    @Override
    public int getLayoutId() {
        if (mIfCurrentIsFullscreen) {
            return R.layout.live_video_land;
        }
        return R.layout.video_layout_normal;
    }

    private Context context;


    private void initView() {

        int w = Public.getScreenWidthPixels(context);
        int h = Public.getScreenHeigthtPixels(context);
        int width = w > h ? h : w;
        if (mIfCurrentIsFullscreen) {
            getFullscreenButton().getLayoutParams().height = (int) (h * 0.1);
            getFullscreenButton().getLayoutParams().width = (int) (h * 0.1);
            clarity = (TextView) findViewById(R.id.clarity);
            //切换视频清晰度
            clarity.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSwitchDialog();
                }
            });
        } else {
            getFullscreenButton().getLayoutParams().height = (int) (w * 0.1);
            getFullscreenButton().getLayoutParams().width = (int) (w * 0.1);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTopContainer.getLayoutParams();
            layoutParams.setMargins(0, (int) (Public.getStatusBarHeight(context)), 0, 0);
            mTopContainer.setEnabled(true);

        }

        setShrinkImageRes(R.drawable.ic_fullscreen_exit_black_24dp);
        setEnlargeImageRes(R.drawable.ic_fullscreen_black_24dp);

    }


    @Override
    protected void updateStartImage() {
        if (mIfCurrentIsFullscreen) {
            if (mStartButton instanceof ImageView) {
                ImageView imageView = (ImageView) mStartButton;
                if (mCurrentState == CURRENT_STATE_PLAYING) {
                    imageView.setImageResource(R.drawable.video_click_pause_selector);
                } else if (mCurrentState == CURRENT_STATE_ERROR) {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                } else {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                }
            }
        } else {
            super.updateStartImage();
        }
    }

    private boolean isFirst = true;

    @Override
    protected void changeUiToNormal() {
        super.changeUiToNormal();
        if (isFirst) {
            getStartButton().setVisibility(GONE);
            isFirst = false;
        }
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
     * 必须设置
     * @param context
     * @param actionBar
     * @param statusBar
     * @return
     */
    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        VideoPlayer videoPlayer = (VideoPlayer) super.startWindowFullscreen(context, actionBar, statusBar);
        videoPlayer.mSourcePosition = mSourcePosition;
        videoPlayer.mUrlList = mUrlList;
        videoPlayer.clarity.setText(mUrlList.get(mSourcePosition).getName());
        return videoPlayer;
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
