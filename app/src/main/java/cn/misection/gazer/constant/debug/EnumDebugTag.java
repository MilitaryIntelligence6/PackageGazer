package cn.misection.gazer.constant.debug;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName DebugTag
 * @Description TODO
 * @CreateTime 2021年04月22日 13:19:00
 */
public enum EnumDebugTag {

    /**
     * debug 的级别等;
     */
    NORMAL("normal"),

    WARNING("warning"),

    DANGER("danger"),
    ;

    private final String value;

    EnumDebugTag(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
