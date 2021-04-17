package org.example.reference;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * Reference:
 * WeakReference:
 *
 * 缓存系统的设计
 *
 *
 * @author: Liu Jinyun
 * @date: 2021/2/14/23:05
 */
public class ReferenceTest {

    @Test
    public void referenceTest() {
        @NonNull AsyncCache<Object, Object> caffeine = Caffeine.newBuilder()
                .buildAsync();
        // todo： 这下缓存系统  和  JDK1.8 的 CompletableFuture.
        caffeine.put("String", new CompletableFuture<>());

    }
}
