package cn.misection.gazer.util.outstream;

import android.content.Context;

import cn.misection.gazer.util.ToastUtil;
import cn.misection.gazer.view.GazeView;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName UiOutStream
 * @Description TODO
 * @CreateTime 2021年04月20日 17:55:00
 */
public class UiOutStream implements IOutStream {

    private volatile static UiOutStream instance = null;

    private UiOutStream() {

    }

    public static UiOutStream getInstance() {
        if (instance == null) {
            synchronized (UiOutStream.class) {
                if (instance == null) {
                    instance = new UiOutStream();
                }
            }
        }
        return instance;
    }

    @Override
    public void println(Context context, String s) {
        // 其实不需要换行;
        GazeView.show(context, s);
        ToastUtil.show(context, s);
    }

    @Override
    public void printf(Context context, String fmt, Object... args) {
        GazeView.show(context, String.format(fmt, args));
        ToastUtil.show(context, String.format(fmt, args));
    }
}
