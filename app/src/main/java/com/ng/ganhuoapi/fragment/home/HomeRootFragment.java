package com.ng.ganhuoapi.fragment.home;


import android.graphics.Rect;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.ng.ganhuoapi.MainActivity;
import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.base.BaseFragment;
import com.ng.ganhuoapi.base.IBaseView;
import com.ng.ganhuoapi.constant.Constant;
import com.ng.ganhuoapi.custom.MovingImageView;
import com.ng.ganhuoapi.custom.MovingViewAnimator;
import com.ng.ganhuoapi.fragment.FragmentPageAdapter;
import com.ng.ganhuoapi.fragment.FragmentPresenter;
import com.ng.ganhuoapi.fragment.home.video_content.HomeVideoContentFragmet;
import com.ng.ganhuoapi.util.Listener;
import com.ng.ganhuoapi.util.SettingUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/18.
 */

public class HomeRootFragment extends BaseFragment<FragmentPresenter, String> implements IBaseView<FragmentPresenter, String> {
    private Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    private DrawerLayout drawerLayout;
    private MovingImageView mivMenu;
    /**
     * videoId
     */
    private String[] categoryId;
    /**
     * TabTitle
     */
    private String[] home_title;
    private MainActivity mainActivity;
    /**
     * 监听选择的page 释放播放器
     */
    private Listener<Boolean ,Integer > listener;
    public void addPageChangeListener(Listener<Boolean ,Integer >listener){
        this.listener=listener;
    }
    public static HomeRootFragment newInstance(MainActivity mainActivity,DrawerLayout drawerLayout, MovingImageView miv_menu) {
        HomeRootFragment fragment = new HomeRootFragment();
        fragment.mainActivity=mainActivity;
        fragment.drawerLayout = drawerLayout;
        fragment.mivMenu = miv_menu;
        return fragment;
    }

    @Override
    protected int attachLayoutId() {
        return R.layout.fragment_gankioroot;
    }


    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        if (mivMenu != null) initStatus(mivMenu);
        mainActivity.addPageChangeListener(new Listener<Boolean, Integer>() {
            @Override
            public void onCallBack(Boolean aBoolean, Integer reply) {
                listener.onCallBack(true,reply);
            }
        });
    }

    @Override
    protected void initData() throws NullPointerException {
        if (presenter != null) {
            presenter.doLoadData(false, Constant.limit, Constant.start);
        }
    }

    @Override
    protected void initToolBar() {
        toolbar.setTitle("首页");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 在Toolbar上添加侧滑菜单切换按钮
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, 0, 0);
        if (toggle != null && drawerLayout != null) {
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }

        title.setText("");
        title.setVisibility(View.GONE);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                listener.onCallBack(true,position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setPresenter(FragmentPresenter presenter) {
        home_title = getResources().getStringArray(R.array.home_title);
        categoryId = getResources().getStringArray(R.array.video_id);
        this.presenter = new FragmentPresenter(getActivity(), this, home_title);

    }

    @Override
    public void showData(Collection<? extends String> data) {
        List<Fragment> fragmentList = new ArrayList<>();
        if (categoryId != null && categoryId.length == data.size()) {
            for (int i = 0; i < data.size(); i++) {

                fragmentList.add(HomeVideoContentFragmet.newInstance(this,categoryId[i]));
//                fragmentList.add(HomeContentFragmet.newInstance(categoryId[i]));
            }
        }
        FragmentPageAdapter fragmentPageAdapter = new FragmentPageAdapter(getChildFragmentManager(), (List<String>) data, fragmentList);
        viewpager.setAdapter(fragmentPageAdapter);
        tabLayout.setupWithViewPager(viewpager);//必须设置 否侧tablayout无法显示出来

        if (SettingUtil.getInstance().getIsFirstTime()) {
            showTapTarget();
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
    }


    @Override
    public void disPosable(Disposable disposable) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void showTapTarget() {
        final Display display = getActivity().getWindowManager().getDefaultDisplay();
        final Rect target = new Rect(
                0,
                display.getHeight(),
                0,
                display.getHeight());
        target.offset(display.getWidth() / 8, -56);
        // 引导用户使用
        TapTargetSequence sequence = new TapTargetSequence(getActivity())
                .targets(
//                        TapTarget.forToolbarMenuItem(toolbar, R.id.toolbar, "点击这里进行搜索")
//                                .dimColor(android.R.color.black)
//                                .outerCircleColor(R.color.colorPrimary)
//                                .drawShadow(true)
//                                .id(1),
                        TapTarget.forToolbarNavigationIcon(toolbar, "点击这里展开侧栏")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.colorPrimary)
                                .drawShadow(true)
                                .id(1),
                        TapTarget.forBounds(target, "点击这里切换内容")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.colorPrimary)
                                .targetRadius(60)
                                .transparentTarget(true)
                                .drawShadow(true)
                                .id(2)

                ).listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        SettingUtil.getInstance().setIsFirstTime(false);
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        SettingUtil.getInstance().setIsFirstTime(false);
                    }
                });
        sequence.start();
    }

    /**
     * 移动图片设置
     */
    private void initStatus(final MovingImageView mivMenu) {
        if (drawerLayout != null) {
            drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    mivMenu.pauseMoving();
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    if (mivMenu.getMovingState() == MovingViewAnimator.MovingState.stop) {
                        mivMenu.startMoving();
                    } else if (mivMenu.getMovingState() == MovingViewAnimator.MovingState.pause) {
                        mivMenu.resumeMoving();
                    }
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    mivMenu.stopMoving();
                }

                @Override
                public void onDrawerStateChanged(int newState) {
                    mivMenu.startMoving();

                    if (mivMenu.getMovingState() == MovingViewAnimator.MovingState.stop) {
                        mivMenu.startMoving();
                    } else if (mivMenu.getMovingState() == MovingViewAnimator.MovingState.pause) {
                        mivMenu.resumeMoving();
                    }
                }
            });
        }
    }


}
