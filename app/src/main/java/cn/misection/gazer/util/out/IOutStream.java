package cn.misection.gazer.util.out;

import android.content.Context;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName OutStream
 * @Description TODO
 * @CreateTime 2021年04月20日 17:19:00
 */
public interface IOutStream {

    /**
     * println;
     * @param context
     * @param s
     */
    void println(Context context, String s);

    /**
     * printf;
     * @param context
     * @param fmt
     * @param args
     */
    void printf(Context context, String fmt, Object... args);
}
