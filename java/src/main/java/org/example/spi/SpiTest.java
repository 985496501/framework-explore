package org.example.spi;

import java.sql.Driver;
import java.util.ServiceLoader;

/**
 * 必须指定spi文件类型, idea支持这种文件索引, 如果没有指定  retrieve null.
 *
 * @author: jinyun
 * @date: 2021/2/8
 */
public class SpiTest {
    public static void main(String[] args) {
        ServiceLoader<Driver> load = ServiceLoader.load(Driver.class);
        if (load.iterator().hasNext()) {
           System.out.println(load.iterator().next());
        }
    }
}
