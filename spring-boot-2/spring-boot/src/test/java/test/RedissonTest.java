package test;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
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
    private RedisTemplate<Object, Object> redisTemplate;

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


    @Test
    public void listTest() {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, "刘津运"));

        ListOperations<Object, Object> listOperations = redisTemplate.opsForList();
        // list: list操作一般用于什么场景呢， 它是一个 [] - [] - []  基本不用啊
        listOperations.rightPush("list:order:", list);
    }






    /**
     * 需要显示实现 serializable 不然实例化失败
     *
     * 如果这么用对象纯属会出现很大问题 redis客户端展示的 16进制
     */
    @AllArgsConstructor
    @Data
    static class Student implements Serializable {
        private int num;
        private String userName;
    }
}
