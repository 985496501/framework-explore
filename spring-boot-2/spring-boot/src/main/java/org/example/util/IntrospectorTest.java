package org.example.util;

import org.junit.jupiter.api.Test;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;

/**
 * Introspector：这个是jdk带的bean的工具 自省器
 * introspect: 这个单词真的老外经常用啊, 自己对自己做一些事情，把自己看透了的感觉，
 * 翻译成中文就是自省 反省，  自我反省，  就是自己主动 找出自身的缺陷 或者 不足 自我弥补的
 * 要找到那种感觉，  intro： 向内  spect: 看， 自己看向自己的内心.
 *
 *
 *
 * @author: Liu Jinyun
 * @date: 2021/4/18/17:04
 */
public class IntrospectorTest {

    /**
     * capitalize: 印刷首字母大写， 使首字母大写； 变现，为..提供资本
     *
     * decapitalize: 首字母小写
     */
    @Test
    public void decapitalizeTest() {
        String shortName = ClassUtils.getShortName(IntrospectorTest.class);
        System.out.println("shortName: " + shortName);
        String decapitalize = Introspector.decapitalize(shortName);
        System.out.println("decapitalizeName: " + decapitalize);
    }
}
