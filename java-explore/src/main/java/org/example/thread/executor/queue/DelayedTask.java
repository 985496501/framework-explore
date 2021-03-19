package org.example.thread.executor.queue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author: jinyun
 * @date: 2021/3/18
 */
public class DelayedTask implements Delayed, Runnable {
    /**
     * high precision time0
     * when use this to start figure out current time
     */
    private static final long START_TIME = System.nanoTime();


    static long nanoTime() {
        return System.nanoTime() - START_TIME;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private long time;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    private Object object;

    public DelayedTask(long time, Object object) {
        this.time = time;
        this.object = object;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(getTime(), TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
        System.out.println(TimeUnit.SECONDS.convert(2000, TimeUnit.MILLISECONDS));
    }


    @Override
    public int compareTo(Delayed o) {
        return 0;
    }

    @Override
    public void run() {
        System.out.println(object.toString());
    }
}
