package cn.misection.gazer.constant.receiver;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName NotificationString
 * @Description TODO
 * @CreateTime 2021年04月20日 00:30:00
 */
public enum EnumActionString {

    /**
     * string;
     */
    EXTRA_NOTIFICATION("command"),

    NOTIFICATION_RECEIVER("cn.misection.gazer.ACTION_NOTIFICATION_RECEIVER"),
    ;

    private final String value;

    EnumActionString(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
