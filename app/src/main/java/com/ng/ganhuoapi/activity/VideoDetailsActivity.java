package com.ng.ganhuoapi.activity;

import android.animation.Animator;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.activity.adapter.VideoCommentRVadapter;
import com.ng.ganhuoapi.base.IBaseActivity;
import com.ng.ganhuoapi.base.IBaseView;
import com.ng.ganhuoapi.constant.Constant;
import com.ng.ganhuoapi.custom.AppBarStateChangeListener;
import com.ng.ganhuoapi.data.home.HomeContentDataBean;
import com.ng.ganhuoapi.data.video.CommentBean;
import com.ng.ganhuoapi.util.Listener;
import com.ng.ganhuoapi.util.Public;
import com.ng.ganhuoapi.util.SettingUtil;
import com.ng.ganhuoapi.video.VideoListener;
import com.ng.ganhuoapi.video.VideoPlayer;
import com.ng.ganhuoapi.video.VideoResources;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.Disposable;


public class VideoDetailsActivity extends IBaseActivity<VideoDetialsPresenter, String> implements IBaseView<VideoDetialsPresenter, String> {

    private Unbinder unbinder;
    @BindView(R.id.video_player)
    VideoPlayer videoPlayer;
    OrientationUtils orientationUtils;
    private String title;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.coordinator_Layout)
    CoordinatorLayout root;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolBarLayout;

    private TextView titleName;
    private TextView userName;
    private TextView titleInfoTv;
    private CircleImageView avatarCircle;
    private boolean isLoadMore = false;
    private int start = 1;
    /**
     * p判断是否是首页在播放过程中过来
     */
    private String fromHomeTAG;
    /***
     * 时长
     */
    private String tv_video_time;
    private VideoCommentRVadapter commentRVadapter;
    private boolean isPlay;
    private boolean isPause;
    private int measuredHeight;
    private AppBarStateChangeListener.State curState;
    private boolean isSamll;
    private int currentPositionWhenPlaying;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!SettingUtil.getInstance().getIsDayOrNighTheme()){
            setTheme(R.style.AppThemeDark);
        }
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
                localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            }
        }
        View view = View.inflate(VideoDetailsActivity.this, R.layout.activity_video_details, null);
        super.setContentView(view);
    }


    @Override
    protected void initView(View view) {

        unbinder = ButterKnife.bind(this);
        appBar.addOnOffsetChangedListener(appBarStateChangeListener);
        if (SettingUtil.getInstance().getIsFirstTime()) {
            showTapTarget();
        }

        //网络数据还没获取到播放源fab不能点击
        fab.setEnabled(false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabPlay(videoPlayer, fab);
            }
        });

        appBar.getLayoutParams().height = (int) (Public.getScreenWidthPixels(this) * 0.7);
        //增加封面
        imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        resolveNormalVideoUI();
        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);
        //自动旋转
        videoPlayer.setRotateViewAuto(true);
        videoPlayer.setLockLand(false);
        videoPlayer.setNeedLockFull(true);
        videoPlayer.setShowFullAnimation(false);
        videoPlayer.setIsTouchWiget(true);
        videoPlayer.setSeekRatio(1);
        //设置点击屏幕播放
        videoPlayer.setThumbPlay(false);
        videoPlayer.setShowPauseCover(true);
        //设置触摸显示控制ui的消失时间  默认2500
        videoPlayer.setDismissControlTime(7000);

        addUrlListener(new Listener<Boolean, List<String>>() {
            @Override
            public void onCallBack(Boolean aBoolean, List<String> reply) {
                if (aBoolean) {
                    Glide.with(VideoDetailsActivity.this).load(reply.get(0)).asBitmap().into(imageView);
                    videoPlayer.setThumbImageView(imageView);
//                    videoPlayer.setUp(reply.get(1), false, title);
                    List<VideoResources> videoResourcesList=new ArrayList<>();
                    String [] type=new String[]{"标清 270p","高清 480p","超清 720p", "蓝光 1080p",};

                    for (int i = 0; i <type.length ; i++) {
                        videoResourcesList.add(new VideoResources(type[i],reply.get(1)));
                    }
                    videoPlayer.setUp(videoResourcesList, false, title);
                    fab.setEnabled(true);
//                    fabPlay(videoPlayer, fab);
                }
            }


        });
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                videoPlayer.startWindowFullscreen(VideoDetailsActivity.this, true, true);
            }
        });
        videoPlayer.setStandardVideoAllCallBack(new VideoListener() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                //开始播放了才能旋转和全屏
                orientationUtils.setEnable(true);
                isPlay = true;
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
            }

            @Override
            public void onClickStartError(String url, Object... objects) {
                super.onClickStartError(url, objects);
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                if (orientationUtils != null) {
                    orientationUtils.backToProtVideo();
                }
            }
        });

        videoPlayer.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });
    }

    private synchronized void fabPlay(VideoPlayer videoPlayer, FloatingActionButton fab) {
        Animator animation = ViewAnimationUtils.createCircularReveal(videoPlayer,
                videoPlayer.getWidth(),
                videoPlayer.getHeight(),
                0,
                (float) Math.hypot(videoPlayer.getWidth(), videoPlayer.getHeight()));
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        animation.setDuration(500).start();

        videoPlayer.startPlayLogic();

        fab.setVisibility(View.GONE);
        root.removeView(fab);
    }

    @Override
    public void setPresenter(VideoDetialsPresenter presenter) {

        this.presenter = new VideoDetialsPresenter(this);
        this.presenter.addCommentListener(new Listener<Boolean, List<CommentBean.BeanData>>() {
            @Override
            public void onCallBack(Boolean aBoolean, List<CommentBean.BeanData> reply) {
                if (aBoolean) {
                    //评论数据
                    final int siaze = reply == null ? 0 : reply.size();
                    if (isLoadMore) {
                        if (siaze > 0) {
                            commentRVadapter.addData(reply);
                            commentRVadapter.loadMoreComplete();
                        } else {
                            commentRVadapter.loadMoreEnd(false);
                            Snackbar snackbar = Snackbar.make(recyclerView, "no more data", Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            snackbar.show();
                        }
                    } else {
                        if (siaze > 0) {
                            commentRVadapter.setNewData(reply);
                        }
                    }

                }
            }
        });

    }

    @Override
    protected void initData() throws NullPointerException {

        HomeContentDataBean homeContentDataBean = (HomeContentDataBean) getIntent().getParcelableExtra(Constant.KEY_HOME_DATA);
        int video_duration = homeContentDataBean.getVideo_duration();
        String min = String.valueOf(video_duration / 60);
        String second = String.valueOf(video_duration % 10);
        if (Integer.parseInt(second) < 10) {
            second = "0" + second;
        }
        tv_video_time = min + ":" + second;
        title = homeContentDataBean.getTitle();
        titleInfoTv.setText("时长：" + tv_video_time + "|" + homeContentDataBean.getComment_count() + "评论");
        userName.setText(homeContentDataBean.getSource());
        titleName.setText(homeContentDataBean.getTitle());
        if (getIntent().getStringExtra(Constant.KEY_HOME_USER_IMGURL) != null) {
            Glide.with(this).load(getIntent().getStringExtra(Constant.KEY_HOME_USER_IMGURL))
                    .asBitmap()
                    .centerCrop()
                    .into(avatarCircle);
        }
        if (getIntent().getBooleanExtra(Constant.KEY_FROM_HOMEVIDEO, false)) {
            fromHomeTAG = Constant.KEY_NO_FROM_HOMEVIDEO;
            currentPositionWhenPlaying = getIntent().getIntExtra(Constant.KEY_CurrentPositionWhenPlaying, 0);
            if (currentPositionWhenPlaying > 0 & getIntent().getStringExtra(Constant.KEY_CurrentUrl) != null) {
                fromHomeTAG =Constant. KEY_IS_FROM_HOMEVIDEO;
                fab.setEnabled(true);
                if (homeContentDataBean.getVideo_detail_info().getDetail_video_large_image().getUrl() != null) {
                    String imgUrl = homeContentDataBean.getVideo_detail_info().getDetail_video_large_image().getUrl();
                    Glide.with(VideoDetailsActivity.this).load(imgUrl).asBitmap().into(imageView);
                }
                videoPlayer.setThumbImageView(imageView);

                videoPlayer.setUp(getIntent().getStringExtra(Constant.KEY_CurrentUrl), false, title);
//                videoPlayer.setSeekOnStart(currentPositionWhenPlaying);
//                videoPlayer.startPlayLogic();
//                fab.setVisibility(View.GONE);
//                root.removeView(fab);

            }
        } else {
            fromHomeTAG = Constant.KEY_NO_FROM_HOMEVIDEO;

        }
        presenter.doLoadData(homeContentDataBean.getVideo_id(), String.valueOf(homeContentDataBean.getGroup_id()), fromHomeTAG);
    }



    @Override
    protected void initToolBar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAfterTransition(VideoDetailsActivity.this);
            }
        });
    }

    private Listener<Boolean, List<String>> listListener;

    /**
     * 获取到播放源的回调监听
     *
     * @param listListener
     */
    public void addUrlListener(Listener<Boolean, List<String>> listListener) {
        this.listListener = listListener;
    }

    @Override
    public void showData(Collection<? extends String> data) {
        List<String> dataBeanList = (List<String>) data;
        if (data.size() > 1) {
            listListener.onCallBack(true, dataBeanList);
        }
    }

    @Override
    public void onShowLoading() {

    }

    @Override
    public void onHideLoading() {

    }

    @Override
    public void onShowNetError(String err) {
        onHideLoading();
        Snackbar snackbar = Snackbar.make(recyclerView, err, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();


    }

    private Disposable disposable;

    @Override
    public void disPosable(Disposable disposable) {
        this.disposable = disposable;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            videoPlayer.onConfigurationChanged(this, newConfig, orientationUtils);
        }
    }

    @Override
    public void onBackPressed() {

        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    private GSYVideoPlayer getCurPlay() {
        if (videoPlayer.getFullWindowPlayer() != null) {
            return videoPlayer.getFullWindowPlayer();
        }
        return videoPlayer;
    }


    private void resolveNormalVideoUI() {
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        videoPlayer.getBackButton().setVisibility(View.GONE);
        commentRVadapter = new VideoCommentRVadapter(R.layout.item_comment_recycleview, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentRVadapter);
        commentRVadapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isLoadMore = true;
                start++;
                presenter.doLoadData(true, Constant.limit * start, start);
            }
        });
        View headerView = getLayoutInflater().inflate(R.layout.video_detial_info, (ViewGroup) recyclerView.getParent(), false);
        titleName = (TextView) headerView.findViewById(R.id.title_tv);
        userName = (TextView) headerView.findViewById(R.id.user_name);
        titleInfoTv = (TextView) headerView.findViewById(R.id.title_info_tv);
        avatarCircle = (CircleImageView) headerView.findViewById(R.id.avatar_iv);
        commentRVadapter.addHeaderView(headerView);


    }


    /**
     * appBar滑动监听
     */
    AppBarStateChangeListener appBarStateChangeListener = new AppBarStateChangeListener() {
        @Override
        public void onStateChanged(AppBarLayout appBarLayout, AppBarStateChangeListener.State
                state) {
            if (state == AppBarStateChangeListener.State.EXPANDED) {
                //展开状态
                curState = state;
                toolBarLayout.setTitle("");
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else if (state == AppBarStateChangeListener.State.COLLAPSED) {
                //折叠状态
                //如果是小窗口就不需要处理
                toolBarLayout.setTitle(title);
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                /**
                 * 进入折叠状态的时候 如果是暂停 不显示小屏
                 */
                if (!isSamll && isPlay) {
                    if (videoPlayer.getCurrentState() == videoPlayer.CURRENT_STATE_PLAYING) {
                        int size = CommonUtil.dip2px(VideoDetailsActivity.this, (float) (Public.getScreenWidthPixels(VideoDetailsActivity.this) * 0.2));
                        videoPlayer.showSmallVideo(new Point(size, size), true, true);
                        orientationUtils.setEnable(false);
                        isSamll = true;
                    }
                }
                curState = state;
            } else {
                if (curState == AppBarStateChangeListener.State.COLLAPSED) {
                    //由折叠变为中间状态
                    getWindow().setStatusBarColor(Color.TRANSPARENT);
                    toolBarLayout.setTitle("");
                    if (isSamll) {
                        isSamll = false;
                        orientationUtils.setEnable(true);
                        //必须
                        videoPlayer.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                videoPlayer.hideSmallVideo();

                            }
                        }, 50);
                    }
                }
                //中间状态
                curState = state;

            }
        }
    };
/*    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.setVideoWindow:
                    break;
            }
            return true;
        }
    };*/

    /**
     * 引导设置
     */
    private void showTapTarget() {
        final Display display = getWindowManager().getDefaultDisplay();
        fab.post(new Runnable() {
            @Override
            public void run() {
                measuredHeight = videoPlayer.getMeasuredHeight();
                final Rect target = new Rect(
                        (int) (display.getWidth() /6.5),
                        display.getWidth() /4,
                        0,
                        0);
                final Rect target2 = new Rect(
                        (int) (display.getWidth() / 1.93),
                        0,
                        (int) (display.getWidth() * 1.28),
                        measuredHeight * 2);
                // 引导用户使用
                TapTargetSequence sequence = new TapTargetSequence(VideoDetailsActivity.this)
                        .targets(

                                TapTarget.forBounds(target, "点击这里返退出")
                                        .dimColor(android.R.color.black)
                                        .targetRadius(20)
                                        .transparentTarget(true)
                                        .drawShadow(true)
                                        .id(1),
                                TapTarget.forBounds(target2, "点击这里播放","播放按钮在这里哦")
                                        .dimColor(android.R.color.black)
                                        .outerCircleColor(R.color.colorPrimary)
                                        .targetRadius(35)
                                        .transparentTarget(true)
                                        .drawShadow(true)
                                        .id(2)

                        ).listener(new TapTargetSequence.Listener() {
                            @Override
                            public void onSequenceFinish() {
                                SettingUtil.getInstance().setIsFirstTime(false);
                            }

                            @Override
                            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                            }

                            @Override
                            public void onSequenceCanceled(TapTarget lastTarget) {
                                SettingUtil.getInstance().setIsFirstTime(false);
                            }
                        });
                sequence.start();
            }
        });

    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_set_video_style, menu);
        return true;
    }*/

    @Override
    protected void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();

        isPause = true;
    }

    @Override
    protected void onResume() {
        getCurPlay().onVideoResume();
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {

        if (disposable != null) {
            disposable.dispose();
        }

        unbinder.unbind();
        GSYVideoPlayer.releaseAllVideos();
        GSYVideoManager.clearAllDefaultCache(this);
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
        }

        super.onDestroy();

    }

}
