package com.ng.ganhuoapi.fragment.movie.content;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.activity.MBDetailsActivity;
import com.ng.ganhuoapi.base.BaseFragment;
import com.ng.ganhuoapi.base.IBaseView;
import com.ng.ganhuoapi.constant.Constant;
import com.ng.ganhuoapi.data.movie.SubItemsBean;
import com.ng.ganhuoapi.util.Listener;
import com.ng.ganhuoapi.util.Public;

import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/26.
 */

public class MovieContentFragment extends BaseFragment<MovieContentPresenter, SubItemsBean> implements IBaseView<MovieContentPresenter, SubItemsBean> {
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.empty_layout)
    RelativeLayout empty_error_layout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private Unbinder unbinder;
    /**
     * 是否加载更多
     */
    private boolean isLoadMore = false;

    /**
     * 第一页
     */
    private int start = 0;
    /**
     * 类型
     */
    private int position;
    private MovieContentRVadapter movieContentRVadapter;
    private String type;

    public static MovieContentFragment newInstance(int position, String type) {
        MovieContentFragment fragment = new MovieContentFragment();
        fragment.position = position;
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
        movieContentRVadapter = new MovieContentRVadapter(null);
        if (type.equals("即将上映") || type.equals("Top250")) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            if (type.equals("Top250")) {
                movieContentRVadapter.setEnableLoadMore(true);
                movieContentRVadapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        isLoadMore = true;
                        start++;
                        presenter.doLoadData(true, Constant.limit, start * Constant.limit);
                    }
                });
            } else {

            }
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        recyclerView.setAdapter(movieContentRVadapter);
        movieContentRVadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SubItemsBean subItemsBean = (SubItemsBean) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), MBDetailsActivity.class);
                intent.putExtra(Constant.KEY_MOVIE_ID, subItemsBean.getId());
                intent.putExtra(Constant.KEY_MOVIE_IMGURL, subItemsBean.getImages().getLarge());
                intent.putExtra(Constant.KEY_MOVIE_TITLE, subItemsBean.getTitle());
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                        (ImageView) view.getTag(), "mbdetialsTranstion");
                startActivity(intent, options.toBundle());
            }
        });
        movieContentRVadapter.setEnableLoadMore(false);
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
    }

    @Override
    public void setPresenter(MovieContentPresenter presenter) {
        this.presenter = new MovieContentPresenter(this, position);

    }

    @Override
    protected void initData() throws NullPointerException {
        presenter.doLoadData(false, Constant.limit, 0);
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    public void showData(Collection<? extends SubItemsBean> data) {
        if (data != null) {
            List<SubItemsBean> beanList = (List<SubItemsBean>) data;
            final int siaze = beanList == null ? 0 : beanList.size();
            if (isLoadMore) {
                movieContentRVadapter.addData(beanList);
                if (siaze > 0) {
                    movieContentRVadapter.loadMoreComplete();
                } else {
                    movieContentRVadapter.loadMoreEnd(false);
                    Toast.makeText(getActivity(), "no more data", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (siaze > 0) {
                    movieContentRVadapter.setNewData(beanList);
                }
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
        if (isLoadMore) {
            movieContentRVadapter.loadMoreFail();
        }
    }

    @Override
    public void disPosable(Disposable disposable) {
        this.disposable = disposable;
    }

    /**
     * 水管
     */
    private Disposable disposable;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }

        unbinder.unbind();
    }

}
