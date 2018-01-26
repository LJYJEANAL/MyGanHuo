package com.ng.ganhuoapi.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Meiji on 2017/2/20.
 */

public class SettingUtil {

    private SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(AndroidFactory.getApplicationContext());

    public static SettingUtil getInstance() {
        return SettingsUtilInstance.instance;
    }

    public boolean getIsFirstTime() {
        return setting.getBoolean("first_time", true);
    }

    public void setIsFirstTime(boolean flag) {

        setting.edit().putBoolean("first_time", flag).apply();
    }

    /**
     * 记录主题模式
     * @return
     */
    public boolean getIsDayOrNighTheme() {
        return setting.getBoolean("key_is_DayOrNight", true);
    }

    public void setIsDayOrNighTheme(boolean flag) {
        setting.edit().putBoolean("key_is_DayOrNight", flag).apply();
    }

    /**
     * 记录是否做了切换模式操作
     * @return
     */
    public boolean getIsCancleSp() {
        return setting.getBoolean("key_IsCancleSp", true);
    }

    public void setIsIsCancleSp(boolean flag) {
        setting.edit().putBoolean("key_IsCancleSp", flag).apply();
    }

    private static final class SettingsUtilInstance {
        private static final SettingUtil instance = new SettingUtil();
    }
}
