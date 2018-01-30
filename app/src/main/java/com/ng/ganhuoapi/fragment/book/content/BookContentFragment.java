package com.ng.ganhuoapi.fragment.book.content;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.ng.ganhuoapi.data.book.BookItemBean;
import com.ng.ganhuoapi.util.Public;

import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/25.
 */

public class BookContentFragment extends BaseFragment<BookContentPresenter, BookItemBean> implements IBaseView<BookContentPresenter, BookItemBean> {
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
    private int start = 2;
    /**
     * 类型
     */
    private String type;
    private Unbinder unbinder;
    private BookContentRVadapter bookContentRVadapter;

    public static BookContentFragment newInstance(String type) {
        BookContentFragment fragment = new BookContentFragment();
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
        bookContentRVadapter = new BookContentRVadapter(R.layout.item_book_content_recyview, null);
        recyclerView.setAdapter(bookContentRVadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookContentRVadapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), MBDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.KEY_BOOK_ITEM_BEAN, (BookItemBean) adapter.getItem(position));
                intent.putExtras(bundle);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                        (ImageView) view.getTag(), "mbdetialsTranstion");
                startActivity(intent, options.toBundle());
            }
        });
        bookContentRVadapter.setEnableLoadMore(true);
        bookContentRVadapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isLoadMore = true;
                start++;
                presenter.doLoadData(true, Constant.limit * start, bookContentRVadapter.getItemCount() + 1);
            }
        });

    }

    @Override
    protected void initToolBar() {

    }

    @Override
    public void setPresenter(BookContentPresenter presenter) {
        this.presenter = new BookContentPresenter(this, type);

    }

    @Override
    protected void initData() throws NullPointerException {
        presenter.doLoadData(false, Constant.limit, 0);
    }

    @Override
    public void showData(Collection<? extends BookItemBean> data) {
        List<BookItemBean> bookItemBeans = (List<BookItemBean>) data;
        final int siaze = bookItemBeans == null ? 0 : bookItemBeans.size();
        if (isLoadMore) {
            if (siaze > 0) {
                bookContentRVadapter.addData(bookItemBeans);
                bookContentRVadapter.loadMoreComplete();
            } else {
                bookContentRVadapter.loadMoreEnd(false);
                Toast.makeText(getActivity(), "no more data", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (siaze > 0) {
                bookContentRVadapter.setNewData(bookItemBeans);
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
            bookContentRVadapter.loadMoreFail();
        }
    }

    @Override
    public void disPosable(Disposable disposable) {
        this.disposable = disposable;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //切断
        if (disposable!=null){
            disposable.dispose();
        }
        unbinder.unbind();
    }

}
