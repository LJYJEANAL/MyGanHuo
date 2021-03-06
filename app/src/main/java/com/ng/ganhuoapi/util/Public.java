package com.ng.ganhuoapi.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ng.ganhuoapi.MyApplication;
import com.ng.ganhuoapi.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2017/10/30.
 */

public class Public {
    /**
     * 字体大小-36px
     */
    public static float textSize_36px;
    /**
     * 字体大小-34px
     */
    public static float textSize_34px;
    /**
     * 字体大小-32px
     */
    public static float textSize_32px;
    /**
     * 字体大小-30px
     */
    public static float textSize_30px;
    /**
     * 字体大小-28px
     */
    public static float textSize_28px;
    /**
     * 字体大小-26px
     */
    public static float textSize_26px;
    /**
     * 字体大小-24px
     */
    public static float textSize_24px;
    /**
     * 字体大小-22px
     */
    public static float textSize_22px;
    /**
     * 字体大小-20px
     */
    public static float textSize_20px;

    /**
     * 方法说明：多屏幕字体大小适应
     */
    public static float getTextSize(Context context, float ratio) {
        return getTextSize(context, ratio, true);
    }

    /**
     * 方法说明：多屏幕字体大小适应
     */
    public static float getTextSize(Context context, float ratio, boolean limit) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = Math.min(dm.widthPixels, dm.heightPixels);
        float dpiWidth = width / dm.density;
        float size = ratio * dpiWidth;
        if (limit) {
            if (size < 6f) {
                return 6f;
            } else if (size > 20f) {
                return 20f;
            }
        }
        return size;
    }

    /**
     * 方法说明：返回屏幕宽度，单位：像素
     */
    public static int getScreenWidthPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeigthtPixels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = context.getResources().getDimensionPixelSize(context.getResources().getIdentifier("status_bar_height", "dimen", "android"));
        if (statusBarHeight < 0) {
            statusBarHeight = (int) Math.ceil(25 * context.getResources().getDisplayMetrics().density);
        }
        return statusBarHeight;
    }

    /**
     * 重新启动当前activity
     */
    public static void reStartActivity(Activity activity) {
        Intent intent = activity.getIntent();
        activity.finish();
//        activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
        activity. startActivity(intent);
        activity.  overridePendingTransition(0,0);
    }
    /**
     * 方法说明：退出程序
     */
    @SuppressWarnings("deprecation")
    public static void killAPP(Context context) {
        if (Build.VERSION.SDK_INT < 8) {
            android.os.Process.killProcess(android.os.Process.myPid());
            ActivityManager manager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
            manager.restartPackage(context.getPackageName());
            System.exit(0);// 退出程序
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            ActivityManager manager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
            manager.killBackgroundProcesses(context.getPackageName());
            System.exit(0);// 退出程序
        }
        ActivityCollector.finishAll();
    }
    /**
     * 整个应用重新启动
     * @param activity
     */
    public static void reStartAPP(Activity activity){
        final Intent intent = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity. startActivity(intent);
    }
    public static boolean checkBuildVERSIONCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private static ArrayList<Integer> nodataLoadingIds;

    public static int getNodataLoadingId(Context context) {
        if (null == nodataLoadingIds) {
            nodataLoadingIds = new ArrayList<>();
            TypedArray array = context.getResources().obtainTypedArray(R.array.img_nodata_loading);
            int size = array.length();
            for (int index = 0; index < size; index++) {
                nodataLoadingIds.add(array.getResourceId(index, 0));
            }
            array.recycle();
        }
        return nodataLoadingIds.get(new Random().nextInt(nodataLoadingIds.size()));
    }

    public static void getErrorEmpty(final View view, final Listener<Boolean, Boolean> listener) {

        ImageView empty_error_img = view.findViewById(R.id.empty_error_img);
        TextView empty_error_tv = view.findViewById(R.id.empty_error_tv);
        empty_error_tv.setText("加载失败，点击重连！");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setErrorEmptyAnim(v, false);
                listener.onCallBack(true, true);
            }
        });
    }

    public static void setErrorEmptyAnim(View view,boolean isShow) {
        if (null == view) {
            return;
        }
        if (isShow){
            view.setVisibility(View.VISIBLE);
            Animation alphaAnimation = new AlphaAnimation(1, 0.4f);
            alphaAnimation.setDuration(300);
            alphaAnimation.setInterpolator(new LinearInterpolator());
            alphaAnimation.setRepeatCount(Animation.INFINITE);
            alphaAnimation.setRepeatMode(Animation.REVERSE);
            view.startAnimation(alphaAnimation);
        }else{
            view.setVisibility(View.GONE);
            view.clearAnimation();// 取消View闪烁效果
        }
    }

    /**
     * 显示软键盘
     */
    public static void openSoftInput(EditText et) {
        InputMethodManager inputMethodManager = (InputMethodManager) et.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(et, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(EditText et) {
        InputMethodManager inputMethodManager = (InputMethodManager) et.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager
                .HIDE_NOT_ALWAYS);
    }

    /**
     * 获取SD卡路径
     *
     * @return 如果sd卡不存在则返回null
     */
    public static File getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment
                .MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir;
    }

    /**
     * 获取主线程id
     *
     * @return 主线程id
     */
    public static int getMainThreadId() {
        return MyApplication.getMainThreadId();
    }

    /**
     * 判断是否运行在主线程
     *
     * @return true：当前线程运行在主线程
     * fasle：当前线程没有运行在主线程
     */
    public static boolean isRunOnUIThread() {
        // 获取当前线程id, 如果当前线程id和主线程id相同, 那么当前就是主线程
        int myTid = android.os.Process.myTid();
        if (myTid == getMainThreadId()) {
            return true;
        }
        return false;
    }

    /**
     * 运行在主线程
     *
     * @param r 运行的Runnable对象
     */
    public static void runOnUIThread(Runnable r) {
        if (isRunOnUIThread()) {
            // 已经是主线程, 直接运行
            r.run();
        } else {
            // 如果是子线程, 借助handler让其运行在主线程
            MyApplication.getHandler().post(r);
        }
    }
}
