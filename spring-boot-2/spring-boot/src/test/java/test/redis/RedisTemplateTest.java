package test.redis;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import org.example.EntryApplication;
import org.example.dto.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * 研究redis:
 * 对于缓存中间件的有关问题和使用, 面向内存编程
 *
 * key: string
 * value: string, list, set, zset, hash.
 *
 * redis_key_prefix 命名规则：  服务名:模块名:valueType
 * auth:detail:hash
 *
 *
 * redis: 的基本的操作
 *
 * @author: jinyun
 * @date: 2021/4/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EntryApplication.class)
public class RedisTemplateTest {
    public static final String KEY_LIST = "test:list:";
    public static final String KEY_SET = "test:set:";
    public static final String KEY_ZSET = "test:zset:";
    public static final String KEY_HASH = "test:hash:";

    Person person = new Person("津运", 25, "P7");
    Person person2 = new Person("刘津运", 26, "P8");

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 使用这个最好的办法就是类型强转a
     */
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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
            listOperations.rightPushAll(KEY_LIST, myself);
        }
    }


    @Test
    public void listTestGet() {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        List<Map> typeList = getTypeList(listOperations.range(KEY_LIST, 0, 4), Map.class);
        typeList.forEach(k -> {
            k.forEach((key, value) -> System.out.println(key + " " + value));
        });
    }


    @Test
    public void listSetTest() {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        Long aLong = listOperations.rightPushAll(KEY_LIST, person, person2);
        System.out.println(aLong); // 每次调用都会插入, 并且返回这个list的数量
    }

    @Test
    public void setTest() {
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();

        Long add = setOperations.add(KEY_SET, person, person2);
        System.out.println(add);
    }


    @Test
    public void sortedSetTest() {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
//        zSetOperations.
        Boolean add = zSetOperations.add(KEY_ZSET, person, 0);
        System.out.println(add);
        Boolean add1 = zSetOperations.add(KEY_ZSET, person2, 1);
        System.out.println(add1);
        Boolean add2 = zSetOperations.add(KEY_ZSET, person2, 3);
        System.out.println(add2);
    }

    @Test
    public void opZsetTest() {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
    }


    @Test
    public void singleHashSetTest() {
        String primaryKey = "123456";

        Map<String, Object> myself = new HashMap<>(4);
        myself.put("name", "刘津运");
        myself.put("age", 25);
        myself.put("job", "高级开发工程师2");
        myself.put("family", null);
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(KEY_HASH, primaryKey, myself);

        Map<String, Object> dbMap = (Map<String, Object>) hashOperations.get(KEY_HASH, primaryKey);
        System.out.println(JSONObject.toJSONString(dbMap));
    }

    /**
     * k, v, 这个场景用于一个key
     */
    @Test
    public void batchHashSetTest() {
        String dict = "dict";
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        Person person = new Person("津运", 25, "P7");
        Person person2 = new Person("刘津运", 26, "P8");
        Map<String, Person> map = new HashMap<>(2);
        map.put("001", person);
        map.put("002", person2);

        // key, Map<HK, HV> map 批量插入 value: JSONObject;
        hashOperations.putAll(KEY_HASH + dict, map);

        // 对应的方法, 批量直接取出
        Map<String, Person> entries = (Map<String, Person>) (Object) hashOperations.entries(KEY_HASH + dict);
        System.out.println(JSONObject.toJSONString(entries));

        ArrayList<Object> hashKey = new ArrayList<>();
        hashKey.add("001");
        hashKey.add("002");
        List<JSONObject> objects = (List<JSONObject>) (Object) hashOperations.multiGet(KEY_HASH + dict, hashKey);

        JSONObject o = (JSONObject) hashOperations.get(KEY_HASH + dict, "002");
        Person person1 = JSONObject.toJavaObject(o, Person.class);
        System.out.println(person1);
    }


    private <T> List<T> getTypeList(List<Object> list, Class<T> tClass) {
        return (List<T>) list;
    }
}
