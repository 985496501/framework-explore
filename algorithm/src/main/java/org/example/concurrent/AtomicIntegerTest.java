package org.example.concurrent;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: jinyun
 * @date: 2021/6/3
 */
public class AtomicIntegerTest {

    /**
     *
     */
    @Test
    public void limitTest() {
        AtomicInteger atomicInteger = new AtomicInteger(Integer.MAX_VALUE-1);
        System.out.println(atomicInteger.incrementAndGet());
    }
}
