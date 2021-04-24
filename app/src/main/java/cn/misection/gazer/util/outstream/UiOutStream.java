package cn.misection.gazer.util.outstream;

import android.content.Context;
import android.view.View;

import cn.misection.gazer.util.SnackbarUtil;
import cn.misection.gazer.util.ToastUtil;

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
    public void printt(Context context, String msg) {
        // 其实不需要换行, 习惯使然;
        ToastUtil.show(context, msg);
    }

    @Override
    public void printf(Context context, String fmt, Object... args) {
        ToastUtil.show(context, String.format(fmt, args));
    }

    @Override
    public void prints(View view, String msg) {
        SnackbarUtil.show(view, msg);
    }

    @Override
    public void log(String msg) {
        System.out.println("hh");
    }

    @Override
    public void echo(String msg) {
        System.err.println("unimpl exception");
    }
}
