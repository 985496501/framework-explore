package test.redis;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

/**
 * 使用redis 的zset 完成排行的问题
 * <p>
 * 指令： ZADD key [NX|XX] [GT|LT] [CH] [INCR] score member
 * <p>
 * 关键在于 score: a double 64-bit floating point number to represent the score.
 * 这个score 使用了一个双精度64位浮点型小数标识  that is able to represent precisely integer numbers between -(2^53) and +(2^53) included.
 * <p>
 * lexicographical
 *
 * @author: jinyun
 * @date: 2021/6/2
 */
@SpringBootTest
public class ZsetTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // redis 操作使用：
    // key: 建立尽可能的短, 一般就是 tableName(业务名):id  这里没有id   就直接使用 微服务的名：表明： 实际上可以直接表名
    // value 直接使用object 会通过fastjson进行序列化
    // 尽可能的使用批量操作 避免频繁的网络调用
    // 默认永久存储100行记录
    //
    //  zset 如何执行记录修改呢？ 直接使用key? key是redis的一个大目录啊？  解决不存储全量数据  仅仅使用id; 这样
    // 我们在提供一个 hash 结构存储这些热点数据 一方面给前面调用查询 一方面后端使用;

    //  zset 如何进行 查询操作呢 数据被分区了;
    //
    @Test
    public void zsetAddTest() {
        ZSetOperations<String, Object> op = redisTemplate.opsForZSet();
//        op.add("")
    }


    @Test
    public void geoTest() {
        GeoOperations<String, Object> operations = redisTemplate.opsForGeo();
//        operations.
    }


    @Test
    public void helloTest() {
        System.out.println("hello world");
    }
}
