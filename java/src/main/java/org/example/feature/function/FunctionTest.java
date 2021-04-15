package org.example.feature.function;

import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author: Liu Jinyun
 * @date: 2021/2/17/1:17
 */
public class FunctionTest {

    /**
     * 生产者
     * 一个函数式接口
     *
     * 1. return a object
     * 2. return a static method
     * 3. stream supplier
     */
    @Test
    public void supplierTest() {
        // declare a supplier.
        Supplier<?> supplier = FunctionTest::new;
        Object o = supplier.get();

        Supplier<?> supplier1 = FunctionTest::hi;
    }


    public static String hi() {
        return "hello toString";
    }

    /**
     * 消费者接口
     * accept(T)
     *
     */
    @Test
    public void consumerTest() {
        Consumer<Object> consumer = System.out::println;
        consumer.accept("hello world");
    }

}
