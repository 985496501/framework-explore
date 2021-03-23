package org.example.thread.executor.queue;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author: jinyun
 * @date: 2021/3/23
 */
public class ArrayBlockingQueueTest {
    @Test
    public void queueTest() {
        // fixed capacity.
        ArrayBlockingQueue<String> strings = new ArrayBlockingQueue<>(2);

        strings.add("fasdf");
        strings.add("fdasf");
        strings.add("fdsaf");
    }

}
