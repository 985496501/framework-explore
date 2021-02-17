package org.example.jol.util;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author: Liu Jinyun
 * @date: 2021/2/17/21:55
 */
public class LayoutUtil {

    public static void print(Object o) {
        System.out.println();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
    }
}
