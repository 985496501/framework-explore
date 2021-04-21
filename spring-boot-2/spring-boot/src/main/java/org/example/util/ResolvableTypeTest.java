package org.example.util;

import org.example.beans.postprocessor.Configuraer;
import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jinyun
 * @date: 2021/4/21
 */
public class ResolvableTypeTest {


    @Test
    public void forRawClassTest() {
        ResolvableType resolvableType = ResolvableType.forRawClass(Configuraer.class);


    }

    @Test
    public void classUtilsTest() {
        // 这个api 可以快速实现 右边的 是不是 左边的子类;
        System.out.println(ClassUtils.isAssignable(Map.class, HashMap.class));
    }
}
