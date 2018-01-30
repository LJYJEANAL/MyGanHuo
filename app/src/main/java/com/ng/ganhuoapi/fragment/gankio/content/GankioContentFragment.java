package com.ng.ganhuoapi.fragment.gankio.content;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.activity.GanWebDetailsActivity;
import com.ng.ganhuoapi.activity.PictureDetailsActivity;
import com.ng.ganhuoapi.base.BaseFragment;
import com.ng.ganhuoapi.base.IBaseView;
import com.ng.ganhuoapi.constant.Constant;
import com.ng.ganhuoapi.data.gankio.GankioInfo;
import com.ng.ganhuoapi.util.Listener;
import com.ng.ganhuoapi.util.Public;

import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/20.
 */

public class GankioContentFragment extends BaseFragment<GankioContentPresenter, GankioInfo> implements IBaseView<GankioContentPresenter, GankioInfo> {
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.empty_layout)
    RelativeLayout empty_error_layout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    /**
     * 是否加载更多
     */
    private boolean isLoadMore = false;
    /**
     * 水管
     */
    private Disposable disposable;
    /**
     * 第一页
     */
    private int start = 1;
    /**
     * 类型
     */
    private String type;
    private GankioContentRVadapter mGankIoCustomAdapter;
    private Unbinder unbinder;

    public static GankioContentFragment newInstance(String type) {
        GankioContentFragment fragment = new GankioContentFragment();
        fragment.type = type;
        return fragment;
    }

    @Override
    protected int attachLayoutId() {
        return R.layout.fragment_content_gankio;
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
        mGankIoCustomAdapter = new GankioContentRVadapter(null);
        recyclerView.setAdapter(mGankIoCustomAdapter);
        if (type=="福利") {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mGankIoCustomAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                GankioInfo gankioInfo = (GankioInfo) adapter.getItem(position);
                if (!(gankioInfo.getType()=="福利")) {
                    Intent intent = new Intent(getActivity(), GanWebDetailsActivity.class);
                    intent.putExtra(Constant.WEB_VIEW_LOAD_URL, (gankioInfo.getUrl()));
                    intent.putExtra(Constant.WEB_VIEW_LOAD_TITLE, (gankioInfo.getDesc()));
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                }


            }
        });
        mGankIoCustomAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                GankioInfo gankioInfo = (GankioInfo) adapter.getItem(position);
                if (!(gankioInfo.getType()=="福利")) {
                    Intent intent = new Intent(getActivity(), PictureDetailsActivity.class);
                    intent.putExtra(Constant.key_BITMAP_TRANSTION, gankioInfo.getUrl());
                    intent.putExtra(Constant.key_BITMAP_WHO, gankioInfo.getWho());
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                            view, "picturedetails");
                    startActivity(intent, options.toBundle());
                }
            }
        });
        mGankIoCustomAdapter.setEnableLoadMore(true);
        mGankIoCustomAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isLoadMore = true;
                start++;
                presenter.doLoadData(true, Constant.limit, start);
            }
        });
        Public.getErrorEmpty(empty_error_layout, new Listener<Boolean, Boolean>() {
            @Override
            public void onCallBack(Boolean aBoolean, Boolean reply) {
                if (aBoolean){
                    if (presenter!=null){
                        presenter.doRefresh();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (disposable!=null){
            disposable.dispose();
        }
    }

    @Override
    protected void initToolBar() {
    }


    @Override
    protected void initData() throws NullPointerException {
        presenter.doLoadData(false, Constant.limit, Constant.start);

    }


    @Override
    public void setPresenter(GankioContentPresenter presenter) {
        this.presenter = new GankioContentPresenter(this, type);

    }

    @Override
    public void disPosable(Disposable disposable) {
        this.disposable = disposable;
    }

    @Override
    public void showData(Collection<? extends GankioInfo> data) {
        List<GankioInfo> gankioInfoList = (List<GankioInfo>) data;
        final int siaze = gankioInfoList == null ? 0 : gankioInfoList.size();

        if (isLoadMore) {
            mGankIoCustomAdapter.addData(gankioInfoList);
            if (siaze > 0) {
                mGankIoCustomAdapter.loadMoreComplete();
            } else {
                mGankIoCustomAdapter.loadMoreEnd(false);
                Toast.makeText(getActivity(), "no more data", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (siaze > 0) {
                mGankIoCustomAdapter.setNewData(gankioInfoList);
            }
        }
    }


    @Override
    public void onShowLoading() {
        if (!swipeRefresh.isRefreshing()) {
            empty_error_layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            swipeRefresh.setRefreshing(true);
        }

    }

    @Override
    public void onHideLoading() {
        Public. setErrorEmptyAnim(empty_error_layout,false);
        recyclerView.setVisibility(View.VISIBLE);
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }

    }

    @Override
    public void onShowNetError(String err) {
        onHideLoading();
        Snackbar snackbar=Snackbar.make(recyclerView,err,Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
        if (!isLoadMore) {
            Public. setErrorEmptyAnim(empty_error_layout,true);
            recyclerView.setVisibility(View.GONE);
        }
        if (isLoadMore) {
            mGankIoCustomAdapter.loadMoreFail();
        }
    }


}
