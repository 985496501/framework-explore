package org.example.cache;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;

/**
 * @author: jinyun
 * @date: 2021/2/8
 */
public class CacheTest {

    public static void main(String[] args) {
        // mybatis的真实的缓存就是 PerpetualCache
        Cache cache = new PerpetualCache("testCache");
        cache.putObject("object", new NamedClass("namedObject"));
        // 一个非常非常简单的缓存类


        // 看看包装的cache, 这些缓存通过链表相互连接, 当缓存一个key的时候, 会经历 缓存链条
        // 每个类具备单一功能职责, 负责处理自己的任务, 每个cacheImpl都执行完毕, 缓存存入
        // 每个类的实现 也都非常简单, 可以稍微看看


    }



    static class NamedClass {
        private String name;

        public NamedClass(String name) {
            this.name = name;
        }
    }

}
