package com.ng.ganhuoapi;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ng.ganhuoapi.base.BaseActivity;
import com.ng.ganhuoapi.custom.MovingImageView;
import com.ng.ganhuoapi.fragment.gankio.GankIoRootFragment;
import com.ng.ganhuoapi.fragment.home.HomeRootFragment;
import com.ng.ganhuoapi.network.IApi;
import com.ng.ganhuoapi.network.OkHttpCreateHelper;
import com.ng.ganhuoapi.util.BottomNavigationViewHelper;
import com.ng.ganhuoapi.util.Listener;
import com.ng.ganhuoapi.util.Public;
import com.ng.ganhuoapi.util.SettingUtil;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
*  bottomNavigationView.setSelectedItemId(R.id.menu_item_personal);  底部栏选中第几个
* */
public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    private MovingImageView miv_menu;
    private Unbinder unbinder;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.bottom_tabs)
    BottomNavigationView bottom_tabs;

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    private Drawable icon = null;
    private String title;
    private MenuItem prevMenuItem;
    /**
     * ViewPage滑动 监听选择的page 释放播放器
     */
    private Listener<Boolean, Integer> listener;

    public void addPageChangeListener(Listener<Boolean, Integer> listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        unbinder = ButterKnife.bind(MainActivity.this);
        if (SettingUtil.getInstance().getIsDayOrNighTheme()) {
            icon = getResources().getDrawable(R.drawable.ic_night);
            title = "夜间模式";

        } else {
            icon = getResources().getDrawable(R.drawable.ic_daytime);
            title = "日间模式";

        }
        navigationView.getMenu().findItem(R.id.nav_camera).setIcon(icon);
        navigationView.getMenu().findItem(R.id.nav_camera).setTitle(title);
        bottom_tabs.setOnNavigationItemSelectedListener(this);
        miv_menu = (MovingImageView) navigationView.getHeaderView(0).findViewById(R.id.miv_menu);

        setupViewPager(viewPager);
        BottomNavigationViewHelper.disableShiftMode(bottom_tabs);
        navigationView.setNavigationItemSelectedListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottom_tabs.getMenu().getItem(0).setChecked(false);
                }
                bottom_tabs.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottom_tabs.getMenu().getItem(position);
                listener.onCallBack(true, position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        OkHttpCreateHelper.createApi(IApi.class,"https://u1.3gtv.net/").liveTab().enqueue(new Callback<RequestBody>() {
            @Override
            public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {

                if (response.isSuccessful()){
                    Log.e("信息",response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<RequestBody> call, Throwable t) {
                Log.e("信息", t.toString());
            }
        });
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main:
                viewPager.setCurrentItem(0);
                break;
            case R.id.gankio:
                viewPager.setCurrentItem(1);
                break;
            case R.id.movie:
                viewPager.setCurrentItem(2);
                break;
            case R.id.book:
                viewPager.setCurrentItem(3);
                break;
            case R.id.nav_camera:

                if (SettingUtil.getInstance().getIsDayOrNighTheme()) {
                    SettingUtil.getInstance().setIsDayOrNighTheme(false);
                    icon = getResources().getDrawable(R.drawable.ic_night);
                    title = "夜间模式";
                } else {
                    SettingUtil.getInstance().setIsDayOrNighTheme(true);
                    icon = getResources().getDrawable(R.drawable.ic_daytime);
                    title = "日间模式";

                }
                navigationView.getMenu().findItem(R.id.nav_camera).setIcon(icon);
                navigationView.getMenu().findItem(R.id.nav_camera).setTitle(title);
                SettingUtil.getInstance().setIsCancleSp(true);
                SettingUtil.getInstance().setIsFirstTime(false);

                Public.reStartActivity(this);
                break;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(HomeRootFragment.newInstance(this, drawerLayout, miv_menu));
        adapter.addFragment(GankIoRootFragment.newInstance("干货"));
        adapter.addFragment(GankIoRootFragment.newInstance("影视"));
        adapter.addFragment(GankIoRootFragment.newInstance("书籍"));
        viewPager.setAdapter(adapter);
    }

    //禁止ViewPager滑动
/*viewPager.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    });*/
//
    private long lastKeyTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                //在这里设置也可以
                CommonUtil.getAppCompActivity(this).getSupportActionBar().hide();
                StandardGSYVideoPlayer.backFromWindowFull(this);
            } else {
                if (System.currentTimeMillis() - lastKeyTime > 2000) {
                    lastKeyTime = System.currentTimeMillis();
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                } else {
                    GSYVideoPlayer.releaseAllVideos();

                    Animation anim = AnimationUtils.loadAnimation(this, R.anim.tv_off);
                    anim.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            SettingUtil.getInstance().setIsCancleSp(false);
                            Log.i("信息",SettingUtil.getInstance().getIsCancleSp()+"-onAnimationEnd");
                            Public.killAPP(MainActivity.this);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    ((View) llContent.getParent()).startAnimation(anim);
                }
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }
}
