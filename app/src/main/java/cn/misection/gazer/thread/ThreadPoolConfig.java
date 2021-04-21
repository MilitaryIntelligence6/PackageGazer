package cn.misection.gazer.thread;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Military Intelligence 6 root
 * @version 1.0.0
 * @ClassName ThreadPoolConfig
 * @Description TODO
 * @CreateTime 2021年04月20日 14:05:00
 */
class ThreadPoolConfig {

    public static final int corePoolSize = 4;

    public static final int maximumPoolSize = 8;

    public static final long keepAliveTime = 60;

    public static final TimeUnit unit = TimeUnit.SECONDS;

    public static final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();

    private ThreadPoolConfig() {

    }
}
