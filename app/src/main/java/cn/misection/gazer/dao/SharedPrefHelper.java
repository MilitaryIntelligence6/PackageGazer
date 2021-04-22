package cn.misection.gazer.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import cn.misection.gazer.constant.dao.EnumPrefKey;

/**
 * @author Administrator
 */
public class SharedPrefHelper {

    /**
     * pop 判断传入窗口是否shown;
     * @param context
     * @return
     */
    public static boolean hasWindowShown(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getBoolean(
                EnumPrefKey.WINDOW_SHOWN.value(),
                true);
    }

    /**
     * 设定窗口显示状态;
     * @param context
     * @param shown
     */
    public static void putWindowShown(Context context, boolean shown) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        sp.edit().putBoolean(
                        EnumPrefKey.WINDOW_SHOWN.value(),
                        shown)
                .apply();
    }

    /**
     * 设置中心是否开启;
     * @param context
     * @return
     */
    public static boolean hasSettingTileAdded(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getBoolean(
                EnumPrefKey.SETTING_TILE_ADDED.value(),
                false);
    }

    /**
     * 修改设置中心状态;
     * @param context
     * @param added
     */
    public static void putSettingTileAdded(Context context, boolean added) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        sp.edit().putBoolean(
                EnumPrefKey.SETTING_TILE_ADDED.value(),
                added)
                .apply();
    }

    /**
     * 任务栏程序是否运行;
     * @param context
     * @return
     */
    public static boolean hasNotificationToggleEnabled(Context context) {
        if (!hasSettingTileAdded(context)) {
            return true;
        }
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getBoolean(
                EnumPrefKey.NOTIFY_TOGGLE_ENABLED.value(),
                true);
    }

    /**
     * 设定任务栏程序运行状态;
     * @param context
     * @param enabled
     */
    public static void putNotificationToggleEnabled(Context context, boolean enabled) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        sp.edit().putBoolean(
                        EnumPrefKey.NOTIFY_TOGGLE_ENABLED.value(),
                        enabled)
                .apply();
    }
}
