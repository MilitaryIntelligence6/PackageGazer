package cn.misection.gazer.constant;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName EnumPrefKey
 * @Description shared preference 的key;
 * @CreateTime 2021年04月19日 23:49:00
 */
public enum EnumPrefKey {

    /**
     * shared preference 的key;
     */
    WINDOW_SHOWN("is_show_window"),

    SETTING_TILE_ADDED("has_qs_tile_added"),

    NOTIFY_TOGGLE_ENABLED("is_noti_toggle_enabled"),
    ;

    private final String value;

    EnumPrefKey(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
