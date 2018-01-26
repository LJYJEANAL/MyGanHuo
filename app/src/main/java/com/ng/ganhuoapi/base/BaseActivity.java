package com.ng.ganhuoapi.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.util.ActivityCollector;
import com.ng.ganhuoapi.util.SettingUtil;

/**
 * Created by Administrator on 2017/12/18.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (!SettingUtil.getInstance().getIsDayOrNighTheme()){
            setTheme(R.style.AppThemeDark);
        }
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        ActivityCollector.addActivity(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
