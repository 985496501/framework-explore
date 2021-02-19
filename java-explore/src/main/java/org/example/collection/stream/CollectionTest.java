package org.example.collection.stream;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * map(T::xx)
 * .collect(Collectors.xxx())
 *
 * @author: jinyun
 * @date: 2021/2/9
 */
public class CollectionTest {

    String[] arr = new String[]{"a", "b", "c", "d"};

    /**
     * 开发中常常使用这个流
     * 用的时候 学习一下
     */
    @Test
    public void mapTest() {
        Map<String, List<String>> keyListMap = new HashMap<>(4);

        for (int i = 0; i < arr.length; i++) {
            List<String> list = keyListMap.computeIfAbsent("key", k -> new ArrayList<>());
            list.add(arr[i]);
        }

        // 这个函数就是把 每个key 的 value 取出来然后一顿操作 然后替换原来的values
        keyListMap.replaceAll((k, vs) -> vs.stream().distinct().collect(Collectors.collectingAndThen(Collectors.toList(),
                Collections::unmodifiableList)));
    }
}
