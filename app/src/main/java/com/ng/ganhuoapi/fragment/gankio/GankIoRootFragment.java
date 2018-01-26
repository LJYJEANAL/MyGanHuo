package com.ng.ganhuoapi.fragment.gankio;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.base.BaseFragment;
import com.ng.ganhuoapi.base.IBaseView;
import com.ng.ganhuoapi.constant.Constant;
import com.ng.ganhuoapi.fragment.FragmentPageAdapter;
import com.ng.ganhuoapi.fragment.FragmentPresenter;
import com.ng.ganhuoapi.fragment.book.content.BookContentFragment;
import com.ng.ganhuoapi.fragment.gankio.content.GankioContentFragment;
import com.ng.ganhuoapi.fragment.movie.content.MovieContentFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

public class GankIoRootFragment extends BaseFragment<FragmentPresenter,String> implements IBaseView<FragmentPresenter,String> {
    private Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    private String type;

    public static GankIoRootFragment newInstance(String type) {
        GankIoRootFragment fragment = new GankIoRootFragment();
        fragment.type = type;
        return fragment;
    }
    @Override
    public void setPresenter(FragmentPresenter presenter) {
        if(type!=null){
            String[] titlearray=null;
            if (type.equals("干货")){
                titlearray=getResources().getStringArray(R.array.gankio_title);
            }else if (type.equals("影视")){
                titlearray=getResources().getStringArray(R.array.movie_title);
            }else if (type.equals("书籍")){
                titlearray=getResources().getStringArray(R.array.book_title);
            }
            if (titlearray != null) {
                this.presenter = new FragmentPresenter(getActivity(), this, titlearray);
            }
        }

    }

    @Override
    public void disPosable(Disposable disposable) {

    }

    @Override
    protected int attachLayoutId() {
        return R.layout.fragment_gankioroot;
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        if (type!=null){
            title.setText(type);
            if (type.equals("干货")){
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }else if (type.equals("影视")){
                tabLayout.setTabMode(TabLayout.GRAVITY_CENTER);
            }else{
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void initData() throws NullPointerException {
        if (presenter!=null)
        presenter.doLoadData(false, Constant.limit, Constant.start);
    }

    @Override
    protected void initToolBar() {
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    @Override
    public void showData(Collection<? extends String> data) {
        List<Fragment> fragmentList = new ArrayList<>();

        if (type.equals("干货")) {
            for (int i = 0; i < data.size(); i++) {
                fragmentList.add(GankioContentFragment.newInstance(((List<String>) data).get(i)));
            }
        } else if (type.equals("书籍")) {
            for (int i = 0; i < data.size(); i++) {
                fragmentList.add(BookContentFragment.newInstance(((List<String>) data).get(i)));
            }
        } else  {
            for (int i = 0; i < data.size(); i++) {
                fragmentList.add(MovieContentFragment.newInstance(i,((List<String>) data).get(i)));
            }
        }
            FragmentPageAdapter fragmentPageAdapter = new FragmentPageAdapter(getChildFragmentManager(), (List<String>) data, fragmentList);
            viewpager.setAdapter(fragmentPageAdapter);
            tabLayout.setupWithViewPager(viewpager);//必须设置 否侧tablayout无法显示出来

    }


    @Override
    public void onShowLoading() {
    }

    @Override
    public void onHideLoading() {
    }

    @Override
    public void onShowNetError(String err) {
    }


}
