package cn.misection.gazer.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import cn.misection.gazer.constant.dao.EnumPrefKey;

/**
 * @author Administrator
 */
public class SharedPrefHelper {

    public static boolean hasWindowShown(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(
                EnumPrefKey.WINDOW_SHOWN.value(),
                true);
    }

    public static void putWindowShown(Context context, boolean shown) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit()
                .putBoolean(
                        EnumPrefKey.WINDOW_SHOWN.value(),
                        shown)
                .apply();
    }

    public static boolean hasSettingTileAdded(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(
                EnumPrefKey.SETTING_TILE_ADDED.value(),
                false);
    }

    public static void putSettingTileAdded(Context context, boolean added) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(
                EnumPrefKey.SETTING_TILE_ADDED.value(),
                added)
                .apply();
    }

    public static boolean hasNotificationToggleEnabled(Context context) {
        if (!hasSettingTileAdded(context)) {
            return true;
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(
                EnumPrefKey.NOTIFY_TOGGLE_ENABLED.value(),
                true);
    }

    public static void putNotificationToggleEnabled(Context context, boolean enabled) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit()
                .putBoolean(
                        EnumPrefKey.NOTIFY_TOGGLE_ENABLED.value(),
                        enabled)
                .apply();
    }
}
