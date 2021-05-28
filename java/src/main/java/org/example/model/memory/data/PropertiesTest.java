package org.example.model.memory.data;

import org.junit.Test;

import java.util.Properties;

/**
 * @author: jinyun
 * @date: 2021/5/26
 */
public class PropertiesTest {

    @Test
    public void propertiesTest() {
        Properties p = new Properties();
        p.setProperty("name", "nacos");
        System.out.println(p.get("name"));
    }


}
