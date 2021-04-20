package cn.misection.gazer.constant.common;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName EnumStringPool
 * @Description TODO
 * @CreateTime 2021年04月20日 00:03:00
 */
public enum EnumStringPool {

    /**
     * 状态;
     */
    EMPTY(""),

    EXTRA_FROM_QS_TILE("from_qs_tile"),

    ACTION_STATE_CHANGED("cn.misection.gazer.ACTION_STATE_CHANGED"),

    ACTION_UPDATE_TITLE("cn.misection.gazer.ACTION.UPDATE_TITLE"),
    ;

    private final String value;

    EnumStringPool(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
