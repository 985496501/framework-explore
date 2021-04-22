package test;

import cn.hutool.json.JSONUtil;
import org.example.EntryApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 研究redis:
 * 对于缓存中间件的有关问题和使用, 面向内存编程
 *
 * @author: jinyun
 * @date: 2021/4/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EntryApplication.class)
public class RedissonTest {
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void redissonClientTest() {

    }


    @Test
    public void setStringRedisTemplateTest() {
        // 看看redis的自动注入是怎么搞得啊;

        // val: string JsonStr int doule ...
        // 一般用于存储json
        stringRedisTemplate.opsForValue()
                .set("orderNum:1:", JSONUtil.toJsonPrettyStr(Collections.singletonMap("price", "123")));
    }

    /**
     * list: 的底层应该是双端队列的实现, 说明白就是 双向链表的实现
     * left->right: 左边为head, 右边为tail
     * <p>
     * leftPush()/rightPush(): 单个数据往list里面push
     * leftPushAll()/rightPushAll(): 可以批量推送数据
     */
    @Test
    public void listTestSet() {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        // list: list操作一般用于什么场景呢， 它是一个 [] - [] - []  基本不用啊 每一个key是一个列表啊
        for (int i = 0; i < 16; i++) {
            Map<String, Object> myself = new HashMap<>(4);
            myself.put("name", "刘津运" + i);
            myself.put("age", 25);
            myself.put("job", "高级开发工程师");
            myself.put("family", null);
            // push: 每次在列表中推送一个 pushAll 虽然推送的是列表但是确实 redis list的一个item
            listOperations.rightPushAll(KEY_1, myself);
        }
    }


    public static final String KEY_1 = "person:class:";

    @Test
    public void listTestGet() {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
//        listOperations.leftPop()
        List<Object> range = listOperations.range(KEY_1, 0, 4);

    }

    public <T> List<T> convert(List<Object> list) {
        return null;
    }

}
