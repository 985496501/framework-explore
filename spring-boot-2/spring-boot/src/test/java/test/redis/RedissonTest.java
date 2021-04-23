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
 *
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
