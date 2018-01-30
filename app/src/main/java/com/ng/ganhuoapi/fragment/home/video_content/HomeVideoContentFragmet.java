package com.ng.ganhuoapi.fragment.home.video_content;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.activity.VideoDetailsActivity;
import com.ng.ganhuoapi.base.BaseFragment;
import com.ng.ganhuoapi.base.IBaseView;
import com.ng.ganhuoapi.constant.Constant;
import com.ng.ganhuoapi.data.home.HomeContentDataBean;
import com.ng.ganhuoapi.fragment.home.HomeRootFragment;
import com.ng.ganhuoapi.util.Listener;
import com.ng.ganhuoapi.util.Public;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/22.
 */

public class HomeVideoContentFragmet extends BaseFragment<HomeVideoContentPresenter, HomeContentDataBean> implements IBaseView<HomeVideoContentPresenter, HomeContentDataBean> {
    private String categoryId;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.empty_layout)
    RelativeLayout empty_error_layout;
    @BindView(R.id.tv_toast)
    TextView mTvToast;
    @BindView(R.id.rl_top_toast)
    CardView mRlTopToast;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private Unbinder unbinder;
    private boolean isLoadMore = false;
    private boolean isFirstRefresh = true;
    private LinearLayoutManager linearLayoutManager;
    private HomeVideoContentRVadapter videoContentRecyAdapter;
    private boolean isFull;
    private int start = 1;
    private HomeRootFragment homeRootFragment;


    public static HomeVideoContentFragmet newInstance(HomeRootFragment homeRootFragment, String categoryId) {
        HomeVideoContentFragmet fragment = new HomeVideoContentFragmet();
        fragment.categoryId = categoryId;
        fragment.homeRootFragment = homeRootFragment;
        return fragment;
    }


    @Override
    protected int attachLayoutId() {
        return R.layout.fragment_content_gankio;
    }


    @Override
    public void setPresenter(HomeVideoContentPresenter presenter) {
        this.presenter = new HomeVideoContentPresenter(this, categoryId);
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setColorSchemeResources(R.color.md_red, R.color.md_purple,
                R.color.md_green, R.color.md_amber);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLoadMore = false;
                presenter.doRefresh();
                isFirstRefresh = false;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView != null) {
                    recyclerView.scrollToPosition(0);
                }
            }
        });

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

//        videoContentRecyAdapter = new VideoContentRecyAdapter(getActivity(), null);
        videoContentRecyAdapter = new HomeVideoContentRVadapter(R.layout.item_home_video_recyview, null);
        recyclerView.setAdapter(videoContentRecyAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();

                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals(HomeVideoContentRVadapter.TAG)
                            && (position < firstVisibleItem || position > lastVisibleItem)) {
                        //如果滑出去了上面和下面就是否，和今日头条一样
                        if (!isFull) {
                            GSYVideoPlayer.releaseAllVideos();
                            videoContentRecyAdapter.notifyDataSetChanged();

                        }
                    }
                }
            }
        });
        videoContentRecyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StandardGSYVideoPlayer gsyVideoPlayer = (StandardGSYVideoPlayer) adapter.getViewByPosition(recyclerView, position, R.id.video_player);
                final HomeContentDataBean dataBean = (HomeContentDataBean) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), VideoDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.KEY_HOME_DATA, dataBean);
                intent.putExtras(bundle);
                intent.putExtra(Constant.KEY_FROM_HOMEVIDEO, true);
                intent.putExtra(Constant.KEY_CurrentPositionWhenPlaying, gsyVideoPlayer.getCurrentPositionWhenPlaying());
                intent.putExtra(Constant.KEY_CurrentUrl, dataBean.getVideoUrl());
                intent.putExtra(Constant.KEY_HOME_USER_IMGURL, dataBean.getUser_info().getAvatar_url());
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                        gsyVideoPlayer, "listvideoPlayer");
                startActivity(intent, options.toBundle());

            }
        });
        videoContentRecyAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (presenter != null) {
                    isLoadMore = true;
                    start++;
                    presenter.doLoadData(true, Constant.limit * start, start);
                }
            }
        });
        Public.getErrorEmpty(empty_error_layout, new Listener<Boolean, Boolean>() {
            @Override
            public void onCallBack(Boolean aBoolean, Boolean reply) {
                if (aBoolean) {
                    if (presenter != null) {
                        presenter.doRefresh();
                    }
                }
            }
        });
        homeRootFragment.addPageChangeListener(new Listener<Boolean, Integer>() {
            @Override
            public void onCallBack(Boolean aBoolean, Integer reply) {
                GSYVideoPlayer.releaseAllVideos();
            }
        });

    }

    //Fragment中全屏点击返回物理按键退出全屏在这里设置也可以 ，但需要在activity中onKeyDown返回监听设置点击
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (newConfig.orientation != ActivityInfo.SCREEN_ORIENTATION_USER) {
//            if (isFull) {
//                StandardGSYVideoPlayer.backFromWindowFull(getActivity());
//            }
            isFull = false;
        } else {
            isFull = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume();


    }


    @Override
    protected void initToolBar() {
    }

    @Override
    protected void initData() throws NullPointerException {
        presenter.doLoadData(false, Constant.limit, 0);
    }


    @Override
    public void showData(Collection<? extends HomeContentDataBean> data) {
        List<HomeContentDataBean>   dataBeanList= (List<HomeContentDataBean>) data;
        final int siaze = dataBeanList == null ? 0 : dataBeanList.size();
        if (isLoadMore) {
            videoContentRecyAdapter.addData(dataBeanList);

            showToast(videoContentRecyAdapter.getData().size(), false);
            if (siaze > 0) {
                videoContentRecyAdapter.loadMoreComplete();
            } else {
                videoContentRecyAdapter.loadMoreEnd(false);
                Snackbar snackbar = Snackbar.make(recyclerView, "no more data", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                snackbar.show();
            }
        } else {
            if (siaze > 0) {

                videoContentRecyAdapter.setNewData(dataBeanList);
                if (!isFirstRefresh) {
                    showToast(dataBeanList.size(), true);
                }

            }
        }
    }

    private void showToast(int num, boolean isRefresh) {
        if (isRefresh) {
            mTvToast.setText(String.format("已为您推荐了%1$s条新内容", num + ""));
        } else {
            mTvToast.setText(String.format("已为您加载了%1$s条内容", num + ""));
        }

        mRlTopToast.setVisibility(View.VISIBLE);
        ViewAnimator.animate(mRlTopToast)
                .newsPaper()
                .duration(1000)
                .start()
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        ViewAnimator.animate(mRlTopToast)
                                .bounceOut()
                                .duration(1000)
                                .start();

                    }
                }
                );

    }

    @Override
    public void onShowLoading() {

        if (!swipeRefresh.isRefreshing()) {
            recyclerView.setVisibility(View.VISIBLE);
            swipeRefresh.setRefreshing(true);
        }

    }

    @Override
    public void onHideLoading() {
        Public.setErrorEmptyAnim(empty_error_layout, false);
        recyclerView.setVisibility(View.VISIBLE);
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }

    }

    @Override
    public void onShowNetError(String err) {
        onHideLoading();
        Snackbar snackbar = Snackbar.make(recyclerView, err, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
        if (!isLoadMore) {
            Public.setErrorEmptyAnim(empty_error_layout, true);
            recyclerView.setVisibility(View.GONE);
        }

    }

    private Disposable disposable;

    @Override
    public void disPosable(Disposable disposable) {
        this.disposable = disposable;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
        unbinder.unbind();
        GSYVideoPlayer.releaseAllVideos();
    }
}
