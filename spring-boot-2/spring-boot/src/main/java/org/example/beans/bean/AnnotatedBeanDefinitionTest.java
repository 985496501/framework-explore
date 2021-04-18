package org.example.beans.bean;

import org.example.EntryApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigUtils;

/**
 * 注解相关的 beanDefinition.
 *
 * @author: Liu Jinyun
 * @date: 2021/4/18/22:24
 */
public class AnnotatedBeanDefinitionTest {

    /**
     * 如何获取注解相关的 AnnotatedBeanDefinition 对象呢？
     * see {@link AnnotatedBeanDefinition} 这个类的作用就是为了暴露 AnnotationMetadata 对象
     *
     * see {@link AnnotatedGenericBeanDefinition} 这个是最通用的 AnnotatedBeanDefinition 的实现；
     */
    @Test
    public void initAnnotatedBeanDefinitionTest() {
        AnnotatedBeanDefinition anDef = new AnnotatedGenericBeanDefinition(EntryApplication.class);
        // 这个方法处理 @Lazy @Role @Description @DependOn 这4个注解填充到BeanDefinition属性
        AnnotationConfigUtils.processCommonDefinitionAnnotations(anDef);

        // 然后进行IOC 实例化
        // 说明上面这4个注解 需要填充 4个bean定义的属性, 用于在 IOC  refresh() 的时候进行一些对应的逻辑处理;

        // role: 这个就是如果已经存在这个BeanDefinition的时候 再允许后来的BeanDefinition进行覆盖的时候
        // 这个role通过比较可以打印日志;
    }
}
