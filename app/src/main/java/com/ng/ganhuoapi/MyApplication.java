package com.ng.ganhuoapi;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.ng.ganhuoapi.util.AndroidFactory;
import com.ng.ganhuoapi.util.SettingUtil;


public class MyApplication extends Application {
    protected static int mainThreadId;
    protected static Handler handler;
    @Override
    public void onCreate() {



        super.onCreate();
        Context context = getApplicationContext();
        mainThreadId = android.os.Process.myTid();
        handler = new Handler();
        AndroidFactory.setApplicationContext(context);
    }

    /**
     * 获取主线程id
     *
     * @return 主线程id
     */
    public static int getMainThreadId() {
        return mainThreadId;
    }

    /**
     * 获取全局handler
     *
     * @return 全局handler
     */
    public static Handler getHandler() {
        return handler;
    }
}
