package test.redis;

import org.example.EntryApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 分布式锁的实现规则:
 *
 * 看下如何实现的, 如果通过多主机的内存变量 完成同步的
 * 思路是怎么样的?
 *
 * todo: 分布式锁的实现?
 * Required:
 * 1. 分布式环境下, 一个方法同一时刻是能被一个机器的一个线程执行;
 * 2. 高可用的获取锁 和 释放锁
 * 3. 高性能的获取锁 和 释放锁
 * 4. 具备可重入的特性， 可重入;
 * 5. 具备锁失效机制 防止死锁, 能够自动释放锁, 不然会锁住资源永远都不会执行;
 * 6. 具备非阻塞特性, 没有获取到锁直接返回获取锁失败。 排他锁；
 *
 *
 *
 *
 *
 * 基于数据库实现, 这个直接就不用看了
 * 基于redis实现 需要掌握：
 * 基于zookeeper实现 需要掌握;
 *
 *
 * 分布式场景面临的问题：数据一致性
 * CAP 原则：
 * 一般的设计都是在保证分区容错性的前提下, 牺牲一定的一致性, 在事件允许的情况下 达到最终一致性；同时要具备高可用性；
 *
 * @author: jinyun
 * @date: 2021/4/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EntryApplication.class)
public class RedissonTest {
    @Autowired
    private RedissonClient redissonClient;

    private static final String TEST_LOCK = "testLock";

    @Test
    public void redissonClientTest() {
        // 看看redis的自动注入是怎么搞得啊;
        RLock lock = redissonClient.getLock(TEST_LOCK);
        System.out.println(lock);
    }


}
