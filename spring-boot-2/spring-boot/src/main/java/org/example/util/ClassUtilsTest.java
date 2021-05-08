package org.example.util;

import org.example.beans.bean.scan.SimpleBean;
import org.junit.jupiter.api.Test;
import org.springframework.util.ClassUtils;

/**
 * @author: jinyun
 * @date: 2021/5/8
 */
public class ClassUtilsTest {

    /**
     * 我们通过上层引用变量 获取 堆内存对象的class类型  会获取实际的类型;
     */
    @Test
    public void getUserClassTest() {
        Object o = new SimpleBean();
        System.out.println(o.getClass());
        Class<?> userClass = ClassUtils.getUserClass(o);
        System.out.println(userClass);
    }
}
