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
    ACTION_PAUSE,

    ACTION_RESUME,

    ACTION_STOP,
    ;

    EnumActionCode() {
    }

    public static EnumActionCode selectByOrdinal(int ordinal) {
        if (ordinal < 0 || ordinal > values().length) {
            throw new IndexOutOfBoundsException(String.format("index %d is out of bounds", ordinal));
        }
        return values()[ordinal];
    }
}
