package com.ng.ganhuoapi.fragment.home.content;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.activity.VideoDetailsActivity;
import com.ng.ganhuoapi.base.BaseFragment;
import com.ng.ganhuoapi.base.IBaseView;
import com.ng.ganhuoapi.constant.Constant;
import com.ng.ganhuoapi.data.home.HomeContentDataBean;

import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/22.
 */

public class HomeContentFragmet extends BaseFragment<HomeContentPresenter, HomeContentDataBean> implements IBaseView<HomeContentPresenter, HomeContentDataBean> {
    private String categoryId;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private Unbinder unbinder;
    private boolean isLoadMore = false;
    private HomeContentRVadapter homeContentRVadapter;

    public static HomeContentFragmet newInstance(String categoryId) {
        HomeContentFragmet fragment = new HomeContentFragmet();
        fragment.categoryId = categoryId;
        return fragment;
    }

    @Override
    protected int attachLayoutId() {
        return R.layout.fragment_content_gankio;
    }



    @Override
    public void setPresenter(HomeContentPresenter presenter) {
        this.presenter = new HomeContentPresenter(this, categoryId);
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

        homeContentRVadapter = new HomeContentRVadapter(R.layout.item_home_contnet_recyview, null);
        recyclerView.setAdapter(homeContentRVadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        homeContentRVadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), VideoDetailsActivity.class);
                HomeContentDataBean homeContentDataBean = (HomeContentDataBean) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.KEY_HOME_DATA, homeContentDataBean);
                intent.putExtras(bundle);
                intent.putExtra(Constant.KEY_HOME_USER_IMGURL, homeContentDataBean.getUser_info().getAvatar_url());
//                startActivity(intent);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });
        homeContentRVadapter.setEnableLoadMore(true);
        homeContentRVadapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isLoadMore = true;
                start++;
                presenter.doLoadData(true, Constant.limit * start, start);
            }
        });
    }

    private int start = 1;

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initData() throws NullPointerException {
        presenter.doLoadData(false, Constant.limit, 0);
    }

    List<HomeContentDataBean> adapterData;

    @Override
    public void showData(Collection<? extends HomeContentDataBean> data) {
        List<HomeContentDataBean> dataBeanList = (List<HomeContentDataBean>) data;
        final int siaze = dataBeanList == null ? 0 : dataBeanList.size();

        if (isLoadMore) {
//            try {
//                //过滤重复资源
//                adapterData = homeContentRVadapter.getData();
//                for (int i = 0; i < adapterData.size(); i++) {
//                    for (int j = 0; j < siaze; j++) {
//                        if ((adapterData.get(j).getVideo_id() == dataBeanList.get(j).getVideo_id()) || (adapterData.get(i).getTitle().equals(dataBeanList.get(j).getTitle())) || (adapterData.get(i).getGroup_id() == dataBeanList.get(j).getGroup_id())) {
//                            dataBeanList.remove(j);
//                        }
//                    }
//                }
//                homeContentRVadapter.addData(dataBeanList);
//            } catch (Exception e) {
//                e.printStackTrace();
//                homeContentRVadapter.loadMoreComplete();
//            }
            homeContentRVadapter.addData(dataBeanList);
            if (siaze > 0) {
                homeContentRVadapter.loadMoreComplete();
            } else {
                homeContentRVadapter.loadMoreEnd(false);
                Snackbar snackbar = Snackbar.make(recyclerView,"no more data", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                snackbar.show();
            }
        } else {
            if (siaze > 0) {
                homeContentRVadapter.setNewData(dataBeanList);
            }
        }
    }


    @Override
    public void onShowLoading() {
        if (!swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(true);
        }

    }

    @Override
    public void onHideLoading() {
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
        if (isLoadMore) {
            homeContentRVadapter.loadMoreFail();
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
        if (disposable!=null){
            disposable.dispose();
        }
        unbinder.unbind();
    }
}
