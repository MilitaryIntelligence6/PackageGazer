package cn.misection.gazer.system;

import cn.misection.gazer.thread.ThreadPool;
import cn.misection.gazer.util.out.IOutStream;
import cn.misection.gazer.util.out.UiOutStream;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName AppSystem
 * @Description 暂时来不及添加管理了;
 * @CreateTime 2021年04月20日 14:06:00
 */
public class AppSystem {

    public static final IOutStream out = UiOutStream.getInstance();

    public static final ThreadPool pool = ThreadPool.getInstance();

    private AppSystem() {

    }
}
