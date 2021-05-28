package org.example.model.memory.data;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: jinyun
 * @date: 2021/5/26
 */
public class LinkedHashMapTest {
    private final Map<String, Integer> map = new LinkedHashMap<>();

    @Test
    public void featuresTest() {
        map.put("liu", 1);
        map.put("jin", 1);
        map.put("yun", 1);

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    @Test
    public void stringTest() {
        String str = "name=liujinyun";
        int l = str.indexOf("=");
        System.out.println(l);
        System.out.println(str.substring(0, l));
        System.out.println(str.substring(l + 1));
    }
}
