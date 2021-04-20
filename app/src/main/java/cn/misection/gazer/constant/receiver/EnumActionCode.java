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
    PAUSE,

    RESUME,

    STOP,

    UNKNOWN,
    ;

    EnumActionCode() {
    }

    public int value() {
        return this.ordinal();
    }

    public static EnumActionCode selectByOrdinal(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length - 1) {
//            throw new IndexOutOfBoundsException(String.format("index %d is out of bounds", ordinal));
            return UNKNOWN;
        }
        return values()[ordinal];
    }
}
