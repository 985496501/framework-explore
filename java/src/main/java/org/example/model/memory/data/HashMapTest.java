package org.example.model.memory.data;

import org.junit.Test;

import java.util.*;

/**
 * This class provides a skeletal implementation of the Map interface, to minimize
 * the effort required to implement this interface.
 * 这个提供了map接口的骨架实现, 为了减少 实现这个接口的工作量;
 *
 * 如果为了实现 一个无法修改 的map, 开发者仅仅需要继承这个类, 提供一个entrySet() 方法的实现, 返回这个 entry 的 SET-VIEW
 * 返回的这个set 最好实现了 AbstractSet, 并且实现类不能支持 add, remove, iterator 不支持remove.
 *
 * <h2>内存结构, 存储模式</h2>
 * <p>数组使用直接寻址的方式, 可以实现顺序查找的功能; 不仅如此因为地址连续, 可以充分发挥CPU高速缓存的优势;
 * 但是它的灵活性不强, 属于大块内存分配, 如果有很多大数组存在的话, 会存在很多的 内存碎片; 也是资源的一种浪费;
 *
 * <p>为了充分利用内存, 提供链式存储的方法, 属于见缝插针, 充分利用了内存碎片;
 * 这个小和大是相对概念, 也可能一个链式结点也比一个数组的size要大(bytes)
 *
 * <p>Java作为一种高级语言, 提供了间接操作内存的方式, 我们可以尽情的定义各种数据结构, 在heap上尽情玩耍
 * 数组我们可以直接使用; 链式存储我们使用 对象节点自行封装;
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
         *    这个方法是final 方法, onlyIfAbsent 代表如果没有这个key才会塞进去, 写死false, 说明会覆盖
         *    evict: false, this table is in creation mode. todo: evict 到底搞啥东西?
         *
         *    下面开始无情的判断了; 首先定义了一个内置Node 结构体, 实现了Entry, 重写了3个方法, 4个属性值, hash, key, value, next;
         *
         *    1. 创建了一个 Node数组 变量, 一个临时节点 Node, n 数组长度, i 具体是哪个索引
         *    如果还没有创建这个数组 就会创建这个数组, 调用数组的方法 resize() 方法,
         *
         *    通过 散列表 table 确定这个长度是 2^n  让它  (size-1) & hash 获取它的hash,   p = table[i]
         *    1> if p = null 创建一个Node塞进去;
         *    2> if p != null, 创建一个新的Node e
         *      2.1> if hash(p) == hash(e.key) && (key == key || equals())  e=p; 直接将e指向原来的p Node
         *          最后会拿到e.value 也就是就的value, 如果原来的value=null 或者 允许覆盖 就直接把新的 value 覆盖;
         *          afterNodeAccess(e): node获取之后的操作... 什么也没做.... 交给子类实现;
         *          将替换掉的 oldVal 直接返回; 因为是覆盖, 所以下面的逻辑，修改结果的次数, table Size, resize() 都不用操作;
         *      2.2> p 是 TreeNode 类型吗? 一般往里面塞入, 不会类型转换 TreeNode 是 entry类型, entry是Node类型
         *
         *      2.3> e = p.next, p=e,  除了数组中的那个Node 坐标为-1吧, 然后启动一个计数器, TREEIFY_THRESHOLD = 8.
         *           binCount >= 7; -1 0 1 ... 5;  一个bin的链长度达到7 就会树化; e=null; 直接下面的是否resize()
         *           这里就看出 算法的功底了, 使用了双指针 p e 完成了所有的操作, 真的特别舒服; 就是链表的长度exclude(table[i])
         *           == treeify_threshold, 就会发生树化, treeifyBin(tab, hash); 针对 table[] 对应的hash 进行 treeify
         *          todo: treeify()
         *
         *
         *    那我就创建一个node, 设置这个值;
         *    ++ modCount; 记录这个 散列表
         *    ++ size 这个就是entry的大小;
         *    同时会比较size 和  threshold 的值, capacity*loadFactor的大小 就是这个阈值 等于整个容量的3/4 就会重新hash table;
         *    初始capacity=16  threshold=9
         *    初始的table[16] 0 1 ... 15; if hash() 算法非常好, 让10个值分别处于10个bin, 那么就会出现 resize();
         *          仅仅是数组的resize() 一次扩容为原来的2倍 table[32] 然后 threshold = 24
         *    必须是 size 严格 > threshold, 才会 resize(), 这个resize() 是插入之后发生的. 还是插入之后大于 threshold 才会resize();
         *          todo: resize()
         *    然后调用afterNodeInsertion(evict); 这是一个空实现, 没有做任何操作, 看了下它子类实现大体是淘汰
         *    最早的数据;
         *
         *    2.
         *
         *
         *
         *
         *
         *
         *
         *
         *
         * 3. 必不可少的方法 resize() 方法, 这个方法显然就是创建 和 扩容 Node<K, V>[] 的;
         *   先看创建数组的逻辑:
         *   3.1> oldThr 默认就是16 这边判断这个值>0 就把这个值设置成新的newCap
         *   newThr 没有设置值, ft = 16 * 0.75 = 9   newThr = 9
         *   然后创建 一个 newCap的node[] 让table= newTab
         *   如果老的table不是空的说明这个需要扩容, todo: 需要执行其他任务
         *   否则  就把初始化的数组 直接返回;
         *   3.2> 如果capacity > threshold 就 resize()
         *   直接 oldTab;  newTab = new Node(newCap)
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
    public void andTest() {
        // 任何数和 & 运算是  比如15;
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            System.out.println(random.nextInt() & 15);
        }
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
