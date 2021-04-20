package cn.misection.gazer.thread;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
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

    public static final BlockingQueue<Runnable> workQueue = new BlockingQueue<Runnable>() {
        @Override
        public boolean add(Runnable runnable) {
            return false;
        }

        @Override
        public boolean offer(Runnable runnable) {
            return false;
        }

        @Override
        public void put(@NonNull Runnable runnable) throws InterruptedException {

        }

        @Override
        public boolean offer(Runnable runnable, long timeout, @NonNull TimeUnit unit) throws InterruptedException {
            return false;
        }

        @NonNull
        @Override
        public Runnable take() throws InterruptedException {
            return null;
        }

        @Override
        public Runnable poll(long timeout, @NonNull TimeUnit unit) throws InterruptedException {
            return null;
        }

        @Override
        public int remainingCapacity() {
            return 0;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public int drainTo(@NonNull Collection<? super Runnable> c) {
            return 0;
        }

        @Override
        public int drainTo(@NonNull Collection<? super Runnable> c, int maxElements) {
            return 0;
        }

        @Override
        public Runnable remove() {
            return null;
        }

        @Override
        public Runnable poll() {
            return null;
        }

        @Override
        public Runnable element() {
            return null;
        }

        @Override
        public Runnable peek() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @NonNull
        @Override
        public Iterator<Runnable> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] a) {
            return null;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends Runnable> c) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }
    };

    private ThreadPoolConfig() {

    }
}
