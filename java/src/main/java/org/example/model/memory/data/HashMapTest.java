package org.example.model.memory.data;

import org.junit.Test;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class provides a skeletal implementation of the Map interface, to minimize
 * the effort required to implement this interface.
 * 这个提供了map接口的骨架实现, 为了减少 实现这个接口的工作量;
 *
 * 如果为了实现 一个无法修改 的map, 开发者仅仅需要继承这个类, 提供一个entrySet() 方法的实现, 返回这个 entry 的 SET-VIEW
 * 返回的这个set 最好实现了 AbstractSet, 并且实现类不能支持 add, remove, iterator 不支持remove.
 *
 *
 *
 *
 * @author: jinyun
 * @date: 2021/4/28
 */
public class HashMapTest {

    /**
     * HashMap的源码是面试问的重点, 那么应该怎么回答才会体现出 你的功底很深呢?
     */
    @Test
    public void hashMapTest() {
        /**
         * 创建map建立根据问题的规模设置 initialCapacity.
         * 然后会加入 default load factor = 0.75   3/4
         * 就申请了一块内存: 设置了两个属性
         *      loadFactor=0.75
         *      threshold= 这里会根据位运算 推到出一个 a power of 2 size for the given target capacity, 14 默认转成16
         *      这里传参 如果需要3个 就直接给4是一样的;
         *      default_capacity=16  max_capacity=2^30
         *
         * 主要就是看它的put(k, v):
         * 1. 首先调用了 hash(key)
         * 2. putVal(hashVal, key, value, onlyIfAbsent false, evict true)
         *    这个方法是final 方法, onlyIfAbsent 代表如果没有这个key才会塞进去, 写死false, 说明会覆盖\
         *    evict: false, this table is in creation mode. todo: evict 到底搞啥东西?
         *
         *    下面开始无情的判断了; 首先定义了一个内置Node 结构体, 实现了Entry, 重写了3个方法, 4个属性值, hash, key, value, next;
         *
         *    1. 创建了一个 Node数组 变量, 一个临时节点 Node, n 数组长度, i 具体是哪个索引
         *    如果还没有创建这个数组 就会创建这个数组, 调用数组的方法 resize() 方法,
         *
         *    通过 散列表 table 确定这个长度是 2^n  让它-1 & hash 获取它的hash bin的坐标判断如果这个坐标没有值
         *    那我就创建一个node, 设置这个值;
         *    ++ modCount; 记录这个 散列表
         *
         *
         *
         *
         *
         *
         *
         * 3. 必不可少的方法 resize() 方法, 这个方法显然就是创建 和 扩容 Node<K, V>[] 的;
         *   先看创建数组的逻辑:
         *   oldThr 默认就是16 这边判断这个值>0 就把这个值设置成新的newCap
         *   newThr 没有设置值, ft = 16 * 0.75 = 9   newThr = 9
         *   然后创建 一个 newCap的node[] 让table= newTab
         *   如果老的table不是空的说明这个需要扩容, todo: 需要执行其他任务
         *   否则  就把初始化的数组 直接返回;
         *
         *
         *
         *
         *
         *
         *
         */
        Map<String, String> map = new HashMap<>(16);
        map.put("name", "jinyun liu");
    }


    @Test
    public void abstractMapTest() {

    }

    /**
     * 自定义实现自己的map
     * 实现类不能支持 add, remove, iterator 不支持remove.
     *
     * @param <K>
     * @param <V>
     */
    static class ImmutableMap<K, V> extends AbstractMap<K, V> {
        /**
         * 使用什么基础的数据结构存储数据?
         * AbstractMap 已经模板实现了很多方法, 都是基于下面这个获取所有的Set<Entry<K, V>>
         */


        @Override
        public Set<Entry<K, V>> entrySet() {
            throw new UnsupportedOperationException("");
        }

        @Override
        public V put(K key, V value) {
            throw new UnsupportedOperationException("This is an immutable map, cannot put");
        }

        @Override
        public V remove(Object key) {
            throw new UnsupportedOperationException("This is an immutable map, cannot remove");
        }
    }

}
