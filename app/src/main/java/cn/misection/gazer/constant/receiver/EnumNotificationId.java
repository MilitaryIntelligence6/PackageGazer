package cn.misection.gazer.constant.receiver;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName EnumNotificationActionReceiverParam
 * @Description TODO
 * @CreateTime 2021年04月20日 00:22:00
 */
public enum EnumNotificationId {

    /**
     * notification id;
     */
    NOTIFICATION_ID(1),
    ;

    private final int value;

    EnumNotificationId(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
