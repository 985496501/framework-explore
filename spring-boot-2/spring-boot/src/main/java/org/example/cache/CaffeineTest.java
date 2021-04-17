package org.example.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.Test;

import java.time.Duration;

/**
 * Optional:
 * <p>
 * 1. 自动加载数据到缓存中, 可以异步
 * 2. 自动淘汰机制 evict
 * 3. keys自动被包装到 weak 引用中
 * 4. value 自动被包装到 弱引用 和  软引用中
 * 5. write 转播给外部资源
 * 6. evict entry 会有通知
 * 7. 缓存的积累 可以获取统计数据
 *
 * @author: jinyun
 * @date: 2021/4/16
 */
public class CaffeineTest {

    @Test
    public void buildTest() {
        Caffeine<Object, Object> objectObjectCaffeine = Caffeine.newBuilder();
        objectObjectCaffeine
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofMinutes(10))
                .removalListener((k, v, c) -> {

                });
    }
}
