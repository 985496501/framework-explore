package org.example;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * 假期期间完成 jvm内存相关的系统学习
 * 阿里P7 技术专家 硬性条件
 *
 * 使用maven构建项目：
 * http://www.choupangxia.com/2020/12/01/maven-optional-scope/
 *
 *
 * todo: jdk1.8 你是用的那些类：
 * <ul>
 *     <li>{@link Stream}</li>
 *     <li>{@link Instant}</li>
 *     <li>{@link CompletableFuture}</li>
 *     <li>{@link FunctionalInterface}</li>
 * </ul>
 *
 * @author: jinyun
 * @date: 2021/2/1
 */
public class JavaMain {
    public static void main(String[] args) {
        Object o = new Object();

//        LockSupport.getBlocker()
        // java object layout
        for (; ; ) {
            break;
        }
    }
}