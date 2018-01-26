package com.ng.ganhuoapi.base;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.util.ActivityCollector;
import com.ng.ganhuoapi.util.SettingUtil;

/**
 * Created by Administrator on 2017/12/28.
 */

public abstract class IBaseActivity<T extends IBasePresenter, V> extends AppCompatActivity implements IBaseView<T, V> {
    protected T presenter;

    @Override
    public void setContentView(View view) {
        if (!SettingUtil.getInstance().getIsDayOrNighTheme()){
            setTheme(R.style.AppThemeDark);
        }
        super.setContentView(view);
        ActivityCollector.addActivity(this);
        initView(view);
        initToolBar();

        setPresenter(presenter);
        initData();
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    /**
     * 初始化视图控件
     */
    protected abstract void initView(View view);

    /**
     * 初始化数据
     */
    protected abstract void initData() throws NullPointerException;

    /**
     * 初始化 Toolbar
     */
    protected abstract void initToolBar();

}
