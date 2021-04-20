package cn.misection.gazer.constant.receiver;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName EnumActionCode
 * @Description TODO
 * @CreateTime 2021年04月20日 00:25:00
 */
public enum EnumActionCode {

    /**
     * 生命周期;
     */
    ACTION_PAUSE(0),

    ACTION_RESUME(1),

    ACTION_STOP(2),
    ;

    private final int value;

    EnumActionCode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
