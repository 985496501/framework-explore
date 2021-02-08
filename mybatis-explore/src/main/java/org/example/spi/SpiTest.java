package org.example.spi;

import java.sql.Driver;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author: jinyun
 * @date: 2021/2/8
 */
public class SpiTest {
    public static void main(String[] args) {
        ServiceLoader<Driver> serviceLoader = ServiceLoader.load(Driver.class);
        Iterator<Driver> iterator = serviceLoader.iterator();
        if (iterator.hasNext()) {
            Driver next = iterator.next();
            System.out.println(next);
        }
    }
}
