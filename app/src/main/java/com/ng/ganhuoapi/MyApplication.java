package com.ng.ganhuoapi;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.ng.ganhuoapi.util.AndroidFactory;
import com.ng.ganhuoapi.util.Public;


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
        if ("com.ng.ganhuoapi".equals(getCurProcessName(context))) {
            // 初始化相关字体大小
            Public.textSize_36px = Public.getTextSize(this, 0.0483f);
            Public.textSize_34px = Public.getTextSize(this, 0.0455f);
            Public.textSize_32px = Public.getTextSize(this, 0.0430f);
            Public.textSize_30px = Public.getTextSize(this, 0.0403f);
            Public.textSize_28px = Public.getTextSize(this, 0.0375f);
            Public.textSize_26px = Public.getTextSize(this, 0.0352f);
            Public.textSize_24px = Public.getTextSize(this, 0.0323f);
            Public.textSize_22px = Public.getTextSize(this, 0.0297f);
            Public.textSize_20px = Public.getTextSize(this, 0.0270f);
        }
    }
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
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
