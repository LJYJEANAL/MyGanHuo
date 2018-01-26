package com.ng.ganhuoapi.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 在活动管理器中，我们通过一个List 来暂存活动，
 * 然后提供了一个addActivity()方法 ,用于向List 中添加一个活动，
 * 提供了一个removeActivity()方法用于从List 中移除活动，
 * 最后提供了一个finishAll()方法用于将List 中存储的活动全部都销毁掉。
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
